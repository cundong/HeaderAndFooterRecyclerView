# Development

Install JDK 17 (CI) or JDK 21 and Android SDK command-line tools. Configure `ANDROID_HOME` or an untracked `local.properties` with `sdk.dir=/your/android/sdk`.

```sh
sdkmanager "platforms;android-36" "build-tools;35.0.0" "platform-tools"
./gradlew testDebugUnitTest assembleDebug lintDebug --console=plain
git diff --check
```

On Windows use `gradlew.bat`. First builds need access to Google Maven, Maven Central and Gradle distribution servers; Robolectric also downloads an Android runtime from Maven Central. Unit tests run on the host without an emulator. Java source/bytecode level is 17; Android API availability is still constrained by minSdk 21.

Test reports are under `library/build/reports/tests/testDebugUnitTest/` and `samplePlus/build/reports/tests/testDebugUnitTest/` and each module's `build/reports/lint-results-debug.html`. Debug APKs are under `sample/build/outputs/apk/debug/` and `samplePlus/build/outputs/apk/debug/`; the AAR is under `library/build/outputs/aar/`.

For a quick library-only iteration run `./gradlew :library:testDebugUnitTest`. Before sending a change run the full command above. CI runs both test suites, builds both examples and checks lint in all three modules.

The maintenance scope and known legacy limits are recorded in [MAINTENANCE.md](docs/MAINTENANCE.md).

For runtime verification, install both examples on an API 21 device/emulator and an API 36 device/emulator. Exercise header/footer placement, linear/grid/staggered layouts, pagination, retry/end states, rotation, and system-bar insets. Host tests do not replace these visual checks.

Keep public API changes explicit, test regressions, and describe behavior plus validation in pull requests. See [architecture](docs/ARCHITECTURE.md), [migration notes](docs/MODERNIZATION.md), and [agent guidance](AGENTS.md).
