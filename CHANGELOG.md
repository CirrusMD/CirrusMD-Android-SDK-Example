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

