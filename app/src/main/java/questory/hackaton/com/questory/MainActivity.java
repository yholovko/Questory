package questory.hackaton.com.questory;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class MainActivity extends Activity implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;
    private Button startButton;
    private TextView scoresTextView;
    private LinearLayout llProfileLayout;
    private LinearLayout questTypeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        startButton = (Button) findViewById(R.id.startButton);
        scoresTextView = (TextView) findViewById(R.id.scoresTextView);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        questTypeLayout = (LinearLayout) findViewById(R.id.questTypeLayout);

        btnSignIn.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "Login successful...", Toast.LENGTH_LONG).show();
        getProfileInformation();
        updateUI(true);
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);

            llProfileLayout.setBackgroundColor(getResources().getColor(R.color.common_signin_btn_light_text_disabled));
            llProfileLayout.setVisibility(View.VISIBLE);

            startButton.setVisibility(View.VISIBLE);
            startButton.setOnClickListener(this);

            questTypeLayout.setVisibility(View.VISIBLE);
            scoresTextView.setVisibility(View.VISIBLE);

            generateMainActivityText();
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                //String personName = currentPerson.getDisplayName();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                int gender = currentPerson.getGender();
            } else {
                Toast.makeText(getApplicationContext(), "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                if (!mGoogleApiClient.isConnecting()) {
                    mSignInClicked = true;
                    resolveSignInError();
                }
                break;
            case R.id.startButton:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void generateMainActivityText(){
        scoresTextView.setText("Рахунок: "+ "" +"$");
        //send request with type of quest
        //waiting
    }
}