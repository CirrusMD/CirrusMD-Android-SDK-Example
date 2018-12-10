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

