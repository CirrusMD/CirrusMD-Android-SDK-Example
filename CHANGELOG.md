# 10.5.0 / June 7th, 2023 / minSdk = API 24 (Android 7) / targetSDK: API 33 (Android 13)
### SDK Features/Changes:
- Minor UI updates in My Family settings
- Enhanced prescription information capture for patients, with pharmacy search feature
- Enhanced accessibility for patient assessments
- Minor Dependency updates

### SDK Bug Fixes:
- All detail text view subtitles will now autolink phone, emails, web links, and map coordinates (Specifically, in the Support settings, all phone numbers and emails will autolink to provide a good user experience)
- Fixed toolbar bug in Support settings
- Fixed issue where rapidly tapping on an assessment action button would cause multiple assessments to be created and displayed to the user
- Fixed issue where the message body was disappearing from encounter messages, as the patient scrolls to older messages in the chat.


# 10.4.0 / April 6th, 2023 / minSdk = API 24 (Android 7) / targetSDK: API 33 (Android 13)
### SDK Features/Changes:
- Minor dependency updates:
  - `androidx_exifinterface = '1.3.6'`
  - `androidx_fragment = '1.5.5'`
  - `androidx_recyclerview = '1.3.0'`
  - `appcompat = '1.6.1'`
  - `firebase_messaging = '23.1.2'`
  - `test_core = '1.5.0', test_runner = '1.5.2', test_rules = '1.5.0'`
  - Google Play services: `'com.google.gms:google-services:4.3.15'`
- Improved assessment/questionnaire capabilities
- Improved assessment/questionnaire accessibility
- Users with no streams now see a "no streams" message instead of blank screen
- Minor dark/night theme bug fixes and updates


# 10.3.0 / March 2nd, 2023 / minSdk = API 24 (Android 7) / targetSDK: API 33 (Android 13)
### SDK Features/Changes:
- Night theme support!
  - Look at our documentation for a full depth look at how to enable Night theme support: https://github.com/CirrusMD/CirrusMD-Android-SDK-Example#readme
- Chat stream accessibility updates
- Removed LifecycleObserver build warnings for SDK module. The remaining warning are mostly due do CirrusEvent deprecation, which we will remove in the near future
- Added "Details" button and border to ProgressNote (now matches iOS CirrusMD SDK)

### SDK Bug Fixes:
- Fixed image permissions issues in dependent profile fragment
- Fixed `IllegalStateException`, in `EventStreamFragment` caused by improper fragment transaction
- Logcat logging cleanup
- Now trimming empty space from Add Minor Dependent form fields,
- Fixed Toolbar disappearing issues in settings



# 10.2.0 / 2022-12-19 / minSdk = API 24 (Android 7) / targetSDK: API 33 (Android 13)
### SDK Bug Fixes:
- Fixed issues with text wrapping in Subscriber ID capture flow/view

### SDK Features/Changes:
- Updated `targetSDK` to `33`
- Dependency updates:
  - AndroidX Exifinterface -> 1.3.4
  - AndroidX Fragment -> 1.5.3
  - AndroidX Compose -> 1.2.1
  - AppCompat -> 1.5.1
  - Firebase Core -> 21.1.1
  - Firebase Messaging -> 23.1.0
  - NewRelic -> 6.8.0
- Refactored all AlertDialogs to use light theme, with primary color (`cirrus_primary_dark`) used as accent.
- Added `POST_NOTIFICATION`, `READ_MEDIA_IMAGES`, and `AD_ID` permission declarations in `sdk/AndroidManifest.xml` (necessary when targeting Android 13)
- Added logic to support showing Push Notification permissions request once the user enters the SDK (for Android 13+ devices only). (Matches iOS CirrusMD SDK now)
- Added Android 13 specific flow for image/media permission requests
- Refactored runtime permissions logic for profile images. The logic should now match what we have in the Chat stream

NOTE: With Android 13's permission changes, we've had to change permission declarations in our AndroidManifest.xml file.
Our SDK now requests permissions during runtime, according to the version running on the device.

```
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
    <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    <uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```

If your app still uses `WRITE_EXTERNAL_STORAGE` or `READ_EXTERNAL_STORAGE`, we recommend that you set ` android:maxSdkVersion="32"` on these permissions (as shown above), 
since these are or will be deprecated, and can conflict with permission handling on Android 13 devices.


# 10.1.0 / 2022-10-20 / minSdk = API 24 (Android 7) / targetSDK: API 31 (Android 12)
### SDK Features/Changes:
- Dependency updates
- Removed `VideoError.kt`
- Added support for Spanish localization. Please contact your CirrusMD account representative for more information.
  - Added Language tab to settings, which navigates user to device locale settings
  - If device and CirrusMD's user profile languages do not match, the app preferred_language is set to english, unless the device language is set specifically to Spanish (at which point an AlertDialog is displayed notifying the user)
  - Real time translation support, in Spanish, when chatting with a doctor. The device language must be set to Spanish (es, es-US, or any other spanish locale), AND must be configured in CirrusMD's Manage settings.
  - Accessibility announces translated message if the language code matches the language code in user preferences
- Progress notes translated

### SDK Bug Fixes:
- Updated `sourcesJar` gradle task to include new customer facing classes (CirrusDataEventListener, CirrusDataEvent etc.)
- Fixed issue where multiple encounters are created, when user decides to open an encounter with an image first, followed by text message quickly after (before the backend has a chance to create the encounter on record). The fix involves disabling the text send button until the image has successfully been uploaded and therefore the encounter has also been created. The user will be allowed to send the message after, without creating 2 encounters.
- Fixed issue/crash where users were unable to answer multiselect answers in assessments
- Fixed issue where we weren't refreshing the stream (and hence show the out of service hours banner) when the patient happens to open the chat stream right before the end of service hours, sends a message, and then the plan happens to be out of service hours.
- Fixed issue with toolbar disappearance after the app is backgrounded while on the Chat stream

### SDK dependency updates:
- Shared Dependency updates:
    - Gradle Wrapper -> 7.0.2
    - Gradle Build tools -> 7.0.0 (compatible with New Relic)
    - Timber -> 5.0.1
    - Android Build Tools -> 30.0.3
    - AndroidX Fragment -> 1.5.1
    - AppCompat -> 1.4.2
    - AndroidX ConstraintLayout -> 2.1.4
    - JUnit -> 4.13.1
    - Kotlin Coroutines -> 1.6.1
    - Kotlin Coroutines Test -> 1.5.2
    - Google Material -> 1.6.1
    - unmockplugin -> 0.7.8
    - Kotlin-gradle-plugin -> 1.6.10
    - Kotlin reflect -> 1.7.10
    

# 10.0.0 / 2022-07-27 / minSdk = API 24 (Android 7) / targetSDK: API 31 (Android 12)
### SDK Features:
- Added assessments feature, which will allow patients to receive and submit GAD7, PHQ9, and ACT questionnaires. Mobile Clients that are on 10.0.0+ will receive a native experience questionnaire, while older versions will receive a URL link to complete the assessment outside of the SDK experience.

###SDK Bug Fixes:
- Fixed issue where provider name doesn't display in toolbar on some occasions
- Fixed issue where patient name isn't displayed properly in the stream when using channels to link directly to the desired chat


# 9.5.0 / 2022-05-23 / minSdk = API 24 (Android 7) / targetSDK: API 31 (Android 12)

### SDK Features/Updates:
- New DE&I fields can be updated in user's profile settings. DE&I can now be controlled via CirrusMD's manage
- Cross channel transfer feature allows users to transfer to new chat, if and when the provider transfers the patient.
- Added account delete feature, which enables users to disable their account, and request that their account be deleted. NOTE: This is used internally for the CirrusMD developer team. We DO NOT recommend setting this flag to true, without consent from CirrusMD. Please talk to your CirrusMD Account Manager before enabling this feature.
- Video chat/call requests now appear as an in-chat message, so users can now join a video call effortlessly.
- Opentok dependency update to 2.21.5
- `CirrusDataEvents.Error.VideoSessionError` is now `CirrusDataEvents.VideoSessionEvents.SessionError`

### SDK Bug Fixes:
- Fixed issues where messages weren't wrapping their title appropriately, effecting smaller screen sizes and larger font sizes.


# 9.4.0 / 04-01-2022 / minSdk = API 24 (Android 7) / targetSDK: API 31 (Android 12)
### SDK Features:
* CirrusMD SDK now targets Android 12!
* CirrusEvents is now deprecated. CirrusDataEvents is its replacement, which gives us more flexibility around sending instance data to mobile clients. We are maintaining compatibility, but will stop supporting it in the near future.
* CirrusListener is now deprecated. CirrusDataEventListener is its replacement. Again, we are maintaining compatibility, but will stop supporting it in the near future.
* Video Sessions throw video errors to CirrusDataEventListener now, so the mobile clients can do whatever they want with it (either report it to a crash handler or something else)
* Added subscriber verification feature
* Added Insurance info view to settings, which will display the patient's subscriber ID and insurance provider
* Terms of service URL, shown in settings, is now configurable via CirrusMD's manage.


# 9.3.0 / 01-21-2022 / minSdk = API 24 (Android 7) / targetSDK: API 30 (Android 11)
### SDK Features:
* Added the ability for the user to set a preferred name, preferred pronouns, and gender Identity, in profile settings
* The CirrusMD SDK now locks down any eligibility related data in the profile settings and dependent settings.

### SDK Bug Fixes:
* Zipcode field in Profile settings will now only allow 5 digit entries
* Fixed issue where user's previously selected location wasn't persisting in the location selection view.
* Added FLAG_IMMUTABLE to PendingIntents to solve IllegalStateException and Lint errors

### Dependency Updates:
* Remaining dependencies now pull from mavenCentral(), in order to remove the deprecated JCenter() dependency
* Update Opentok dependency to 2.21.4, in order to fix video chat issues for Android 12 users


# 9.2.0 / 10-29-2021 / minSdk = API 24 (Android 7) / targetSDK: API 30 (Android 11)
### Features:
* Added `CirrusActions`, which will allow mobile clients to send actions/events to our SDK to respond to, via `CirrusMD.sendCirrusAction()`. See documentation for more details.
* Added an "ask location" feature that will allow users to select the state in which they are currently located, in order to get matched up with a provider in the future.
* SDK version is now displayed in the settings page, when settings are enabled.

### Bug Fixes:
* Fixed sizing issues with the patient satisfaction survey on smaller screens


# 9.1.0 / 08/18/2021 / minSdk = 24
### Features:
* Added support for isSettingsEditable and isPatientSettingsVisible flags from CirrusMD Manage console
* Added ability to update Primary Care Physician via the Medical Settings view, in Settings.
* The Avatar, patient name, and the chevron icon (shown on the Channels of care) should now be one clickable unit that will navigate the user to the patient selector view.
* Android client should now gracefully handle provider-deleted messages in the Event Stream (chat view)
* Removed better-pickers dependency from CirrusMDSDK.
* Androidx.Fragments dependency update to 1.3.5
* Androidx.appcompat dependency update to 1.3.0
* Firebase core and messaging dependency updated to 19.0.0 and 22.0.0, respectively
* Moshi version update to 1.12.0
* Kotlin plugin version bump to 1.5.10
* Gradle build tools version update to 4.2.0
* Opentok version update to 2.20.1
* Kotlin Coroutines version update to 1.5.0
* Other minor dependency updates

### Bug Fixes:
* Fixed issue where guarantor's name should be sent over to the web socket, to indicate that the guarantor's dependent is typing during an encounter meant for the dependent. This matches the behavior on iOS and Web.



# 9.0.1 / 06/24/2021 / minSdk = 24
* Fixed issue where our Joda Time dependency cause an issue with Proguard, for Flutter projects.

# 9.0.0 / 06/21/2021 / minSdk = 24
### SDK Features:
* Removed the com.appboy:android-sdk-ui (aka Braze) dependency
* Removed CirrusMDBrazeConfig and CirrusMDBrazeProvider as part of removing the Braze dependency
* Removed "my family" button from the header bar (in the channels of care view) and added a chevron icon to indicate a new navigation pattern. The new Navigation pattern will show a patient selector view, which is reminiscent of the "my family" dependents view.
* Changed the SDK's FileProvider authority to a unique path that is less likely to collide with a parent app or other dependencies.
* Added a 'Add Minor Dependent' button to My Family Settings view, which is enabled/disabled in Manage. This new button navigates the user to a form that allows the user to add a minor dependent.
* Added Minor Dependent form to allow users to add their minor dependents, using first name, last name, DOB, and zipcode

### SDK Bug Fixes:
* Added Theme to VideoSessionActivity in AndroidManifest file: See https://github.com/CirrusMD/CirrusMD-Android-SDK-Example/issues/37 for more information
* Dependent/My Family accessibility improvements
* Removed the Bintray maven repository that was being used to pull the OpenTok dependency and instead pull the OpenTok dependency from mavenCentral(). This was done because the Bintray repository is being deprecated on May 1st, 2021.
* Fixed issue where done button may not show up for users with slow connections
* Fixed issue where survey error alerts may show up on top of each other, when receiving multiple error events.

### Other SDK Changes:
* Firebase and AndroidX dependency minor version bump
* Other dependency updates
* Compiler warning clean up


# 8.0.1 / 04/28/2021 / minSdk = 24
### SDK Bug Fixes:
* Removed the Bintray maven repository that was being used to pull the OpenTok dependency and instead pull the OpenTok dependency from mavenCentral()

# 8.0.0 / 04/20/2021 / minSdk = 24
### Features:
* Added patient satisfaction survey capability, after a user completes an encounter
* Accessibility updates

### Bug Fixes:
* Fixed issue where provider bio menu item appears in toolbar when user is already on the provider bio view
* Fixed issue where multiple dependents receive an unread message indicator, instead of the actual dependent having a conversation
* Fixed issue where dependent profiles weren't editable


# 7.0.0 / 02/22/2021 / minSdk = 24
### SDK Features:
* Removed toolbar color gradient
* Dependency updates
* Build tools updated to 29.0.3
* Images added to chat can now be seen in fullscreen
* Added a security feature that allows the SDK to recover from certificate pinning failures

### SDK Bug Fixes:
* Fixed issue where SDK will crash if opentok dependency is excluded, and the sdk receives a video:now web-socket event
* Fixed video session issue affecting Android 7.0 devices, where a patient's video feed is not visible to user

# 6.0.0 / 12/15/2020 / minSdk = 24
### SDK Features:
* Added Braze push notification and in-app messaging support
* Added support for virtual assistant messages
* Added marketing communication setting to SDK Profile Settings

### SDK Bug Fixes:
* Fixed issue where SDK may produce crash because of an NPE during SDK initialization
* Fixed issue where Progress Note does not show toolbar

# 5.0.2 / 04/28/2021 / minSdk = 24
### SDK Bug Fixes:
* Removed the Bintray maven repository that was being used to pull the OpenTok dependency and instead pull the OpenTok dependency from mavenCentral()

# 5.0.1 / 11/20/2020 / minSdk = 24
### SDK Bug Fixes:
Fixed issue where the patient avatar, in the chat, shows a null string and the dependent avatar, when using the CirrusMD.navigateToChannel() SDK function.

# 5.0.0 / 11/13/2020 / minSdk = 24
### SDK Features
* Add a new queue status to inform patients when there is a queue in a stream and how many patients are in that queue before they send a message
* Implemented a new queue status UI where the all of the information is presented directly in the stream view
* Implemented a new UI to inform the patient when the stream is in off-hours
* Added an "Exit Queue" button to the queue status UI that allows the patient to leave the queue
* Removed getProfile() calls upon encounterUpdated web socket events. Instead, we should now be calling getStream() and getEncounter().
* Updated after-hours UI, so that we can show a new header and subtitle when a plan is in an after-hours state.
* Added newly supported "benefits:updated" and "plan:started" web socket events, which should trigger a stream refresh
* Added the enableUserLogOut property to CirrusMD Object, which controls whether the "Sign Out" option exists inside of the SDK, which allows the user to manually sign out of the CirrusMDSDK.
For example, the Sign Out option can exist in the Settings view or in 401 error alerts. The enableUserLogOut property defaults to false.
* Added the enableDebugFragmentInSettings property to CirrusMD Object, which controls whether the "Debug" option exists inside of the SDK, which allows the developer to see debug information while on the SDK.
* Added the userAgentPrefix property to CirrusMD object, which allows the addition of a custom prefix to the User Agent used with network requests. This defaults to an empty string, which means there will be no custom prefix on the User Agent.
* Improved/standardized the User Agent sent by the SDK
* Removed "is Typing..." UI from the StreamPickerFragment
* Added "pencil" icon, which indicates that a field is editable, in the Profile, Family, and Medical settings.
* Added "edit" textview to profile image view in the profile settings, to indicate that the image is configurable.
* Removed `dontobfuscate` proguard rule, so that SDK consumers can obfuscate their apps

### SDK BUG FIXES:
* Fixed issue where Timber logs could print twice if the SDK consumer has planted a Timber debug tree instance, before initializing the SDK
* Added support contact info to settings
* Fixed issue where SDK tries to unregister push devices even though they weren't registered to begin with.
* Fixed issue where jwt can get cleared before unregistering push device, on logout.
* Fixed issue where patient names could be misconfigured in the chat, if the the user navigated to it from a push notification.
* Fixed issue where unhandled HTTP exceptions potentially crash the SDK. Instead, any previously unhandled HTTP errors fire a CirrusEvent.UNKNOWN_ERROR event
* Fixed issue where NetworkStateObserver would produce a Network.Connect event, causing the red dotter to falsely show a red dot on a stream that has been acknowledged/read.
* Fixed issue for Android 11 users, where capturing an image and sending it to a provider, via chat, would NOT work.

# 4.0.1 / 10/22/2020 / minSdk = 21
* Changed CirrusMDSDK's minSdk to 21

# 4.0.0 / 09/18/2020 / minSdk = 24
### New Features:
- Added settings view to SDK, which can be enabled by the client and the ability to switch to and from dependent accounts associated with the user's account.
- Created a callback in ```CirrusMD``` to dispatch a patient's credential ID
- Added ability to change back button color across the SDK. Please check the documentation as a reference.
- Chat/Encounter toolbar will now show plan name, and then the provider's name will take it's place when the patient is assigned.
- Added inactive stream message to indicate that the patient no longer has access to the health plan/stream

### Bug Fixes:
- Fixed red dot appearing in toolbar for user profiles without any dependents.
- CirrusMD.shouldShowNotification() should now return false if either of the NotificationMetaData fields are empty, because the SDK won't be able to show the notification in this case.
- Push notification registration errors should now fail silently.
- Fixed bug where Patients get switched to another channel of care
- Fixed pre-encounter alert issue where it would show up multiple times in a row.

### Other Updates:
- Updated ```targetSdk``` and ```compileVersion``` to API ```29```
- Updated ```minSdk``` to API ```24```


# 3.2.4 / 2020-08-26 / minSdk = 21
* New USER_INTERACTION, MISSING_JWT and MISSING_SECRET CirrusEvents added to MainActivity. Based off of 3.2.3.

# 3.1.4 / 2020-08-25 / minSdk = 21
* New USER_INTERACTION, MISSING_JWT and MISSING_SECRET CirrusEvents added to MainActivity. Based off of 3.1.3.

# 3.2.3 / 2020-08-20 / minSdk = 21
* Additional updates to the pinned SSL certificates. Based off of 3.2.2.

# 3.1.3 / 2020-08-19 / minSdk = 21
* Additional updates to the pinned SSL certificates. Based off of 3.1.2.

--- 
# DEPRECATED: All builds below this line are deprecated and will not work starting on 8/20/2020

# 3.2.2 / 2020-07-27
* Updated the pinned SSL certificates. Based off of 3.2.1.

# 3.2.1 / 2020-06-25
* Fixed an ID casting issue.

# 3.1.2 / 2020-07-27
* Updated the pinned SSL certificates. Based off of 3.1.1.

# 3.1.1 / 2020-04-23
* SDK: Empty message toasts are now displayed for 10 seconds for ADA compliance.
* SDK: Fixed issue where provider initials persists in care channel stream after provider releases, dismisses, or completes patient encounter.

# 3.1.0 / 2020-04-10
* SDK: Refactored the SDK to provide optional debug logging, which can be enabled by setting 'CirrusMD.enableDebugLogging = true', before calling the CirrusMD.start() function. When set to 'true' the SDK will print extensive logging to Logcat for debugging purposes. By default, debug logging is NOT enabled.
* SDK: Fixed crash when attempting to display an error message in channel that would cause an "IllegalStateException: Child already has parent".
* SDK: Fixed Crash when tapping back button from messages view, caused by an "IllegalStateException: Can not perform this action after onSaveInstanceState"
* Added support for messages with the CANCEL meta data action

# 3.0.0 / 2020-03-27
* Refactored the SDK to provide the consumer with an Intent that they can use to start a CirrusMD Activity instead of a Fragment
* Added a debugFragment() function that provides developers with a fragment that displays some debug information
* Made the tappable area around the Provider Bio button larger to prevent accidentally tapping the Back to channels of care bar instead
* Fixed multiple bugs and a crash when switching between portrait and landscape orientations
* Fixed multiple bugs and crashes that could occur when opening the SDK from a push notification
* Fixed/improved handling of video push notifications
* Improved handling of incoming video requests when the SDK's Activity is running

# 2.9.2 / 2020-03-05
* Do not show pre-encounter alert (patient billing alert) if the header and body are empty
* Fixed an issue that could prevent pre-encounter alerts from showing

# 2.9.1 / 2020-02-21
* Migrated to New Relic for crash reporting
* Fixed a crash that could occur when loading new profiles

# 2.9.0 / 2020-02-12
* Added provider bio
* Always land on the stream picker (unless linking in from push or external channels)
* Fixed an error that could occur with admin dismiss actions
* Fixed a crash when tapping on a progress note
* Fixed an issue where unread states could be wrong
* Fixed the behavior of the back button
* Fixed an issue with unread states
* Fixed an issue that could prevent users from entering a video session
* Fixed an issue where the user could end up seeing empty streams
* Fixed an issue where user would not always land on correct stream when tapping a push notification
* Added support for video push notifications
* Fixed an issue where backgrounding the app could cause a crash or an unexpected navigation stack change
* Fixed issues that could prevent push notifications from showing
* Fixed an issue that could prevent provider bios from appearing
* Fixed an issue that could prevent video sessions from starting correctly
* Fixed an issue that could prevent the queue status details from appearing when tapping the queue status bar
* Fixed a crash when entering a video chat on a release build

# 2.8.7 / 2019-12-26
* Updated video dependencies
* Fixed a bug where and empty stream picker was being displayed in failed auth
* Cleaned up requests/setup when there was no API URL due to failed auth
* Adding support for alerting patients of high deductible plan billing
* Fixed a bug with the error screen not being displayed in some situations

# 2.8.4 / 2019-11-13
* Fixed an issue where in some cases users were unable to send an image
* Various bug and crash fixes
* Hiding the message input bar when a plan is inactive with no active encounter

# 2.8.3 / 2019-11-01
* Now closing the keyboard when navigating to the stream picker
* Adding better handling of failed messages sent
* Fixing an issue where the connection snackbar is not being dismissed on screen rotations 

# 2.8.2 / 2019-10-16
* Fixing the blank stream issue
* Fixing the issue of sent messages not showing
* Fixing an issue where the wrong TimeZone was being used for timestamps

# 2.8.1 / 2019-10-7
* A user with multiple streams will now be presented with the stream picker if one is not selected
* Initials in the avatar are no longer white text on white background

# 2.8.0 / 2019-9-25
* Adding video event handling (Note: This requires updates to your build.gradle, even if you're not using video. https://github.com/CirrusMD/CirrusMD-Android-SDK-Example#video)
* Fixed a bug where some devices would not use the correct time zone for the message time stamp

# 2.7.1 / 2019-6-6
* Now using AndroidX
* The attachment button can now be disabled via a back end config
* Fixed a bug where encounter/stream updates were not happening when a new profile was fetched
* Updated many dependencies

# 2.6.0 / 2019-3-25
* **NOTE: This is the last version of the SDK to use the legacy Support Library. 2.7.0 and above will use AndroidX.**
* Moved to Android 28 and Support Library 28.0.0
* Now using R8 instead of ProGuard
* Updated dependencies to current versions
* Cleaning up the way we monitor/notify for internet connectivity
* Adding the ability for a host application to go to a stream for push notifications via `onPushNotificationSelected(NotificationMetaData)`
* Adding the ability for a host application to check if a notification should be displayed via `shouldShowNotification(NotificationMetaData)` and `shouldShowNotification(streamId: String, event: String)`
* Adding the ability for a host application to fetch and navigate to a stream via the `external_id` using `CirrusMD.channels()` and `CirrusMD.navigateToChannel(String)`

# 2.5.3 / 2019-2-22
* Fixed a bug with the loading spinner staying visible after loading
* Enhanced accessibility announcements for messages coming in/out
* Phone numbers, emails and URLs are now clickable in messages, queue messages, welcome screen and progress notes
* Updated styling for encounter state change events
* Fixed a crash related to entering the app via push notifications
* Providers in the stream picker will show their title
* The SDK will now attempt to set the toolbar title based on the encounter state
* Adding unread indicators for streams not currently being viewed

# 2.5.0 / 2018-12-10

* Added multiple channels of care
* Updated default ProGuard config file
* Updated interactions for screen reader users

# 2.4.9 / 2018-12-10

* Adding a button/bar to dismiss the image details screen

# 2.4.8 / 2018-11-27

* Addressing a crash where the `SdkEventStreamServiceInteractor` was attempting to create the instance of retrofit before the URL was gathered from the JWT.

# 2.4.7 / 2018-11-26

* Fixed the incorrect colors used in some queue states. Colors should now be the same in both the banner above the message  input bar and the header of the details modal.
* Text of the welcome message is now left aligned
* Text of the message input bar is no longer bold when entered

# 2.4.6 / 2018-10-23

* Added the attending provder's plan name to the progress note details
* Added the ability to pinch to zoom images in the details view
* Fixed some issues with images flickering/refreshing when not needed

# 2.4.5 / 2018-10-01

Bug Fix:

* Fixing a crash where the ImageDetailsFragment was being dismissed before startPostponedEnterTransition() was being called

# 2.4.4 / 2018-10-01

Bug Fix:

* Fixing some NPE bugs when the presenter talked to the view

# 2.4.1 / 2018-10-01

The large version jump is a result of merging the SDK in to the app repo and using shared versioning.

Enhancements:

* Accessibility updates
* Ability to configure colors and the welcome image
* Updated Progress note designs
* Updated PEQ designs/colors
* The message input bar now wraps
* Optimized some animations
* Updated timestamps
* Added styles to modal buttons

# 1.0.9 / 2018-09-04

* Removing the Jackson dependency from our JWT implementation

# 1.0.0 / 2018-07-11

Enhancements:

* Adds accessibility labels, hints, and announcements

# 0.2.0 / 2018-04-10

Enhancements:

* Attempting to add support for Support Library differences

# 0.14 / 2017-12-12

Enhancements:

* First release of CirrusMDSDK

