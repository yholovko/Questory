package questory.hackaton.com.questory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements LocationListener, View.OnClickListener {

    ImageButton btnHelp;
    Button btnSendAnswer;
    EditText etAnswer;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager lm;
    private LatLng ImHere;
    private LinearLayout mapsLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnHelp = (ImageButton) findViewById(R.id.btn_help);
        btnHelp.setOnClickListener(this);

        etAnswer = (EditText) findViewById(R.id.etAnswer);
        mapsLinearLayout = (LinearLayout) findViewById(R.id.mapsLinearLayout);
        mapsLinearLayout.setBackgroundColor(getResources().getColor(R.color.common_signin_btn_light_text_disabled));

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300, 10f, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        ImHere = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(ImHere)
                .title(this.getResources().getString(R.string.user_location)));
        // Move the camera instantly to ImHere with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ImHere, 150));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_help:
                askAboutPenalty();
                break;
        }
    }

    private void askAboutPenalty(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getResources().getString(R.string.dlg_penalty_title));

        final String[] helps = {this.getResources().getString(R.string.dlg_penalty_help),
                                this.getResources().getString(R.string.dlg_penalty_answer),};

        builder.setItems(helps, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch(item){
                    case 0:
                        showHelp();
                        break;
                    case 1:
                        showAnswer();
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showHelp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getResources().getString(R.string.dlg_help_title))
                .setMessage(this.getResources().getString(R.string.dlg_help_message))
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showAnswer(){
        etAnswer.setText("Ответ!");
    }
}
