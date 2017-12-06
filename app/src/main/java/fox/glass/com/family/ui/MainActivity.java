package fox.glass.com.family.ui;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import fox.glass.com.family.R;
import fox.glass.com.family.model.DataSet;
import fox.glass.com.family.model.Server;
import fox.glass.com.shared.database.Person;

public class MainActivity extends AppCompatActivity {

    private static final String ID = "Family Map MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataSet.instance();
        Server.instance();
        loadLoginFragment();
    }

    private void loadLoginFragment() {
        Log.i(ID, "Loading login fragment");

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        LoginFragment loginFragment = (LoginFragment) fragmentManager.findFragmentById(R.id.fragment_container);

        if (loginFragment == null) {
            loginFragment = new LoginFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, loginFragment)
                    .commit();
        }

        Log.i(ID, "Login Fragment loaded");
    }

    public void loadMapFragment(Person person) {
        Log.i(ID, "Loading Map Fragment");

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        MapFragment mapFragment = new MapFragment();
        mapFragment.initialize(person);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, mapFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.i(ID, "Map Fragment loaded");
    }

    // csweb-HP-Compaq 192.168.9.10
}
