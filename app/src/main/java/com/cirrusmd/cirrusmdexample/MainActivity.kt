package com.cirrusmd.cirrusmdexample

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cirrusmd.androidsdk.*
import com.cirrusmd.androidsdk.CirrusMD.allowDarkMode
import com.cirrusmd.androidsdk.CirrusMD.cirrusDataEventListener
import com.cirrusmd.androidsdk.CirrusMD.credentialIdListener
import com.cirrusmd.androidsdk.CirrusMD.enableDebugLogging
import com.cirrusmd.androidsdk.CirrusMD.enableSettings
import com.cirrusmd.androidsdk.CirrusMD.setSessionToken
import com.cirrusmd.androidsdk.CirrusMD.start
import com.cirrusmd.androidsdk.pinnedbanner.model.CirrusMDActionModal
import com.cirrusmd.androidsdk.pinnedbanner.model.CirrusMDContactIcon
import com.cirrusmd.androidsdk.pinnedbanner.model.CirrusMDContactOption
import com.cirrusmd.androidsdk.pinnedbanner.model.CirrusMDPinnedBanner
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class MainActivity : AppCompatActivity(), CirrusDataEventListener {

    private var homeText: TextView? = null
    private var displayCirrusSDKButton: Button? = null
    private var frame: FrameLayout? = null
    private val cirrusFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeText = findViewById(R.id.textView)
        homeText?.text = getString(R.string.initializing)

        displayCirrusSDKButton = findViewById(R.id.display_cirrussdk_button)
        displayCirrusSDKButton?.isEnabled = false
        displayCirrusSDKButton?.setOnClickListener {
            displayMessages()
        }

        frame = findViewById(R.id.frameLayout)

        // This setup sequence would probably be better placed in the app's implementation of Application.
        // For the example we call it here for ease of access/readability.
        setupCirrus()

        homeTapped()
        fetchTokenForCirrusMDSDK()
    }

    private fun setupCirrus() {
        // FOR DEBUGGING ONLY
        enableDebugLogging = true

        // OPTIONAL: Settings view allows the user to view and edit their profile, medical history, dependents, permissions, and Terms of Use / Privacy Policy.
        enableSettings = true
    
        // OPTIONAL: Allows the CirrusMD SDK to follow the system wide setting for enabling/disabling Dark Theme. Otherwise the CirrusMD SDK will remain in Light mode
        allowDarkMode = true

        // Demo Patient does not currently have any dependents
        // enableDependentProfiles = true

        // OPTIONAL: Obtain Credential Id
        credentialIdListener = object : CredentialIdListener {
            override fun onCredentialIdReady(id: String) {
                Timber.d("Credential ID: $id")
            }
        }
        cirrusDataEventListener = this
        
        // OPTIONAL: Set up pinned banner that displays on home, chat, and settings screens
        configurePinnedBanner(this)
        
        start(this, SECRET)
    }

    private fun fetchTokenForCirrusMDSDK() {
        // This retrofit/fetcher/JWT process is unique for each implementation, based on your
        // organization's SSO environment and your app's architecture.
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .baseUrl(BASE_URL)
                .build()
        val fetcher = retrofit.create<TokenFetcher>(TokenFetcher::class.java)
        val request = TokenRequest(SDK_ID, PATIENT_ID)
        fetcher.getSessionJwt(request)?.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                // Once you have a token and secret, start the instance of the SDK.
                // There is a pre-flight process in the SDK to fetch the user's profile once it has started and report back to the listener.
                // Because of this pre-flight, we are not displaying the fragment until we receive a CirrusDataEvents.Success event in the listener.
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
        displayCirrusSDKButton?.visibility = View.VISIBLE
    }

    private fun onEventError(error: String) {
        Timber.e(error)
        homeText?.text = getString(R.string.error)
        displayCirrusSDKButton?.isEnabled = false
    }
    
    private fun configurePinnedBanner(context: Context) {
        CirrusMD.pinnedBanner = CirrusMDPinnedBanner(
            infoBannerMessage = "My custom info banner message",
            actionModal = CirrusMDActionModal(
                bannerTitle = "My custom banner title",
                modalTitle = "My custom modal title",
                modalHeader = "My custom modal header",
                modalSubHeader = "My custom modal sub header",
                contactOptions = listOf(
                    CirrusMDContactOption(
                        CirrusMDContactIcon.PHONE,
                        "Phone", "tel:8005555555"
                    ),
                    CirrusMDContactOption(
                        CirrusMDContactIcon.TEXT,
                        "SMS", "sms:555555"
                    ),
                    CirrusMDContactOption(
                        CirrusMDContactIcon.CHAT,
                        "Chat",
                        "https://www.google.com"
                    ),
                    CirrusMDContactOption(
                        CirrusMDContactIcon.TTY,
                        "TTY", "tel:8005555555"
                    ),
                    CirrusMDContactOption(
                        CirrusMDContactIcon.NONE,
                        "My custom list header"
                    ),
                    CirrusMDContactOption(
                        CirrusMDContactIcon.LINK,
                        "URL",
                        "https://www.google.com"
                    )
                )
            )
        )
    }

    /**
     *   This is where the CirrusMD SDK will report status events.
     */
    override fun onDataEvent(event: CirrusDataEvents) {
        when (event) {
            CirrusDataEvents.Success -> {
                // Note: onEvent can be called multiple times during the execution of the SDK.
                // It is best to just show/hide or enable/disable the button that presents the CirrusMDSDK Activity.
                // If you are going to display the Activity based on this result be sure to check
                // that it is not already shown.
                Timber.d("CirrusMD SDK successful pre-flight")
                homeText?.text = getString(R.string.ready)
                displayCirrusSDKButton?.isEnabled = true
            }
            CirrusDataEvents.LoggedOut -> onEventError("CirrusMD SDK user was logged out.")
            CirrusDataEvents.UserInteraction -> Timber.d("CirrusMD SDK user interaction")
            CirrusDataEvents.Error.InvalidJwt -> onEventError("CirrusMD SDK invalid JWT supplied")
            CirrusDataEvents.Error.InvalidSecret -> onEventError("CirrusMD SDK invalid secret supplied")
            CirrusDataEvents.Error.MissingJwt -> onEventError("CirrusMD SDK missing jwt")
            CirrusDataEvents.Error.MissingSecret -> onEventError("CirrusMD SDK missing secret")
            CirrusDataEvents.Error.ConnectionError -> onEventError("CirrusMD SDK connection error")
            CirrusDataEvents.Error.AuthenticationError -> onEventError("CirrusMD SDK auth error")
            CirrusDataEvents.Error.UnknownError -> onEventError("CirrusMD SDK generic error") //This error would include cases like network errors
            is CirrusDataEvents.VideoSessionEvents.ConnectionStatus -> Timber.d("CirrusMD SDK ConnectionStatus: ${event.meta}")
        }
    }
    
    override fun viewForError(event: CirrusDataEvents): View? {
        // If you would like to display branded error/logout messages, this is where the CirrusMD SDK will look.
        // Returning null will result in the default views being displayed.
        return null
    }

    companion object {
        private const val BASE_URL = "https://cmd-demo1-app.cirrusmd.com/sdk/v2/"
        private const val SDK_ID = "d2f0aa92-3da9-450c-9ba2-854e36a2e277"
        private const val PATIENT_ID = "63"

        // This is the secret for the sandbox environment.
        private const val SECRET = "eyJzaGFyZWRfc2VjcmV0IjoiOTg0MDMyNDYtMGJmMS00ZjNjLWFhMTktMDg1ZWFiMGMxMWE2IiwieDUwOV9jZXJ0X2RlciI6Ik1JSUQ4RENDQXRpZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0ZBREJjTVFzd0NRWURWUVFHRXdKVlV6RVdNQlFHQTFVRUNnd05RMmx5Y25WelRVUWdTVzVqTGpFVU1CSUdBMVVFQ3d3TFJXNW5hVzVsWlhKcGJtY3hEREFLQmdOVkJBTU1BMU5FU3pFUk1BOEdBMVVFQ0F3SVEyOXNiM0poWkc4d0lCY05OekF3TVRBeE1EQXdNREF3V2hnUE5EQXdNVEF4TURFd01EQXdNREJhTUZ3eEN6QUpCZ05WQkFZVEFsVlRNUll3RkFZRFZRUUtEQTFEYVhKeWRYTk5SQ0JKYm1NdU1SUXdFZ1lEVlFRTERBdEZibWRwYm1WbGNtbHVaekVNTUFvR0ExVUVBd3dEVTBSTE1SRXdEd1lEVlFRSURBaERiMnh2Y21Ga2J6Q0NBU0l3RFFZSktvWklodmNOQVFFQkJRQURnZ0VQQURDQ0FRb0NnZ0VCQU9QVWphcHBBRTg0NnZLZE9jN1VJR0xyK3RPMzBRMy9qVTdIU0FZbFNoZCtCV290bGw4RkkvVkZoc0RXcTBFRk9VUGRvMFQvSmVOcnRuYmVYYXllUlYzSFlRNU9admxkUlZhcUNPYmRmRmFhZWpDM2JVVW5zRFdWalJqTlExZTNicVFkYk9ELzZNZTd3cTI0NFExVTdkY2ZWL3lIVlgzMEtBOUxZZSthcmhWbld4TWFrWW1LbWZ1S1Vrby8vQm5tRDZiQkpJR3hGRzJTOXEwR3ZVODJ4RHViQitDbkpzczJ3QlFpWGE3VW8rM0E3NU5VNlgrcXUySlRQYnBMSkgyZ01oYUZ3MGwzOTN6T0ZuWldGWUx3dlhrTmd0bkYzVFIySk52ZWpseFBYQ0NKN0hrZVpuZVJUdXF2RFN0cGpWK2t2WDg2cUZIZWdMSE5ZTU5rK1pONm1CTUNBd0VBQWFPQnVqQ0J0ekFQQmdOVkhSTUJBZjhFQlRBREFRSC9NQjBHQTFVZERnUVdCQlJpTVBnbTF6SCtLamZpMlpGc3dqaEV3akZncHpDQmhBWURWUjBqQkgwd2U0QVVZakQ0SnRjeC9pbzM0dG1SYk1JNFJNSXhZS2VoWUtSZU1Gd3hDekFKQmdOVkJBWVRBbFZUTVJZd0ZBWURWUVFLREExRGFYSnlkWE5OUkNCSmJtTXVNUlF3RWdZRFZRUUxEQXRGYm1kcGJtVmxjbWx1WnpFTU1Bb0dBMVVFQXd3RFUwUkxNUkV3RHdZRFZRUUlEQWhEYjJ4dmNtRmtiNElCQURBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQVFFQXBYc2owNFBpNmlveXBIMys3cHhIYVM2cmVCVGJva1REUDVlcHM2QTVJamYzSWdsVG93U3IreldYNHJTTldRVVFXZUZqK1Nwa0hmZjVrVjV1bk5GdDk3OE16aXo0SFF0QTdxVzFhVG9mNks5dy93cHh5aGkrS250VzFOYzBjNHA1d1VnWGwvM0Z6YmJIRzJzekpRZEdPaUhTR3YwS3JtU1BJMjhkQ29HOWJUZXIzcG5BTnQ3aHZ6cHJWa3NmbXlkcTB4c1dEek8rSENQSjlIa0tPYXlLUnZZRVMxQXo1TGU2dWtmazRhbWlCc2FEekVmRWE1NzlnMEZXNVRMMFhWZ2xobVluQmxpVTU4d3JjT1hPTXJVQUFFZWVDNzJRQXBIcVBHUkJWNWJnSWV5eWVuUU1PQVJvckgzV2gwY0ZaNjc2WXpOUHUwTlpoTnd5bTJZUWZrNWF3Zz09IiwicHVibGljX2tleV9wZW0iOiItLS0tLUJFR0lOIFBVQkxJQyBLRVktLS0tLVxuTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUE0OVNOcW1rQVR6anE4cDA1enRRZ1xuWXV2NjA3ZlJEZitOVHNkSUJpVktGMzRGYWkyV1h3VWo5VVdHd05hclFRVTVROTJqUlA4bDQydTJkdDVkcko1RlxuWGNkaERrNW0rVjFGVnFvSTV0MThWcHA2TUxkdFJTZXdOWldOR00xRFY3ZHVwQjFzNFAvb3g3dkNyYmpoRFZUdFxuMXg5WC9JZFZmZlFvRDB0aDc1cXVGV2RiRXhxUmlZcVorNHBTU2ovOEdlWVBwc0VrZ2JFVWJaTDJyUWE5VHpiRVxuTzVzSDRLY215emJBRkNKZHJ0U2o3Y0R2azFUcGY2cTdZbE05dWtza2ZhQXlGb1hEU1hmM2ZNNFdkbFlWZ3ZDOVxuZVEyQzJjWGROSFlrMjk2T1hFOWNJSW5zZVI1bWQ1Rk82cThOSzJtTlg2UzlmenFvVWQ2QXNjMWd3MlQ1azNxWVxuRXdJREFRQUJcbi0tLS0tRU5EIFBVQkxJQyBLRVktLS0tLVxuIn0="
    }
}
