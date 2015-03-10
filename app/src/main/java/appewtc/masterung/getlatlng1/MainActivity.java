package appewtc.masterung.getlatlng1;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private TextView txtLat, txtLng;
    private LocationManager objLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindWidget();

        objLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Check Support Location
        checkSupportLocation();

    }   // onCreate

    public Location requestProvider(final String strProvider, String strError) {

        Location objLocation = null;
        if (objLocationManager.isProviderEnabled(strProvider)) {
            objLocationManager.requestLocationUpdates(strProvider, 1000, 10, objLocationListener);
            objLocation = objLocationManager.getLastKnownLocation(strProvider);
        } else {
            Log.d("Location", "Error requestProvider ==> " + strError);
        }

        return objLocation;
    }

    public final LocationListener objLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            txtLat.setText(String.format("%.7f", location.getLatitude()));
            txtLng.setText(String.format("%.7f", location.getLongitude()));

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();

        if (objLocationManager != null) {
            objLocationManager.removeUpdates(objLocationListener);
        }

    }   // onStop

    @Override
    protected void onResume() {
        super.onResume();
        setUp();
    }   // onResume

    public void setUp() {
        objLocationManager.removeUpdates(objLocationListener);
        String strLat = "";
        String strLng = "";

        Location networkLocation = requestProvider(LocationManager.NETWORK_PROVIDER, "Network Error");
        if (networkLocation != null) {
            strLat = String.format("%.7f", networkLocation.getLatitude());
            strLng = String.format("%.7f", networkLocation.getLongitude());
        }

        Location gpsLocation = requestProvider(LocationManager.GPS_PROVIDER, "GPS Error");
        if (gpsLocation != null) {
            strLat = String.format("%.7f", gpsLocation.getLatitude());
            strLng = String.format("%.7f", gpsLocation.getLongitude());
        }
        txtLat.setText(strLat);
        txtLng.setText(strLng);
    }   // setup

    @Override
    protected void onStart() {
        super.onStart();

        //Check GPS&Network
        boolean bolGPS = objLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean bolNetwork = objLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!bolGPS && !bolNetwork) {
            Intent objIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(objIntent);
            Log.d("Location", "Cannot status Connected");
        } else {
            Log.d("Location", "status Connected");
        }

    }   // onStart

    private void checkSupportLocation() {
        if (objLocationManager == null) {
            Toast.makeText(MainActivity.this, "Device not Support Location", Toast.LENGTH_SHORT).show();
            finish();
        }
    }   // checkSupportLocation

    private void bindWidget() {
        txtLat = (TextView) findViewById(R.id.textView);
        txtLng = (TextView) findViewById(R.id.textView2);
    }   // bindWidget


}   // Main Class





















