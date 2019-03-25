# 2.6.0 / 2019-3-25
* **NOTE: This is the last version of the SDK to use the legacy Support Library. 2.7.0 and above will use AndroidX.**
* Moved to Android 28 and Support Library 28.0.0
* Now using R8 instead or ProGuard
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
* SDK: The SDK will now attempt to set the toolbar title based on the encounter state
* SDK: Adding unread indicators for streams not currently being viewed

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

