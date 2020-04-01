package com.cirrusmd.cirrusmdexample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cirrusmd.androidsdk.CirrusEvents
import com.cirrusmd.androidsdk.CirrusListener
import com.cirrusmd.androidsdk.CirrusMD
import com.cirrusmd.androidsdk.CirrusMD.listener
import com.cirrusmd.androidsdk.CirrusMD.setSessionToken
import com.cirrusmd.androidsdk.CirrusMD.start
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class MainActivity : AppCompatActivity(), CirrusListener, View.OnClickListener {

    private var homeText: TextView? = null
    private var button: Button? = null
    private var frame: FrameLayout? = null
    private val cirrusFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeText = findViewById(R.id.textView)
        homeText?.text = getString(R.string.initializing)

        button = findViewById(R.id.button)
        button?.isEnabled = false
        button?.setOnClickListener(this)

        frame = findViewById(R.id.frameLayout)

        //This start() call would probably be better placed in the app's implementation of Application.
        //For the example we placed it here for ease of access/readability.
        start(this, SECRET)
        listener = this
        homeTapped()
        fetchTokenForCirrusMDSDK()
    }

    override fun onClick(v: View) {
        displayMessages()
    }

    private fun fetchTokenForCirrusMDSDK() {
        //This retrofit/fetcher/JWT process is unique for each implementation, based on your organization's SSO environment and your app's architecture.
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .baseUrl(BASE_URL)
                .build()
        val fetcher = retrofit.create<TokenFetcher>(TokenFetcher::class.java)
        val request = TokenRequestKt(SDK_ID, PATIENT_ID)
        fetcher.getSessionJwt(request)?.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                //Once you have a token and secret, start the instance of the SDK.
                //There is a pre-flight process in the SDK to fetch the user's profile once it has started and report back to the listener.
                //Because of this pre-flight, we are not displaying the fragment until we receive a CirrusEvents.SUCCESS event in the listener.
                response.body()?.token?.let { token ->
                    setSessionToken(token)
                } ?: Timber.e("No token in response body")
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun homeTapped() {
        removeMessages()
    }

    private fun displayMessages() {
        if (CirrusMD.intent != null) {
            startActivity(CirrusMD.intent)
        }
    }

    private fun removeMessages() {
        if (cirrusFragment != null) {
            supportFragmentManager.beginTransaction().remove(cirrusFragment).commit()
        }
        homeText?.visibility = View.VISIBLE
        button?.visibility = View.VISIBLE
    }

    private fun onEventError(error: String) {
        Timber.e(error)
        homeText?.text = getString(R.string.error)
        button?.isEnabled = false
    }

    /**
     *   This is where the CirrusMD SDK will report status events.
     */
    override fun onEvent(cirrusEvent: CirrusEvents) {
        when (cirrusEvent) {
            CirrusEvents.SUCCESS -> {
                // Note: onEvent can be called multiple times during the execution of the SDK.
                // It is best to just show/hide or enable/disable the button that presents the CirrusMDSDK fragment.
                // If you are going to display the fragment based on this result be sure to check
                // that it is not already shown (this will cause the "Fragment already added" crash).
                // You will see this check in the displayMessages function.
                Timber.d("CirrusMD SDK successful pre-flight")
                homeText?.text = getString(R.string.ready)
                button?.isEnabled = true
            }
            CirrusEvents.LOGGED_OUT -> onEventError("CirrusMD SDK user was logged out.")
            CirrusEvents.INVALID_JWT -> onEventError("CirrusMD SDK invalid JWT supplied")
            CirrusEvents.INVALID_SECRET -> onEventError("CirrusMD SDK invalid secret supplied")
            CirrusEvents.CONNECTION_ERROR -> onEventError("CirrusMD SDK connection error")
            CirrusEvents.AUTHENTICATION_ERROR -> onEventError("CirrusMD SDK auth error")
            CirrusEvents.UNKNOWN_ERROR -> onEventError("CirrusMD SDK generic error") //This error would include cases like network errors
        }
    }

    override fun viewForError(error: CirrusEvents): View? {
        //If you would like to display branded error/logout messages, this is where the CirrusMD SDK will look.
        //Returning null will result in the default views being displayed.
        return null
    }

    companion object {
        private const val BASE_URL = "https://staging.cirrusmd.com/sdk/v1/"
        private const val SDK_ID = "21dca847-a7c8-4150-99eb-a255231a2f00"
        private const val PATIENT_ID = "886"

        //This is the secret for the sandbox environment.
        private const val SECRET = "eyJzaGFyZWRfc2VjcmV0IjoiZWFlZGZkYWMtZjBkYS00NGYxLTkxNDgtYTE3ZWQ4NDcxY2Q3IiwieDUwOV9jZXJ0X2RlciI6Ik1JSUQ4RENDQXRpZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0ZBREJjTVFzd0NRWURWUVFHRXdKVlV6RVdNQlFHQTFVRUNnd05RMmx5Y25WelRVUWdTVzVqTGpFVU1CSUdBMVVFQ3d3TFJXNW5hVzVsWlhKcGJtY3hEREFLQmdOVkJBTU1BMU5FU3pFUk1BOEdBMVVFQ0F3SVEyOXNiM0poWkc4d0lCY05OekF3TVRBeE1EQXdNREF3V2hnUE5EQXdNVEF4TURFd01EQXdNREJhTUZ3eEN6QUpCZ05WQkFZVEFsVlRNUll3RkFZRFZRUUtEQTFEYVhKeWRYTk5SQ0JKYm1NdU1SUXdFZ1lEVlFRTERBdEZibWRwYm1WbGNtbHVaekVNTUFvR0ExVUVBd3dEVTBSTE1SRXdEd1lEVlFRSURBaERiMnh2Y21Ga2J6Q0NBU0l3RFFZSktvWklodmNOQVFFQkJRQURnZ0VQQURDQ0FRb0NnZ0VCQUt2NFNDT284UURvV3dnWDJHM3NHNTZXcTlBQ3VQMVlMbkQwNjVNdE5oZXZ5SGFiMWhTQTRlOStSR1R6ZUU1cDBFdUh0a3NYK3ZCek9iUFdUOVQwN0dBaVBSVkdWaVFSellpWEFOQTIvMVBBdk9Pa1JCVGUrZlJFc2FkVHJEdWx1SHhhc3B6dk9NR21KU0xIckJBUTVVTUdjck9leU9PYktnb2JzK0dUNFg4V3ZsMG1wWEN4aEZtTVozdmx3aDZJaTEwRlFqN2J0NmI2b2YremRxSXRaK1hkTWtZalB2NFc2dGp4Ym0zS005djJxaTYrcVJraWxlZW5NbTRQb2FEL09YUmZCdUZMMFJrYi9uTDgxV0M4MkFyREUzNE0ycmZrUXNMeGFvLzROdHBtVlExemtxSXNYU3g4VEZaMWpDUVJXcFIxTDAzK09BYkp3c3gwVEgwUE9Fc0NBd0VBQWFPQnVqQ0J0ekFQQmdOVkhSTUJBZjhFQlRBREFRSC9NQjBHQTFVZERnUVdCQlI3RHdOdE5tNzVvVnZiSlBtaXg0dHY4MXlmUkRDQmhBWURWUjBqQkgwd2U0QVVldzhEYlRadSthRmIyeVQ1b3NlTGIvTmNuMFNoWUtSZU1Gd3hDekFKQmdOVkJBWVRBbFZUTVJZd0ZBWURWUVFLREExRGFYSnlkWE5OUkNCSmJtTXVNUlF3RWdZRFZRUUxEQXRGYm1kcGJtVmxjbWx1WnpFTU1Bb0dBMVVFQXd3RFUwUkxNUkV3RHdZRFZRUUlEQWhEYjJ4dmNtRmtiNElCQURBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQVFFQUN6V0ZFY1N5WUQxNWQ1Qm0vdEhWZTRzQU44RWNNVGFQaVRXWkdYN0dXNGZUTFphWE5XRCtrUnN6VDlOU0FCNDVGNHF6Q3pCNE1hbmdZSHI5VlR0c1kzeGZpVDl3ZDFpMDJtNUorTjZLeFVrZ3Awdzh3YW90bVFmLzR4WnRta21Qa1Z3Rlo1NkVXTFVKcDFLLzIvQ2hOMDRlRGNHL3pvWXI1TDVaRkRDQW5iM0s5TUdoOVM0QjdBY2lKV0k1V0lmZncreGJQN3c4ckc3Y2sveDliWXFZbXZmelBrdXh0elRSK1Z1bHV5aDJCMzI2QU5na1k0dDNRNWNDNk1JQm45VFdFTEt3L1p4a2E3SUhKdGZ0ekxxZlpMckJBd1VFcm1OK29IbmFBaGxZb1VjdG9yNmk5alFmQTNnSTVTTW9hcVpUWEwrK0I4aW9nVHJZOUgwNWFEYzZ0dz09IiwicHVibGljX2tleV9wZW0iOiItLS0tLUJFR0lOIFBVQkxJQyBLRVktLS0tLVxuTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUFxL2hJSTZqeEFPaGJDQmZZYmV3YlxubnBhcjBBSzQvVmd1Y1BUcmt5MDJGNi9JZHB2V0ZJRGg3MzVFWlBONFRtblFTNGUyU3hmNjhITTVzOVpQMVBUc1xuWUNJOUZVWldKQkhOaUpjQTBEYi9VOEM4NDZSRUZONzU5RVN4cDFPc082VzRmRnF5bk84NHdhWWxJc2VzRUJEbFxuUXdaeXM1N0k0NXNxQ2h1ejRaUGhmeGErWFNhbGNMR0VXWXhuZStYQ0hvaUxYUVZDUHR1M3B2cWgvN04yb2kxblxuNWQweVJpTSsvaGJxMlBGdWJjb3oyL2FxTHI2cEdTS1Y1NmN5YmcraG9QODVkRjhHNFV2UkdSditjdnpWWUx6WVxuQ3NNVGZnemF0K1JDd3ZGcWovZzIybVpWRFhPU29peGRMSHhNVm5XTUpCRmFsSFV2VGY0NEJzbkN6SFJNZlE4NFxuU3dJREFRQUJcbi0tLS0tRU5EIFBVQkxJQyBLRVktLS0tLVxuIn0="
    }
}
