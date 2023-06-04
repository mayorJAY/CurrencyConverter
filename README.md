<p align="center">
  <img src="media/app_icon.png" title="App Logo">
</p>

# Currency Converter

Currency Converter is an offline-first Android Application that allows the user to view a given amount in a given currency converted into other currencies. The App fetches the data from [Open Exchange Rates API](https://openexchangerates.org) and uses Room for offline caching. The App has a Splash Screen and a home page which initially displays the rates of all available Currencies. A user can input any desired amount and select any currency from the list after which the App converts the figure into other currencies and displays the different amounts to the user. To limit bandwidth usage, the App runs a periodic work (every 30 minutes) to update the local database with remote data. It is implemented using Clean Architecture, Model-View-ViewModel (MVVM) pattern and uses Modern Android Development pattern and libraries. Adequate Unit Tests were also implemented in the codebase.

## Project Characteristics

This application has the following characteristics:
* 100% Kotlin
* Modern Architecture (Clean Architecture, Model-View-ViewModel (MVVM))
* [Android Jetpack Components](https://developer.android.com/jetpack)
* [Material Design](https://material.io/develop/android/docs/getting-started)

## Tech Stack

Minimum API level is set to 21, this means HolidayInfo can run on approximately 98% of Android devices
* [Splash Screen](https://developer.android.com/develop/ui/views/launch/splash-screen), the standard recommended Splash Screen library for Android Applications
* [Retrofit](https://square.github.io/retrofit/) which is a type-safe REST client for Android which makes it easier to consume RESTful web services
* [Moshi](https://github.com/square/moshi), a modern JSON library for Android, Java and Kotlin
* [OkHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor), an OkHttp interceptor which logs HTTP request and response data
* [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) used to perform asynchronous network calls to the remote server
* [Room](https://developer.android.com/training/data-storage/room), a persistence library with an abstraction layer over SQLite for database manipulation
* [Hilt](https://dagger.dev/hilt/), a DI library for Android that reduces the boilerplate of using manual DI
* [WorkManager](https://developer.android.com/reference/androidx/work/WorkManager), the recommended library for persistent (periodic or repetitive) work
* [App Startup](https://developer.android.com/topic/libraries/app-startup), which provides a straightforward, performant way to initialize components at application startup
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) used to store and manage UI-related data in a lifecycle conscious way
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) which is an observable data holder class used to handle data in a lifecycle-aware manner
* [View Binding](https://developer.android.com/topic/libraries/view-binding) used to easily write code that interacts with views by referencing them directly
* [Kotlin flow](https://developer.android.com/kotlin/flow) for emitting live updates from a network call sequentially
* [Material Design](https://material.io/develop/android/docs/getting-started/) which is an adaptable system that guides in maintaining principles and best practices of contemporary UI
* [Timber](https://github.com/JakeWharton/timber), a utility library for logging and easy debugging
* [SearchableSpinner](https://github.com/miteshpithadiya/SearchableSpinner), a dialog spinner with a search feature which allows users to search the items loaded in the spinner
* [SDP/SSP](https://github.com/intuit/sdp) which is a scalable size unit that scales with the screen size. It helps to easily design for multiple screens
* [JUnit4](https://junit.org/junit4), a testing framework used for writing unit tests
* [MockWebServer](https://javadoc.io/doc/com.squareup.okhttp3/mockwebserver/3.14.9/overview-summary.html), a library that makes it easy to test how Apps behave when making HTTP/HTTPS calls
* [Mockito](https://site.mockito.org/), a mocking framework for writing unit tests

## Installation

You will need an App ID from [Open Exchange Rates API](https://openexchangerates.org). In your project's root directory, inside the `local.properties` file, include the following line:

````
app.id=YOUR_APP_ID
````