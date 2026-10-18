# StudentIDAssignment2

This project implements the NIT3213 final assignment Android app with a login screen, dashboard list, and details screen. It calls the provided API endpoints for authentication and dashboard data, uses Koin for dependency injection, and includes unit tests for the critical ViewModels.

## Features
- Login using student ID and first name with location-based auth route selection
- Dashboard screen using RecyclerView to display entity summaries
- Details screen displaying the full selected entity payload
- Retrofit + Gson API integration
- Koin dependency injection
- ViewModel-based state handling with unit tests

## Project structure
- `app/src/main/java/com/example/studentidassignment2` – application code
- `app/src/main/res` – layouts and resources
- `app/src/test/java/com/example/studentidassignment2` – unit tests

## Prerequisites
- Android Studio with Android SDK
- JDK 17
- Gradle wrapper provided in the project

## Build and run
1. Open the project in Android Studio.
2. Ensure the Android SDK is installed and configured.
3. Set the Java home to JDK 17 if prompted.
4. Sync the project and run the `app` configuration on an emulator or Android device.

## Command line build
```bash
cd StudentIDAssignment2
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

## API usage
The app uses the following base URL:
`https://nit3213apinew.onrender.com`

Authentication uses one of these routes based on location:
- `footscray/auth`
- `sydney/auth`
- `br/auth`

Login sends `username` and `password` as a JSON request body.

The dashboard route is:
`/dashboard/{keypass}`

## Notes
- The login password must match the first name exactly, including case.
- The dashboard data is shown in a RecyclerView and each item opens the details view.
