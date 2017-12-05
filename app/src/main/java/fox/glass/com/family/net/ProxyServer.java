package fox.glass.com.family.net;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fox.glass.com.family.BuildConfig;
import fox.glass.com.family.R;
import fox.glass.com.family.model.DataSet;
import fox.glass.com.family.model.Server;
import fox.glass.com.shared.json.*;
import fox.glass.com.shared.requests.*;
import fox.glass.com.shared.responses.*;

/**
 * Communicates with FamilyServer
 */
public class ProxyServer {

    private static final String ID = "FamilyMap ProxyServer";
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static Server server = Server.instance();

    /**
     * Attempts to log in the user to the FamilyServer
     *
     * @param request a LoginRequest containing a username and password
     * @param context the current activity's context
     * @return a LoginResponse containing an authToken
     */
    public Response login(LoginRequest request, Context context) {
        String uri = context.getString(R.string.LoginUrl);
        return getLoginResponse(request, uri);
    }

    /**
     * Attempts to register a new user with the FamilyServer
     *
     * @param request a RegisterRequest containing new account info
     * @param context the current activity's context
     * @return LoginResponse containing an authToken
     */
    public Response register(RegisterRequest request, Context context) {
        String uri = context.getString(R.string.RegisterUrl);
        return getLoginResponse(request, uri);
    }

    /**
     * Loads the user's data from a successful login response
     *
     * @param login a successful login response
     * @param context the current activity's context
     * @return true if the data was loaded successfully
     */
    public Response LoadData(LoginResponse login, Context context) {
        if (BuildConfig.DEBUG && login == null) throw new AssertionError("LoginResponse is null");

        String uri = context.getString(R.string.PersonUrl);
        PersonsResponse personsResponse =
                (PersonsResponse)getAuthResponse(uri, login.getAuthToken(), new PersonsResponse());

        uri = context.getString(R.string.EventUrl);
        EventsResponse eventsResponse =
                (EventsResponse)getAuthResponse(uri, login.getAuthToken(), new EventsResponse());

        if (personsResponse.getMessage().length() == 0 && eventsResponse.getMessage().length() == 0) {
            DataSet.setPersons(personsResponse.getData());
            DataSet.setEvents(eventsResponse.getData());
            return login;
        }
        else {

            if (personsResponse.getMessage().length() > 0) {
                Log.i("ProxyServer", personsResponse.getMessage());
                return personsResponse;
            }

            if (eventsResponse.getMessage().length() > 0) {
                Log.i("ProxyServer", eventsResponse.getMessage());
                return eventsResponse;
            }

            return new MessageResponse("Internal server error");
        }
    }

    private Response getAuthResponse(String uri, String authToken, Response instanceOfDesiredResponse) {
        if (BuildConfig.DEBUG && !Server.isInstantiated()) throw new AssertionError("Missing server info");
        if (BuildConfig.DEBUG && authToken == null) throw new AssertionError("authToken is null");
        if (BuildConfig.DEBUG && uri == null) throw new AssertionError("uri is null");

        try {
            URL url = new URL(server.toString() + uri);
            Log.i("ProxyServer", GET_METHOD + " URL: " + url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET_METHOD);
            connection.setDoOutput(false);
            connection.setRequestProperty("Authorization", authToken);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.i("ProxyServer", "Response: " + connection.getResponseMessage());
                InputStream responseBody = connection.getInputStream();
                JsonDecoder decoder = new JsonDecoder();
                Response output = (Response) decoder.decodeStream(responseBody, instanceOfDesiredResponse);
                responseBody.close();
                return output;
            }
            else {
                Log.i("ProxyServer", "Response failed: " + connection.getResponseMessage());
                return new MessageResponse(connection.getResponseMessage());
            }
        }
        catch (IOException e) {
            Log.e(ID, e.getMessage(), e);
            return new MessageResponse(e.getMessage());
        }
    }

    private Response getLoginResponse(Request request, String uri) {

        if (BuildConfig.DEBUG && !Server.isInstantiated()) throw new AssertionError("Missing server info");
        if (BuildConfig.DEBUG && request == null) throw new AssertionError("Request is null");
        if (BuildConfig.DEBUG && uri == null) throw new AssertionError("uri is null");

        try {
            URL url = new URL(server.toString() + uri);
            Log.i("ProxyServer", POST_METHOD + " URL: " + url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(POST_METHOD);
            connection.setDoOutput(true);
            connection.connect();

            OutputStream requestBody = connection.getOutputStream();
            JsonEncoder encoder = new JsonEncoder();
            encoder.encodeToStream(request, requestBody);
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.i("ProxyServer", "Response: " + connection.getResponseMessage());
                InputStream responseBody = connection.getInputStream();
                JsonDecoder decoder = new JsonDecoder();
                LoginResponse output = (LoginResponse) decoder.decodeStream(responseBody, new LoginResponse());
                responseBody.close();
                return output;
            }
            else {
                Log.i("ProxyServer", "Login failed: " + connection.getResponseMessage());
                return new MessageResponse(connection.getResponseMessage());
            }
        }
        catch (IOException e) {
            Log.e(ID, e.getMessage(), e);
            return new MessageResponse(e.getMessage());
        }
    }

}

