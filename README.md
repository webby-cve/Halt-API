# S8138683Assignment2 — NIT3213 Final Assignment

Android application developed for **NIT3213 (Android Application Development)**.
It authenticates against the `nit3213api` and displays a list of entities
retrieved from the dashboard endpoint.

**Student ID:** S8138683  |  **Name:** Arpan Ghimire  |  **Campus:** Sydney

---

## Architecture

The app follows the standard **Android App Architecture** (MVVM +
Repository pattern) with **Hilt** dependency injection:

```
┌─────────────────────────────────────────────────────────────────┐
│ UI Layer         MainActivity (single activity)                  │
│                  ├─ LoginFragment     (UI)                       │
│                  ├─ DashboardFragment (UI)  ── RecyclerView      │
│                  └─ DetailsFragment   (UI)                       │
├─────────────────────────────────────────────────────────────────┤
│ ViewModel Layer  LoginViewModel ── one ViewModel per Fragment    │
│                  DashboardViewModel    (StateFlow UI states)     │
│                  DetailsViewModel                                │
├─────────────────────────────────────────────────────────────────┤
│ Data Layer       AuthRepository  ── handles data operations,     │
│ (Repository)     DashboardRepository  error mapping, single      │
│                                     source of truth              │
├─────────────────────────────────────────────────────────────────┤
│ Network Layer    ApiService (Retrofit interface) ──→  Server     │
│                  /sydney/auth, /dashboard/{keypass}   nit3213api │
└─────────────────────────────────────────────────────────────────┘
       All layers wired together with Hilt (DI)
```

Data flow: **Fragment → ViewModel → Repository → ApiService → Server**,
with the response flowing back up: **Server → ApiService → Repository →
ViewModel → Fragment**.

## Features

- **Login screen** — username/password form that authenticates against the
  API and shows clear error messages for failed attempts.
- **Dashboard screen** — RecyclerView of Material cards showing a summary
  of every entity (all properties *except* the description), colored
  initial avatars, topic header and `entityTotal` summary card.
- **Details screen** — tap any item to see *all* of its properties and the
  full description, presented in a hero card + property cards layout.
- **Polish** — gradient headers, slide/alpha navigation animations, ripple
  feedback on cards.

## Tech Stack

| Area | Choice |
|---|---|
| Language | Kotlin |
| Architecture | MVVM: one ViewModel per Fragment + Repository pattern |
| Dependency Injection | **Hilt** (Dagger) |
| Networking | Retrofit + Gson + OkHttp |
| UI | XML layouts + View Binding, Navigation Component, RecyclerView |
| Async | Kotlin Coroutines + StateFlow |
| Unit tests | JUnit4 + MockK + kotlinx-coroutines-test |

## Project Structure

```
com.s8138683.assignment2
├── Nit3213App                 @HiltAndroidApp application class
├── di/
│   └── NetworkModule          Hilt module: OkHttp / Retrofit / ApiService
├── data/
│   ├── model/                 LoginRequest/Response, DashboardResponse, EntityItem
│   ├── remote/ApiService      Retrofit interface (sydney/auth, dashboard/{keypass})
│   └── repository/            AuthRepository, DashboardRepository (error mapping)
└── ui/
    ├── MainActivity           single-activity host with NavHostFragment
    ├── login/                 LoginFragment + LoginViewModel + LoginUiState
    ├── dashboard/             DashboardFragment + DashboardViewModel + EntityAdapter
    └── details/               DetailsFragment + DetailsViewModel
```

## How the API is used

| Step | Request | Notes |
|---|---|---|
| 1. Login | `POST https://nit3213apinew.onrender.com/sydney/auth` | Body: `{"username":"<studentID>","password":"<firstName>"}` → returns `{"keypass":"..."}` |
| 2. Dashboard | `GET https://nit3213apinew.onrender.com/dashboard/{keypass}` | Returns `{"entities":[...], "entityTotal":7}` |

- The `keypass` from login is passed to the dashboard fragment as a
  **navigation argument**.
- The selected entity is passed to the details fragment as a
  **Parcelable** navigation argument, read by `DetailsViewModel` via
  `SavedStateHandle`.
- Entities have a dynamic schema, so `description` is parsed separately and
  every remaining property is rendered generically as a key/value pair.

## Login Credentials

| Field | Value |
|---|---|
| Username | `8138683` (student ID, without the "s") |
| Password | `Arpan` (first name — **case sensitive**) |

> These are entered in the app's login screen; they are **not** hard-coded.

## How to Build and Run

1. Clone this repository.
2. Open the project folder in **Android Studio** (Hedgehog or newer
   recommended). Android Studio will sync Gradle and, if the wrapper JAR is
   missing, generate it automatically — or run `gradle wrapper` once.
3. Wait for the Gradle sync to finish (downloads Hilt, Retrofit, etc.).
4. Run on an **emulator or device with API 26+** (minSdk 26).
5. Enter the credentials above and tap **Sign In**.

No API keys, signing configs, or extra setup steps are required — the API
base URL is built in.

## Running the Unit Tests

```bash
./gradlew test            # from the project root (Linux/macOS)
gradlew.bat test          # on Windows
```

Or in Android Studio: right-click the `app/src/test` folder → **Run All Tests**.

**Test suite (6 files, 15 tests):**

| Test file | What it covers |
|---|---|
| `LoginViewModelTest` | Empty-field validation, login success → keypass, login failure → error message |
| `DashboardViewModelTest` | Load success with entities/total, load failure, repository called with keypass |
| `DetailsViewModelTest` | Entity exposed from nav args, missing-arg guard |
| `AuthRepositoryTest` | Success mapping, blank keypass, HTTP 401 → friendly message, network error |
| `DashboardRepositoryTest` | Entity mapping + `entityTotal`, HTTP error mapping, network error |
| `EntityItemMapperTest` | Description extraction/exclusion, null description fallback, null fields skipped |

## Dependency Injection (Hilt)

Hilt is used throughout:

- `@HiltAndroidApp` on `Nit3213App`, `@AndroidEntryPoint` on the Activity
  and Fragments, `@HiltViewModel` on every ViewModel (including
  `SavedStateHandle` injection in `DetailsViewModel`).
- `NetworkModule` (`@Module @InstallIn(SingletonComponent::class)`) provides
  the singleton `OkHttpClient`, `Retrofit` and `ApiService`.
- Repositories are constructor-injected `@Singleton`s, which is also what
  makes the ViewModels and repositories unit-testable with mocks.

## Git

The project is version-controlled with Git. Suggested meaningful commit
sequence:

```bash
git init
git add . && git commit -m "Initial project setup: Gradle config and app manifest"
git commit -m "Add Hilt DI module and Retrofit API service for nit3213api"
git commit -m "Implement login screen with API authentication and error handling"
git commit -m "Implement dashboard screen with RecyclerView and entity cards"
git commit -m "Implement details screen with ViewModel and entity properties"
git commit -m "Add unit tests for ViewModels, repositories and JSON mapping"
git commit -m "Polish UI: gradient headers, avatars, navigation animations"
git commit -m "Add README with build, run and test instructions"
```

## Notes

- HTTP request/response bodies are logged in **debug builds only**
  (`BuildConfig.DEBUG`) to help with development; logging is disabled in
  release builds.
