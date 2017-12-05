package fox.glass.com.family.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import fox.glass.com.family.R;
import fox.glass.com.family.model.DataSet;
import fox.glass.com.family.model.Server;
import fox.glass.com.family.net.ProxyServer;
import fox.glass.com.shared.database.Person;
import fox.glass.com.shared.requests.*;
import fox.glass.com.shared.responses.LoginResponse;
import fox.glass.com.shared.responses.Response;

/**
 * Allows the user to login or register
 */
public class LoginFragment extends Fragment {
    private static String ID = "FamilyMap LoginFragment";

    private Context context;
    private LoginRequest loginRequest;
    private RegisterRequest rRequest;
    private Button loginButton;
    private Button registerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = this.getContext();
        loginRequest = new LoginRequest();
        rRequest = new RegisterRequest();

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        addInputListeners(view);
        addButtonListeners(view);

        return view;
    }

    /**
     * Adds listeners to all textEdit/radio fields
     * @param view the inflated loginFragment view
     */
    private void addInputListeners(View view) {
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
                loginRequest.setUserName(s.toString().trim());
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
                loginRequest.setPassword(s.toString().trim());
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
    }

    /**
     * Adds listeners to all buttons
     * @param view the inflated loginFragment view
     */
    private void addButtonListeners(View view) {
        loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);
                new AsyncLogin().execute();
            }
        });

        registerButton = (Button) view.findViewById(R.id.register_button);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);
                new AsyncRegister().execute();
            }
        });
    }

    /**
     * Determines if either the login or register button should be enabled
     */
    private void checkValues() {
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        if (Server.getHost().length() > 0 && Server.getPort().length() > 0) {
            if (loginRequest.getUserName().length() > 0 && loginRequest.getPassword().length() > 0) {
                loginButton.setEnabled(true);

                if (rRequest.getFirstName().length() > 0 && rRequest.getLastName().length() > 0 &&
                        rRequest.getEmail().length() > 0 && rRequest.getGender().length() > 0) {

                    registerButton.setEnabled(true);
                }
            }
        }
    }

    /**
     * Handles Asynchronous login requests
     */
    private class AsyncLogin extends AsyncTask<LoginRequest, Integer, Response> {

        @Override
        protected Response doInBackground(LoginRequest... params) {
            Log.i(ID, "Logging in: " + loginRequest.getUserName() + ", Password: " + loginRequest.getPassword());
            ProxyServer server = new ProxyServer();
            Response response;

            response = server.login(loginRequest, context);

            if (response.getMessage().length() == 0) {
                Log.i(ID, "Login successful, loading data");
                return server.LoadData((LoginResponse)response, context);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            postAsync(response);
        }
    }

    /**
     * Handles Asynchronous registration requests
     */
    private class AsyncRegister extends AsyncTask<RegisterRequest, Integer, Response> {

        @Override
        protected Response doInBackground(RegisterRequest... params) {
            Log.i(ID, "Registering: " + rRequest.getUserName() + ", Password: " + rRequest.getPassword());
            ProxyServer server = new ProxyServer();
            Response response;

            response = server.register(rRequest, context);

            if (response.getMessage().length() == 0) {
                Log.i(ID, "Login successful, loading data");
                return server.LoadData((LoginResponse)response, context);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            postAsync(response);
        }
    }

    /**
     * Handles the response from either of the Async functions
     */
    private void postAsync(Response response) {
        checkValues(); // re-enable the correct buttons

        if (response.getMessage().length() > 0) {
            Log.i("AsyncLogin", "Login/Register failed: " + response.getMessage());
            makeToast("Error: " + response.getMessage());
        }
        else {
            LoginResponse data = (LoginResponse) response;
            Person person = DataSet.getPersonById(data.getPersonID());

            if (person != null) {
                makeToast("Login successful for " + person.getFirstName() + " " + person.getLastName());
                MainActivity parent = (MainActivity)getActivity();
                parent.loadMapFragment(person);
            }
            else {
                makeToast("Failed to load user data. Please try again.");
            }
        }
    }

    private void makeToast(String message) {
        Toast toast = Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.show();
    }
}
