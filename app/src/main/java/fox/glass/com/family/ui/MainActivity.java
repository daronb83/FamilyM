package fox.glass.com.family.ui;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import fox.glass.com.family.R;
import fox.glass.com.family.model.DataSet;
import fox.glass.com.family.model.Server;

public class MainActivity extends FragmentActivity {

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
        LoginFragment loginFragment = (LoginFragment) fragmentManager.findFragmentById(R.id.fragframe);

        if (loginFragment == null) {
            loginFragment = new LoginFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.fragframe, loginFragment)
                    .commit();
        }

        Log.i(ID, "Login Fragment loaded");
    }

    private void loadMapFragment() {
        Log.i(ID, "Loading Map Fragment");

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.fragframe);

        if (mapFragment == null) {
            mapFragment = new MapFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.fragframe, mapFragment)
                    .commit();
        }

        Log.i(ID, "Map Fragment loaded");
    }
}
