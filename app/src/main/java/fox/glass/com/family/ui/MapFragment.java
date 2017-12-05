package fox.glass.com.family.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fox.glass.com.family.BuildConfig;
import fox.glass.com.family.R;
import fox.glass.com.shared.database.Person;

/**
 * Holds a Google or Amazon map fragment
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    private String tag = "MapFragment";
    private static final int PERMISSION_CODE = 1253;

    private Person user;

    public void initialize(Person user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (BuildConfig.DEBUG && user == null){
            throw new AssertionError("MapFragment must initialize() before committing");
        }

        Log.i(tag, "In MapFragment with " + user.getFirstName() + " " + user.getLastName());
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i(tag, "Loading Map Fragment");
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(tag, "Map ready");

        LatLng birthLocation = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions()
                .position(birthLocation)
                .title("User Birth Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(birthLocation));
    }
}
