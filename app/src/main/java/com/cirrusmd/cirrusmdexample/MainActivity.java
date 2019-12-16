package com.cirrusmd.cirrusmdexample;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cirrusmd.androidsdk.CirrusEvents;
import com.cirrusmd.androidsdk.CirrusListener;
import com.cirrusmd.androidsdk.CirrusMD;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements CirrusListener {

    private TextView homeText;

    private FrameLayout frame;

    private Fragment cirrusFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        homeTapped();
                        return true;
                    case R.id.navigation_cirrus:
                        cirrusMDTapped();
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeText = new TextView(this);
        homeText.setText(R.string.home_text);

        frame = findViewById(R.id.frameLayout);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //This start() call would probably be better placed in the app's implementation of Application.
        //For the example we placed it here for ease of access/readability.
        CirrusMD.INSTANCE.start(this, getSecret());
        CirrusMD.INSTANCE.setListener(this);

        homeTapped();
    }

    private void homeTapped() {
        removeMessages();
        frame.removeAllViews();
        frame.addView(homeText);
    }

    private void cirrusMDTapped() {
        //This retrofit/fetcher/JWT process is unique for each implementation, based on your organization's SSO environment and your app's architecture.
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .baseUrl("https://staging.cirrusmd.com/sdk/v1/")
                .build();

        TokenFetcher fetcher = retrofit.create(TokenFetcher.class);
        TokenRequest request = new TokenRequest();
        request.patientId = "886";
        request.sdkId = "21dca847-a7c8-4150-99eb-a255231a2f00";

        fetcher.getSessionJwt(request).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                //Once you have a token and secret, start the instance of the SDK.
                //There is a pre-flight process in the SDK to fetch the user's profile once it has started and report back to the listener.
                //Because of this pre-flight, we are not displaying the fragment until we receive a CirrusEvents.SUCCESS event in the listener.
                CirrusMD.INSTANCE.setSessionToken(response.body().token);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
    }

    private String getSecret() {
        //This is the secret for the sandbox environment.
        return "eyJzaGFyZWRfc2VjcmV0IjoiZWFlZGZkYWMtZjBkYS00NGYxLTkxNDgtYTE3ZWQ4NDcxY2Q3IiwieDUwOV9jZXJ0X2RlciI6Ik1JSUQ4RENDQXRpZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0ZBREJjTVFzd0NRWURWUVFHRXdKVlV6RVdNQlFHQTFVRUNnd05RMmx5Y25WelRVUWdTVzVqTGpFVU1CSUdBMVVFQ3d3TFJXNW5hVzVsWlhKcGJtY3hEREFLQmdOVkJBTU1BMU5FU3pFUk1BOEdBMVVFQ0F3SVEyOXNiM0poWkc4d0lCY05OekF3TVRBeE1EQXdNREF3V2hnUE5EQXdNVEF4TURFd01EQXdNREJhTUZ3eEN6QUpCZ05WQkFZVEFsVlRNUll3RkFZRFZRUUtEQTFEYVhKeWRYTk5SQ0JKYm1NdU1SUXdFZ1lEVlFRTERBdEZibWRwYm1WbGNtbHVaekVNTUFvR0ExVUVBd3dEVTBSTE1SRXdEd1lEVlFRSURBaERiMnh2Y21Ga2J6Q0NBU0l3RFFZSktvWklodmNOQVFFQkJRQURnZ0VQQURDQ0FRb0NnZ0VCQUt2NFNDT284UURvV3dnWDJHM3NHNTZXcTlBQ3VQMVlMbkQwNjVNdE5oZXZ5SGFiMWhTQTRlOStSR1R6ZUU1cDBFdUh0a3NYK3ZCek9iUFdUOVQwN0dBaVBSVkdWaVFSellpWEFOQTIvMVBBdk9Pa1JCVGUrZlJFc2FkVHJEdWx1SHhhc3B6dk9NR21KU0xIckJBUTVVTUdjck9leU9PYktnb2JzK0dUNFg4V3ZsMG1wWEN4aEZtTVozdmx3aDZJaTEwRlFqN2J0NmI2b2YremRxSXRaK1hkTWtZalB2NFc2dGp4Ym0zS005djJxaTYrcVJraWxlZW5NbTRQb2FEL09YUmZCdUZMMFJrYi9uTDgxV0M4MkFyREUzNE0ycmZrUXNMeGFvLzROdHBtVlExemtxSXNYU3g4VEZaMWpDUVJXcFIxTDAzK09BYkp3c3gwVEgwUE9Fc0NBd0VBQWFPQnVqQ0J0ekFQQmdOVkhSTUJBZjhFQlRBREFRSC9NQjBHQTFVZERnUVdCQlI3RHdOdE5tNzVvVnZiSlBtaXg0dHY4MXlmUkRDQmhBWURWUjBqQkgwd2U0QVVldzhEYlRadSthRmIyeVQ1b3NlTGIvTmNuMFNoWUtSZU1Gd3hDekFKQmdOVkJBWVRBbFZUTVJZd0ZBWURWUVFLREExRGFYSnlkWE5OUkNCSmJtTXVNUlF3RWdZRFZRUUxEQXRGYm1kcGJtVmxjbWx1WnpFTU1Bb0dBMVVFQXd3RFUwUkxNUkV3RHdZRFZRUUlEQWhEYjJ4dmNtRmtiNElCQURBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQVFFQUN6V0ZFY1N5WUQxNWQ1Qm0vdEhWZTRzQU44RWNNVGFQaVRXWkdYN0dXNGZUTFphWE5XRCtrUnN6VDlOU0FCNDVGNHF6Q3pCNE1hbmdZSHI5VlR0c1kzeGZpVDl3ZDFpMDJtNUorTjZLeFVrZ3Awdzh3YW90bVFmLzR4WnRta21Qa1Z3Rlo1NkVXTFVKcDFLLzIvQ2hOMDRlRGNHL3pvWXI1TDVaRkRDQW5iM0s5TUdoOVM0QjdBY2lKV0k1V0lmZncreGJQN3c4ckc3Y2sveDliWXFZbXZmelBrdXh0elRSK1Z1bHV5aDJCMzI2QU5na1k0dDNRNWNDNk1JQm45VFdFTEt3L1p4a2E3SUhKdGZ0ekxxZlpMckJBd1VFcm1OK29IbmFBaGxZb1VjdG9yNmk5alFmQTNnSTVTTW9hcVpUWEwrK0I4aW9nVHJZOUgwNWFEYzZ0dz09IiwicHVibGljX2tleV9wZW0iOiItLS0tLUJFR0lOIFBVQkxJQyBLRVktLS0tLVxuTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUFxL2hJSTZqeEFPaGJDQmZZYmV3YlxubnBhcjBBSzQvVmd1Y1BUcmt5MDJGNi9JZHB2V0ZJRGg3MzVFWlBONFRtblFTNGUyU3hmNjhITTVzOVpQMVBUc1xuWUNJOUZVWldKQkhOaUpjQTBEYi9VOEM4NDZSRUZONzU5RVN4cDFPc082VzRmRnF5bk84NHdhWWxJc2VzRUJEbFxuUXdaeXM1N0k0NXNxQ2h1ejRaUGhmeGErWFNhbGNMR0VXWXhuZStYQ0hvaUxYUVZDUHR1M3B2cWgvN04yb2kxblxuNWQweVJpTSsvaGJxMlBGdWJjb3oyL2FxTHI2cEdTS1Y1NmN5YmcraG9QODVkRjhHNFV2UkdSditjdnpWWUx6WVxuQ3NNVGZnemF0K1JDd3ZGcWovZzIybVpWRFhPU29peGRMSHhNVm5XTUpCRmFsSFV2VGY0NEJzbkN6SFJNZlE4NFxuU3dJREFRQUJcbi0tLS0tRU5EIFBVQkxJQyBLRVktLS0tLVxuIn0=";
    }

    private void displayMessages() {
        if (cirrusFragment == null) {
            //Get the EventStreamFragment from the CirrusMD instance and display it
            cirrusFragment = CirrusMD.INSTANCE.getFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frameLayout, cirrusFragment, "messages")
                .commit();
    }

    private void removeMessages() {
        if (cirrusFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(cirrusFragment).commit();
        }
    }

    @Override
    public void onEvent(CirrusEvents cirrusEvent) {
        //This is where the CirrusMD SDK will report status events.
        switch (cirrusEvent) {
            case SUCCESS:
                Timber.d("CirrusMD SDK successful pre-flight");
                displayMessages();
                break;

            case INVALID_JWT:
                Timber.d("CirrusMD SDK invalid JWT supplied");
                break;

            case INVALID_SECRET:
                Timber.d("CirrusMD SDK invalid secret supplied");
                break;

            case LOGGED_OUT:
                Timber.d("CirrusMD SDK user was logged out.");
                break;

            case UNKNOWN_ERROR:
                //This error would include cases like network errors
                Timber.d("CirrusMD SDK generic error");
                break;
        }
    }

    @Override
    public View viewForError(CirrusEvents cirrusEvents) {
        //If you would like to display branded error/logout messages, this is where the CirrusMD SDK will look.
        //Returning null will result in the default views being displayed.
        return null;
    }
}
