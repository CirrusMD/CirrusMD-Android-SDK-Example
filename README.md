# CirrusMD-Android-SDK-Example
The CirrusMD SDK it an embeddable SDK. It enables customers of CirrusMD to provide the CirrusMD patient chat experience in their own applications. While the example application will work in a sandboxed environment when built, integrating the CirrusMD SDK into your own application will require you to be a CirrusMD customer. Integration requires a unique `secret` and SSO `token` to work correctly, however this example uses sandbox credentials for demo purposes. Please contact your CirrusMD account representative for more information.

![screen](https://user-images.githubusercontent.com/11066298/34179281-874d1940-e4c7-11e7-9588-556de4bc5d62.png)

- [Requirements](#requirements)
- [Screenshots](https://github.com/CirrusMD/CirrusMD-Android-SDK-Example/wiki/Screenshots)
- [Installation](#installing-cirrusmdsdk-in-your-own-project)
- [Basic Usage](#basic-usage)
- [Advanced Usage](#advanced-usage)
  - [Logout](#logout)
  - [Custom Status Views](#custom-status-views)
  - [Push notifications](#push-notifications)
- [License](#license)

## Requirements
- minSdk: `21`

## Installing CirrusMDSDK in your own project
Grab the latest release from Jitpack:
[![Release](https://jitpack.io/v/CirrusMD/cirrusmd-android-sdk.svg)](https://jitpack.io/#CirrusMD/cirrusmd-android-sdk)

**Version 1.0.9 note**
For this version you will need to include the following lines in your build.gradle file in order for the JWT to parse correctly:
```
    implementation 'com.github.CirrusMD:cirrusmd-android-sdk:1.0.9'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.10.5'
    runtimeOnly('io.jsonwebtoken:jjwt-orgjson:0.10.5') {
        exclude group: 'org.json', module: 'json' //provided by Android natively
    }
```
A cleaner solution will be available in a future release.

## Basic Usage

Basic usage of of the CirrusMD SDK is very simple.
1. Retrieve a token via SSO (See [the details](#the-details))
1. Set the CirrusMD provided secret and context via `CirrusMD.start(context: Context, secret: String)`
1. Set the retrieved token via `CirrusMD.setSessionToken(token: String)`
1. Get the fragment with `CirrusMD.getFragment()`

### The details

In Java you can get the SDK instance with `CirrusMD.INSTANCE`.  
In Kotlin you can call `CirrusMD`.

*** **_Do not cache CirrusMD provided JWTs_** ***

1. Our team works with your technical staff to provide SSO for your patients using the CirrusMD platform. The CirrusMDSDK uses tokens retrieved via SSO from CirrusMD's SSO service. Each SSO integration is slightly customized based on your needs. In general, your backend service requests a token representing a patient from our SSO service which provides the token that should be set on the SDK.
2. You have the option to set a `CirrusListener` for error and success events, as well as optionally providing customized error screens. This can be done with `CirrusMD.setListener(CirrusListener)`
3. Start the SDK with the secret and SSO token
4. Once the SDK is started it will attempt to fetch the users profile. At this point you will either receive an error or success through the `CirrusMD.CirrusListener.onEvent(CirrusEvents)` method.
5. On a successful event, you can display the fragment.

## Advanced Usage

### Logout

You may wish to log the user out of the SDK when they sign out of your application. Logging the user out destroys the associated CirrusMD server session and unregisters the device from CirrusMD delivered push notifications if previously registred. A logout can be done by calling `CirrusMD.logout()`

### Event/Error Handling

Through the `CirrusMD.CirrusListener.onEvent` interface method, you can receive one of the following events:
- `INVALID JWT` : An improperly formatted JWT was provided.
- `INVALID SECRET` : An improperly formatted secret token was provided.
- `AUTHENTICATION_ERROR` : There was an authentication failure on a network request.
- `CONNECTION_ERROR` : There was an HTTP exception not otherwise specified.
- `LOGGED OUT` : The current user session was logged out.
- `SUCCESS` : The SDK was provided a valid JWT and secret token and was able to make a request. It is ideal to wait for this event before displaying the fragment.
- `UNKNOWN_ERROR` : This is the generic catch for errors that could not be identified.

### Custom Status Views

Ideally, your patients always see a working messages view when you present a ` CirrusMDMessagesFragment`. However, there are certain times when we're unable to show messages.

The first is when you have explicitly called `logout()`. We recommend calling logout when your patient logs out of your appication. In that case they will not see the _logged out view_ because they will be logged out of your application as well. You should log them back into the SDK when they next log back into your application.

The second is when the SDK is unable to verify the secret, the token or there is another issue (ie network) starting the SDK. In either case, an _error view_  is shown. We recommend you handle all errors through `CirrusListener` prior to showing the `CirrusMDMessagesFragment` if possible. Doing so will provide a better experience for your user. Some errors may happen after the `CirrusMDMessagesFragment` is already on screen. In that case, _error view_ is displayed.

Two screens displayed by the SDK have default views that can be overridden via the `CirrusMD.CirrusListener.viewForError()` interface method. We strongly recommend that you provide your own custom views for both cases. Because the CirrusMDSDK uses SSO to authenticate your patients, we are unable to provide logged out UI that helps the patient log back in. By providing your patients with a custom _logout out view_ you can, for example, provide relevant messaging and a button to log back in using the same SSO you implemented to log them in originally. Every time the _error view_ is shown the resolution is retrieving a new SSO token and setting it via `CirrusMDSDK.start(token, secret)`. Providing a custom _error view_ gives you the ability to display relevant messaging and interactions the user can take, most likely a button to re-attempt SSO.

When an error view would be displayed, errors will also be delivered to the `CirrusMD.CirrusListener.onEvent` interface if you would like to handle the error entirely outside of the CirrusMD SDK.

By default they will look similar to the screens below:

The default logged out screen is shown after you call `CirrusMD.logout()`.

![logout](https://user-images.githubusercontent.com/11066298/34179364-c60bbd9e-e4c7-11e7-85e5-7f92d5bd85ae.png)

The error screen can be shown for several reasons, such as providing an expired token or invalid secret.

![error](https://user-images.githubusercontent.com/11066298/34179280-873a7dda-e4c7-11e7-81df-26249aa75166.png)

Providing custom views of both the _logged out view_ and _error view_ happens via the currently set `CirrusMD.CirrusListener`.

### Push notifications
There are two options of push notification implementations.

1. Use CirrusMD's Firebase account and CirrusMD will provide the `google_services.json` config file.
2. Use your Firebase account and provide CirrusMD with the cloud messaging server key for your application.

Within the application, the specific device must be registered for push notifications using the `CirrusMD.setPushToken(context, deviceToken`) method. 

Information about fetching/managing device tokens with Firebase can be found [here](https://firebase.google.com/docs/cloud-messaging/android/client).

### Support Library Versions

The current version of the CirrusMD SDK is build with version `26.1.0` of the Android Support Library. If your app requires a different version than what is included, you will need to exclude that package and then import the required packages using your version.

To exclude the package:
```groovy
implementation('com.github.CirrusMD:cirrusmd-android-sdk:CURRENT-VERSION') {
    ['com.google.android', 'com.android.support'].each {
        exclude group: it
    }
}
```

The list of support libraries used by the CirrusMD SDK:
```groovy
implementation "com.android.support:design:$SUPPORT_LIBRARY_VERSION"
implementation "com.android.support:appcompat-v7:$SUPPORT_LIBRARY_VERSION"
implementation "com.android.support:cardview-v7:$SUPPORT_LIBRARY_VERSION"
implementation "com.android.support:recyclerview-v7:$SUPPORT_LIBRARY_VERSION"
implementation "com.android.support:support-fragment:$SUPPORT_LIBRARY_VERSION"
implementation "com.android.support:support-compat:$SUPPORT_LIBRARY_VERSION"
implementation "com.android.support:support-v4:$SUPPORT_LIBRARY_VERSION"
implementation "com.android.support:support-annotations:$SUPPORT_LIBRARY_VERSION"
```

## Author

CirrusMD

## License

CirrusMD-Android-SDK-Example is available under the MIT license. See the LICENSE file for more info.

CirrusMD SDK is available under the XXXX license. See the LICENSE file for more info.
