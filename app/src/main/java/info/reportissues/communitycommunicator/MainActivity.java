package info.reportissues.communitycommunicator;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import info.reportissues.communitycommunicator.dialogs.LoginOrRegisterDialog;
import info.reportissues.communitycommunicator.models.Issue;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static info.reportissues.communitycommunicator.R.id.map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int googlePlayStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        if(googlePlayStatus != 0) {
            GoogleApiAvailability.getInstance().getErrorDialog(this,googlePlayStatus,0);
        } else {
            super.onCreate(savedInstanceState);
            if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                        Constants.PERMISSION_ACCESS_FINE_LOCATION );
            }
            startService(new Intent(this, LocationService.class));
            setContentView(R.layout.activity_main);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getAllIssues();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    public void checkForLogin(View view) {
        DialogFragment dia = new LoginOrRegisterDialog();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dia.show(ft, "dialog");
    }

    public void currentLocation(View view) {
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                    Constants.PERMISSION_ACCESS_FINE_LOCATION );
        } else {
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                                mMap.animateCamera(cameraUpdate);
                            } else {
                                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.toast_location_null),Toast.LENGTH_LONG);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.toast_location_null),Toast.LENGTH_LONG);
                            e.printStackTrace();
                        }
                    });
        }
    }

    private void setPoints(List<Issue> issues) {
        if(issues == null) {
            return;
        }
        mMap.clear();
        for(Issue issue : issues) {
            LatLng location = new LatLng(issue.getmLat(),issue.getmLong());
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(""+ issue.getId()));
        }
    }

    private void getAllIssues() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final Gson gson = new Gson();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.SERVER_URL + "/report",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Issue> issues = gson.fromJson(response,new ArrayList<Issue>().getClass());
                        setPoints(issues);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
    }

    private void getUserReports(int userId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final Gson gson = new Gson();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.SERVER_URL + "/user/" + userId + "/report",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Issue> issues = gson.fromJson(response,new ArrayList<Issue>().getClass());
                        setPoints(issues);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
    }
}
