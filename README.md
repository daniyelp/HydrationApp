Hydration App
============
 An app that helps you track your daily water intake. Implemented as a part of Garmin's [mobile challenge](https://github.com/garminmobilechallenge/hydrationapp_specs/blob/main/flow%26screens.pdf).

https://user-images.githubusercontent.com/84658876/151904578-1967ada9-649f-45f8-a0af-9cc282b050cf.mp4

## Libraries used
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android): A dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
- [DataStore](https://github.com/coil-kt/coil): Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
- [Material Motion for Jetpack Compose](https://github.com/fornewid/material-motion-compose): Jetpack Compose library for implementing [motion system](https://material.io/develop/android/theming/motion/) in Material Components for Android.

## Known issues
#### History screen
- The chart looks ugly for the cases in which the size of the input data (the number of elements) is very small
#### Today screen
- The screen data isn't loaded all at once
- The glass is filled taking into consideration its height and not its area
#### Daily goal / Container screen
- The text field doesn't scale with the input's width
- The text field doesn't have input validation
- The enter and exit transitions are somehow sketchy because of the keyboard's revealing/hiding
#### Preferences
- Some portion is lost in the convertion between milliliters and ounces. This causes inconsistency in the app (e.g. when having milliliters selected, the today screen may show 50%, but in the case we would have ounces selected, it would show 49%)
#### Storage
- A fake repository implementation is used for the `DayProgressRepository` instead of an implementation with `Room`. The app behaves correctly durring the user's session, but his progress is lost once he leaves the app

