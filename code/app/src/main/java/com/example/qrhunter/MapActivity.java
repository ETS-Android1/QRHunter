package com.example.qrhunter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
// ressources
// https://developers.google.com/maps/documentation/places/android-sdk/autocomplete
// https://stackoverflow.com/questions/31571050/how-to-add-google-map-markers-outside-of-onmapready-in-android
// https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime
// https://stackoverflow.com/questions/46630507/how-to-run-a-geo-nearby-query-with-firestore
// https://stackoverflow.com/questions/46616352/how-can-i-update-the-markers-when-moving-the-map
/**
 * This class is responsible for displaying the map of qr codes to users
 */
public class MapActivity extends BaseNavigatableActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final double oneMileLat = 0.0144927536231884;
    public static final double oneMileLon = 0.0181818181818182;
    private HashMap<Double, HashMap<Double, HashMap<String, MarkerOptions>>> markers = new HashMap<>();
    private HashMap<MarkerOptions, Double> count = new HashMap<>();
    private GoogleMap map;
    LinearLayout infoLayout;
    FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("qrcodes");
        infoLayout = findViewById(R.id.infolayout);
        infoLayout.setVisibility(View.INVISIBLE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        // Initialize the AutocompleteSupportFragment.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        }
        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // listen to place selection
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            /**
             * This sets the new centre of the map to the selected place
             *
             * @param place The place that gets selected
             */
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                LatLng newCenter = place.getLatLng();
                if (newCenter == null) {
                    return;
                }
                LatLng myLocation = newCenter;
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                        15));
            }

            /**
             * This is called on error of place selection
             *
             * @param status The status
             */
            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("GOGGLEPLACESERROR", "An error occurred: " + status);
            }
        });
        // query database for codes
    }
    /**
     * This is called when the google map widget is ready for display
     * @param myMap
     * The map to be displayed
     */
    @Override
    public void onMapReady(GoogleMap myMap) {
        map = myMap;
        map.setOnMarkerClickListener(this::onMarkerClick);
        map.setOnCameraIdleListener(this::onCameraIdle);
        setUpMap();
    }

    /**
     * This is called to get permissions and centre the map
     */
    public void setUpMap() {

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        map.setMyLocationEnabled(true);
        // zoom in on last user location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng myLocation = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                                    15));
                        }
                    }
                });
    }
    /**
     * This is called by the base activity to get the layout
     * @return returns the layout for this activity
     */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_map;
    }

    /**
     * This is called by the base activity to get the selected item on create
     * @return returns item id corresponding to the activity
     */
    @Override
    protected int getSelectedItemId() {
        return R.id.search;
    }

    /**
     * This is called when the permissions for the application are changed
     * @param requestCode
     * The code that corresponds to a permission change
     * @param permissions
     * the changed permissions
     * @param grantResults
     * whether anything was granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // premission granted
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        // update map location
                        map.setMyLocationEnabled(true);
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                       if (location != null) {
                                           LatLng myLocation = new LatLng(location.getLatitude(),
                                                    location.getLongitude());
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                                                     15));
                                       }
                                    }
                                });
                    }

                }
                return;
            }

        }
    }
    /**
     * This is called when a marker is clicked
     * @param marker
     * the marker clicked
     */
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        infoLayout.setVisibility(View.VISIBLE);
        TextView score = (TextView) findViewById(R.id.score);
        TextView address = (TextView) findViewById(R.id.address);
        score.setText(marker.getSnippet());
        score.setText(marker.getSnippet());
        address.setText(marker.getTitle());
        return false;
    }


    @Override
    public void onCameraIdle() {
        firestoreQueryNearbyLocations();
        // Now fire a new search with the new latitude and longitude
        // searcher.setQuery(new Query().setAroundLatLng(new AbstractQuery.LatLng(centerLat, centerLng)).setAroundRadius(5000))
    }

    public  void processLocation(HashMap hashmaplocation, DocumentSnapshot hashMapSnapshot) {
        HashMap hashMapCode = (HashMap) hashMapSnapshot.getData();
        GeoPoint geopoint = (GeoPoint) hashmaplocation.get("location");
        try {
            if (markers.get(geopoint.getLatitude()) != null && markers.get(geopoint).get(geopoint.getLongitude()) != null
                    && markers.get(geopoint).get(geopoint.getLongitude()).get(hashMapSnapshot.getId()) != null) {
                MarkerOptions options = markers.get(geopoint.getLatitude()).get(geopoint.getLongitude()).get(hashMapSnapshot.getId());

                options.snippet("Count: " + (count.get(options) + 1) + " Score: " + hashMapCode.get("score"));
                count.put(options, count.get(options) + 1);
            }
            else{
                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(geopoint.getLatitude(), geopoint.getLongitude()))
                        .title((String) hashmaplocation.get("address")).snippet("Count: 1" + " Score: " + hashMapCode.get("score"));
                HashMap idsHashed = new HashMap<String, MarkerOptions>() {{
                    put((String) hashMapSnapshot.getId(), options);
                }};
                markers.put(geopoint.getLatitude(),new HashMap<Double, HashMap<String, MarkerOptions>>() {{
                    put(geopoint.getLongitude(), idsHashed);
                }});
                count.put(options, Double.valueOf(1));
                map.addMarker(options);
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e("MAPERROR", msg);
        }
    }
    public void firestoreQueryNearbyLocations () {
        LatLng center = map.getCameraPosition().target;
        double centerLat = (double)center.latitude;
        double centerLng = (double)center.longitude;
        double distance = 100/ map.getCameraPosition().zoom;

        double lowerLat = centerLat - (oneMileLat * distance);
        double lowerLon = centerLng - (oneMileLon * distance);

        double greaterLat = centerLat + (oneMileLat * distance);
        double greaterLon = centerLng + (oneMileLon * distance);
        if(lowerLat < -90) {
            lowerLat = -90;
        }
        if(lowerLon < -180) {
            lowerLat = -180;
        }
        if(greaterLat > 90) {
            lowerLat = 90;
        }
        if(greaterLon > 180) {
            greaterLon = 180;
        }
        GeoPoint lesserGeopoint = new GeoPoint(lowerLat, lowerLon);
        GeoPoint greaterGeopoint =  new GeoPoint(greaterLat, greaterLon);

        CollectionReference docRef =  db.collection("location");

        docRef.whereGreaterThan("location", lesserGeopoint).whereLessThan("location", greaterGeopoint)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                for (QueryDocumentSnapshot document : task.getResult()) {
                    HashMap hashmaplocation = (HashMap) document.getData();
                    DocumentReference referenceToScan = (DocumentReference) hashmaplocation.get("scan");
                    if(referenceToScan == null) { continue; }
                    referenceToScan.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            HashMap hashmapScan = (HashMap) task.getResult().getData();
                            DocumentReference referenceToCode = (DocumentReference) hashmapScan.get("qrcode");
                            referenceToCode.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        return;
                                    }
                                    HashMap hashMapCode = (HashMap) task.getResult().getData();
                                    processLocation(hashmaplocation, task.getResult());
                                }
                            });
                        }

                    });

                }
            }

        });

    }
}