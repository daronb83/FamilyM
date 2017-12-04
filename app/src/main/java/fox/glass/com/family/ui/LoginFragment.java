package fox.glass.com.family.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import fox.glass.com.family.R;
import fox.glass.com.family.model.DataSet;
import fox.glass.com.family.model.Server;
import fox.glass.com.family.net.ProxyServer;
import fox.glass.com.shared.database.Person;
import fox.glass.com.shared.requests.*;
import fox.glass.com.shared.responses.LoginResponse;
import fox.glass.com.shared.responses.MessageResponse;
import fox.glass.com.shared.responses.Response;

/**
 * Allows the user to login or register
 */
public class LoginFragment extends Fragment {
    private static String ID = "FamilyMap LoginFragment";

    private Context context;
    private LoginRequest lRequest;
    private RegisterRequest rRequest;
    private Button loginButton;
    private Button registerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = this.getContext();
        lRequest = new LoginRequest();
        rRequest = new RegisterRequest();

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        addTextListeners(view);
        addButtonListeners(view);

        return view;
    }

    private void makeToast(String message) {
        Toast toast = Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.show();
    }

    /**
     * Adds listeners to all textEdit fields
     * @param view the inflated loginFragment view
     */
    private void addTextListeners(View view) {
        EditText hostEditText = (EditText) view.findViewById(R.id.host_input);
        hostEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Server.setHost("http://" + s.toString().trim());
                checkValues();
            }
        });

        EditText portEditText = (EditText) view.findViewById(R.id.port_input);
        portEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Server.setPort(s.toString().trim());
                checkValues();
            }
        });

        EditText usernameEditText = (EditText) view.findViewById(R.id.username_input);
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                lRequest.setUserName(s.toString().trim());
                rRequest.setUserName(s.toString().trim());
                checkValues();
            }
        });

        EditText passwordEditText = (EditText) view.findViewById(R.id.password_input);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                lRequest.setPassword(s.toString().trim());
                rRequest.setPassword(s.toString().trim());
                checkValues();
            }
        });

        EditText firstNameEditText = (EditText) view.findViewById(R.id.first_input);
        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                rRequest.setFirstName(s.toString().trim());
                checkValues();
            }
        });

        EditText lastNameEditText = (EditText) view.findViewById(R.id.last_input);
        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                rRequest.setLastName(s.toString().trim());
                checkValues();
            }
        });

        EditText emailEditText = (EditText) view.findViewById(R.id.email_input);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                rRequest.setEmail(s.toString().trim());
                checkValues();
            }
        });
    }

    /**
     * Adds listeners to all buttons
     * @param view the inflated loginFragment view
     */
    private void addButtonListeners(View view) {
        RadioGroup genderRadioGroup = (RadioGroup) view.findViewById(R.id.gender_group);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.female_button) {
                    rRequest.setGender("f");
                }
                else if (checkedId == R.id.male_button) {
                    rRequest.setGender("m");
                }

                checkValues();;
            }
        });

        loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                new AsyncLogin().execute();
            }
        });

        registerButton = (Button) view.findViewById(R.id.register_button);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setEnabled(false);
                new AsyncRegister().execute();
            }
        });
    }

    /**
     * Determines if either the login or register button should be enabled
     */
    private void checkValues() {

        if (checkServerValues() && checkLoginRequestValues()) {
            checkRegisterRequestValues();
        }
    }

    private boolean checkServerValues() {
        if (Server.getHost().length() > 0 && Server.getPort().length() > 0) {
            return true;
        }

        loginButton.setEnabled(false);
        registerButton.setEnabled(false);
        return false;
    }

    private boolean checkLoginRequestValues() {

        if (lRequest.getUserName().length() > 0 && lRequest.getPassword().length() > 0) {
            loginButton.setEnabled(true);
            return true;
        }

        loginButton.setEnabled(false);
        return false;
    }

    private void checkRegisterRequestValues() {
        if (rRequest.getFirstName().length() > 0 && rRequest.getLastName().length() > 0 &&
                rRequest.getEmail().length() > 0 && rRequest.getGender().length() > 0) {

            registerButton.setEnabled(true);
        }
        else {
            registerButton.setEnabled(false);
        }
    }

    private class AsyncLogin extends AsyncTask<LoginRequest, Integer, String> {

        @Override
        protected String doInBackground(LoginRequest... params) {
            Log.i(ID, "Logging in: " + lRequest.getUserName() + ", Password: " + lRequest.getPassword());
            ProxyServer server = new ProxyServer();
            Response response;

            try {
                response = server.login(lRequest, context);

                if (response.getClass() == LoginResponse.class) {

                    server.LoadData((LoginResponse)response, context);
                    LoginResponse login = (LoginResponse)response;
                    Person person = DataSet.getPersonById(login.getPersonID());

                    if (person != null) {
                        return person.getFirstName() + " " + person.getLastName() + " is logged in";
                    }
                }
                else {
                    MessageResponse message = (MessageResponse) response;
                    return "Login failed: " + message.getMessage();
                }
            }
            catch (IOException e) {
                return ("Login failed: " + e.getMessage());
            }

            return ("Login failed");
        }

        @Override
        protected void onPostExecute(String result) {
            loginButton.setEnabled(true);
            Log.i("AsyncLogin", result);
            makeToast(result);
        }
    }

    private class AsyncRegister extends AsyncTask<RegisterRequest, Integer, String> {

        @Override
        protected String doInBackground(RegisterRequest... params) {
            Log.i(ID, "Registering: " + rRequest.getUserName() + ", Password: " + rRequest.getPassword());
            ProxyServer server = new ProxyServer();
            Response response;

            try {
                response = server.register(rRequest, context);


                if (response.getClass() == LoginResponse.class) {
                    try {
                        server.LoadData((LoginResponse)response, context);
                        LoginResponse login = (LoginResponse) response;
                        Log.i("AuthToken", login.getAuthToken());
                        Person person = DataSet.getPersonById(login.getPersonID());

                        if (person != null) {
                            return person.getFirstName() + " " + person.getLastName() + " is registered";
                        }
                        else {
                            return "Registration failed: Person not found";
                        }
                    }
                    catch (IOException e) {
                        return "Registration failed: " + e.getMessage();
                    }
                }
                else {
                    MessageResponse message = (MessageResponse) response;
                    return "Registration failed: " + message.getMessage();
                }
            }
            catch (IOException e) {
                return ("Registration failed: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            registerButton.setEnabled(true);
            Log.i("AsyncRegister", result);
            makeToast(result);
        }
    }
}
