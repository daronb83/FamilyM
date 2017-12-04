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
     * @throws IOException e
     */
    public Response login(LoginRequest request, Context context) throws IOException {
        String uri = context.getString(R.string.LoginUrl);
        Response response = getLoginResponse(request, uri);

        if (response.getClass() == LoginResponse.class) {
            Log.i("ProxyServer", "Logged in successfully");
            return response;
        }
        else {
            MessageResponse messageResponse = (MessageResponse) response;
            Log.i("ProxyServer", "Login failed: " + messageResponse.getMessage());
            return messageResponse;
        }
    }

    /**
     * Attempts to register a new user with the FamilyServer
     *
     * @param request a RegisterRequest containing new account info
     * @param context the current activity's context
     * @return LoginResponse containing an authToken
     * @throws IOException e
     */
    public Response register(RegisterRequest request, Context context) throws IOException {
        String uri = context.getString(R.string.RegisterUrl);
        Response response = getLoginResponse(request, uri);

        if (response.getClass() == LoginResponse.class) {
            Log.i("ProxyServer", "Registered successfully");
            return response;
        }
        else {
            MessageResponse messageResponse = (MessageResponse) response;
            Log.i("ProxyServer", "Registration failed: " + messageResponse.getMessage());
            return messageResponse;
        }
    }

    public boolean LoadData(LoginResponse login, Context context) throws IOException {
        if (BuildConfig.DEBUG && login == null) throw new AssertionError("LoginResponse is null");

        PersonsResponse personsResponse = getPersons(login, context);
        EventsResponse eventsResponse = getEvents(login, context);

        if (personsResponse != null && eventsResponse != null) {
            DataSet.setPersons(personsResponse.getData());
            DataSet.setEvents(eventsResponse.getData());
            return true;
        }

        return false;
    }

    private EventsResponse getEvents(LoginResponse login, Context context) throws IOException {
        if (BuildConfig.DEBUG && login == null) throw new AssertionError("LoginResponse is null");

        String uri = context.getString(R.string.EventUrl);
        Response response = getDataResponse(uri, login.getAuthToken(), new EventsResponse());

        if (response.getClass() == EventsResponse.class) {
            return (EventsResponse) response;
        }
        else {
            MessageResponse messageResponse = (MessageResponse) response;
            throw new IOException(messageResponse.getMessage());
        }
    }

    private PersonsResponse getPersons(LoginResponse login, Context context) throws IOException {
        if (BuildConfig.DEBUG && login == null) throw new AssertionError("LoginResponse is null");

        String uri = context.getString(R.string.PersonUrl);
        Response response = getDataResponse(uri, login.getAuthToken(), new PersonsResponse());

        if (response.getClass() == PersonsResponse.class) {
            return (PersonsResponse) response;
        }
        else {
            MessageResponse messageResponse = (MessageResponse) response;
            throw new IOException(messageResponse.getMessage());
        }
    }

    private Response getDataResponse(String uri, String authToken, Response typeOfResponse) {
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
                Response output = (Response) decoder.decodeStream(responseBody, typeOfResponse);
                responseBody.close();

                if (output == null) {
                    Log.i("ProxyServer", "Data Response failed");
                    return new MessageResponse("Unable to retrieve data");
                }
                else {
                    Log.i("ProxyServer", "Data Response received successfully");
                    return output;
                }
            }
            else {
                Log.i("ProxyServer", "Data Response failed: " + connection.getResponseMessage());
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
                LoginResponse output =
                        (LoginResponse) decoder.decodeStream(responseBody, new LoginResponse());
                responseBody.close();

                if (output == null) {
                    Log.i("ProxyServer", "Login Response failed: invalid data");
                    return new MessageResponse("Login Response failed: invalid login data");
                }
                else {
                    Log.i("ProxyServer", "Login Response received successfully");
                    return output;
                }
            }
            else {
                Log.i("ProxyServer", "Data Response failed: " + connection.getResponseMessage());
                return new MessageResponse(connection.getResponseMessage());
            }
        }
        catch (IOException e) {
            Log.e(ID, e.getMessage(), e);
            return new MessageResponse(e.getMessage());
        }
    }

}

