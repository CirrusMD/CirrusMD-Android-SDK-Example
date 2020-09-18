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
import com.cirrusmd.androidsdk.CirrusMD.enableDebugLogging
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

        // This setup sequence would probably be better placed in the app's implementation of Application.
        // For the example we call it here for ease of access/readability.
        setupCirrus()

        homeTapped()
        fetchTokenForCirrusMDSDK()
    }

    override fun onClick(v: View) {
        displayMessages()
    }

    private fun setupCirrus() {
        // For debug logging to be enabled correctly, set CirrusMD.enableDebugLogging = true,
        // before calling CirrusMd.start()
        enableDebugLogging = true
        start(this, SECRET)
        listener = this
    }

    private fun fetchTokenForCirrusMDSDK() {
        //This retrofit/fetcher/JWT process is unique for each implementation, based on your organization's SSO environment and your app's architecture.
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .baseUrl(BASE_URL)
                .build()
        val fetcher = retrofit.create<TokenFetcher>(TokenFetcher::class.java)
        val request = TokenRequest(SDK_ID, PATIENT_ID)
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
                // It is best to just show/hide or enable/disable the button that presents the CirrusMDSDK Activity.
                // If you are going to display the Activity based on this result be sure to check
                // that it is not already shown.
                Timber.d("CirrusMD SDK successful pre-flight")
                homeText?.text = getString(R.string.ready)
                button?.isEnabled = true
            }
            CirrusEvents.LOGGED_OUT -> onEventError("CirrusMD SDK user was logged out.")
            CirrusEvents.INVALID_JWT -> onEventError("CirrusMD SDK invalid JWT supplied")
            CirrusEvents.INVALID_SECRET -> onEventError("CirrusMD SDK invalid secret supplied")
            CirrusEvents.MISSING_JWT -> onEventError("CirrusMD SDK missing jwt")
            CirrusEvents.MISSING_SECRET -> onEventError("CirrusMD SDK missing secret")
            CirrusEvents.CONNECTION_ERROR -> onEventError("CirrusMD SDK connection error")
            CirrusEvents.AUTHENTICATION_ERROR -> onEventError("CirrusMD SDK auth error")
            CirrusEvents.USER_INTERACTION -> Timber.d("CirrusMD SDK user interaction")
            CirrusEvents.UNKNOWN_ERROR -> onEventError("CirrusMD SDK generic error") //This error would include cases like network errors
        }
    }

    override fun viewForError(error: CirrusEvents): View? {
        //If you would like to display branded error/logout messages, this is where the CirrusMD SDK will look.
        //Returning null will result in the default views being displayed.
        return null
    }

    companion object {
        private const val BASE_URL = "https://cmd-demo1-app.cirrusmd.com/sdk/v2/"
        private const val SDK_ID = "d2f0aa92-3da9-450c-9ba2-854e36a2e277"
        private const val PATIENT_ID = "63"

        //This is the secret for the sandbox environment.
        private const val SECRET = "eyJzaGFyZWRfc2VjcmV0IjoiOTg0MDMyNDYtMGJmMS00ZjNjLWFhMTktMDg1ZWFiMGMxMWE2IiwieDUwOV9jZXJ0X2RlciI6Ik1JSUQ4RENDQXRpZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0ZBREJjTVFzd0NRWURWUVFHRXdKVlV6RVdNQlFHQTFVRUNnd05RMmx5Y25WelRVUWdTVzVqTGpFVU1CSUdBMVVFQ3d3TFJXNW5hVzVsWlhKcGJtY3hEREFLQmdOVkJBTU1BMU5FU3pFUk1BOEdBMVVFQ0F3SVEyOXNiM0poWkc4d0lCY05OekF3TVRBeE1EQXdNREF3V2hnUE5EQXdNVEF4TURFd01EQXdNREJhTUZ3eEN6QUpCZ05WQkFZVEFsVlRNUll3RkFZRFZRUUtEQTFEYVhKeWRYTk5SQ0JKYm1NdU1SUXdFZ1lEVlFRTERBdEZibWRwYm1WbGNtbHVaekVNTUFvR0ExVUVBd3dEVTBSTE1SRXdEd1lEVlFRSURBaERiMnh2Y21Ga2J6Q0NBU0l3RFFZSktvWklodmNOQVFFQkJRQURnZ0VQQURDQ0FRb0NnZ0VCQU9QVWphcHBBRTg0NnZLZE9jN1VJR0xyK3RPMzBRMy9qVTdIU0FZbFNoZCtCV290bGw4RkkvVkZoc0RXcTBFRk9VUGRvMFQvSmVOcnRuYmVYYXllUlYzSFlRNU9admxkUlZhcUNPYmRmRmFhZWpDM2JVVW5zRFdWalJqTlExZTNicVFkYk9ELzZNZTd3cTI0NFExVTdkY2ZWL3lIVlgzMEtBOUxZZSthcmhWbld4TWFrWW1LbWZ1S1Vrby8vQm5tRDZiQkpJR3hGRzJTOXEwR3ZVODJ4RHViQitDbkpzczJ3QlFpWGE3VW8rM0E3NU5VNlgrcXUySlRQYnBMSkgyZ01oYUZ3MGwzOTN6T0ZuWldGWUx3dlhrTmd0bkYzVFIySk52ZWpseFBYQ0NKN0hrZVpuZVJUdXF2RFN0cGpWK2t2WDg2cUZIZWdMSE5ZTU5rK1pONm1CTUNBd0VBQWFPQnVqQ0J0ekFQQmdOVkhSTUJBZjhFQlRBREFRSC9NQjBHQTFVZERnUVdCQlJpTVBnbTF6SCtLamZpMlpGc3dqaEV3akZncHpDQmhBWURWUjBqQkgwd2U0QVVZakQ0SnRjeC9pbzM0dG1SYk1JNFJNSXhZS2VoWUtSZU1Gd3hDekFKQmdOVkJBWVRBbFZUTVJZd0ZBWURWUVFLREExRGFYSnlkWE5OUkNCSmJtTXVNUlF3RWdZRFZRUUxEQXRGYm1kcGJtVmxjbWx1WnpFTU1Bb0dBMVVFQXd3RFUwUkxNUkV3RHdZRFZRUUlEQWhEYjJ4dmNtRmtiNElCQURBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQVFFQXBYc2owNFBpNmlveXBIMys3cHhIYVM2cmVCVGJva1REUDVlcHM2QTVJamYzSWdsVG93U3IreldYNHJTTldRVVFXZUZqK1Nwa0hmZjVrVjV1bk5GdDk3OE16aXo0SFF0QTdxVzFhVG9mNks5dy93cHh5aGkrS250VzFOYzBjNHA1d1VnWGwvM0Z6YmJIRzJzekpRZEdPaUhTR3YwS3JtU1BJMjhkQ29HOWJUZXIzcG5BTnQ3aHZ6cHJWa3NmbXlkcTB4c1dEek8rSENQSjlIa0tPYXlLUnZZRVMxQXo1TGU2dWtmazRhbWlCc2FEekVmRWE1NzlnMEZXNVRMMFhWZ2xobVluQmxpVTU4d3JjT1hPTXJVQUFFZWVDNzJRQXBIcVBHUkJWNWJnSWV5eWVuUU1PQVJvckgzV2gwY0ZaNjc2WXpOUHUwTlpoTnd5bTJZUWZrNWF3Zz09IiwicHVibGljX2tleV9wZW0iOiItLS0tLUJFR0lOIFBVQkxJQyBLRVktLS0tLVxuTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUE0OVNOcW1rQVR6anE4cDA1enRRZ1xuWXV2NjA3ZlJEZitOVHNkSUJpVktGMzRGYWkyV1h3VWo5VVdHd05hclFRVTVROTJqUlA4bDQydTJkdDVkcko1RlxuWGNkaERrNW0rVjFGVnFvSTV0MThWcHA2TUxkdFJTZXdOWldOR00xRFY3ZHVwQjFzNFAvb3g3dkNyYmpoRFZUdFxuMXg5WC9JZFZmZlFvRDB0aDc1cXVGV2RiRXhxUmlZcVorNHBTU2ovOEdlWVBwc0VrZ2JFVWJaTDJyUWE5VHpiRVxuTzVzSDRLY215emJBRkNKZHJ0U2o3Y0R2azFUcGY2cTdZbE05dWtza2ZhQXlGb1hEU1hmM2ZNNFdkbFlWZ3ZDOVxuZVEyQzJjWGROSFlrMjk2T1hFOWNJSW5zZVI1bWQ1Rk82cThOSzJtTlg2UzlmenFvVWQ2QXNjMWd3MlQ1azNxWVxuRXdJREFRQUJcbi0tLS0tRU5EIFBVQkxJQyBLRVktLS0tLVxuIn0="
    }
}
