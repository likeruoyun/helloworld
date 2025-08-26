# Android Network List (RecyclerView + ConstraintLayout)

Simple sample app that displays a paged list of randomly generated repositories, simulating a network response.

- RecyclerView with `ConstraintLayout` item
- Pull to refresh via `SwipeRefreshLayout`
- FakeService with coroutines to generate data

## Run

Open the project `android-network-list` in Android Studio (Giraffe or newer) and click Run. Android Studio will create the Gradle wrapper automatically on sync.

Alternatively, if you have Gradle installed locally: `gradle :app:assembleDebug` and install the output APK.