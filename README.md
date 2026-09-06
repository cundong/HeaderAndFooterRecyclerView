# HeaderAndFooterRecyclerView

**Legacy RecyclerView header/footer support and AndroidX migration examples.**

[简体中文](README_CN.md) · [Migration guide](docs/MIGRATING_TO_ANDROIDX.md) · [Development](CONTRIBUTING.md)

This project originally made it easier to add headers, footers and loading-state examples to RecyclerView. Today, it preserves that implementation as a buildable migration reference alongside an AndroidX example.

**For new projects, use AndroidX `ConcatAdapter` for headers and footers.** Add Paging when you need managed pagination. Existing integrations can migrate incrementally; this branch is not a drop-in update for the old Android Support library.

## Choose your path

| Your situation | Start here |
| --- | --- |
| Building a new RecyclerView screen | Run **ConcatAdapter (AndroidX)** in `samplePlus`; its page uses no API from this library. |
| Maintaining an existing integration | Read [compatibility changes](docs/MODERNIZATION.md), then the [migration guide](docs/MIGRATING_TO_ANDROIDX.md). |
| Looking for the original examples | See [legacy usage and screenshots](docs/LEGACY_USAGE.md). |
| Contributing or working with an AI agent | Read [CONTRIBUTING.md](CONTRIBUTING.md), [architecture](docs/ARCHITECTURE.md) and [AGENTS.md](AGENTS.md). |

## What replaces the original features?

| Need | Current AndroidX approach |
| --- | --- |
| Header + data + footer | `ConcatAdapter(headerAdapter, dataAdapter, footerAdapter)` |
| Data-row click positions | `getBindingAdapterPosition()`, checking `NO_POSITION` |
| Incremental list updates | `ListAdapter` / `DiffUtil` |
| Managed pagination and retry UI | Paging + `PagingDataAdapter` + `LoadStateAdapter` |
| Full-width grid / staggered-grid decorations | Explicit span lookup / full-span layout parameters |

`ConcatAdapter` handles adapter composition, notification offsets and view-type isolation; it does not choose layout spans. Paging migration also requires moving data requests and pagination state, not just replacing a scroll listener. See the official [ConcatAdapter reference](https://developer.android.com/reference/androidx/recyclerview/widget/ConcatAdapter) and [Paging loading-state guide](https://developer.android.com/topic/libraries/architecture/paging/load-state).

## Run the examples

Use JDK **17 or 21**, Android SDK **36**, and Build Tools **35.0.0**. Set `ANDROID_HOME` or configure an untracked `local.properties`; see [setup details](CONTRIBUTING.md).

```sh
sdkmanager "platforms;android-36" "build-tools;35.0.0" "platform-tools"
./gradlew testDebugUnitTest assembleDebug lintDebug --console=plain
```

Use `gradlew.bat` on Windows. First builds download dependencies. Tests run on the host without an emulator. The build pins Gradle 8.13, AGP 8.11.1, RecyclerView 1.4.0 and AppCompat 1.7.1; all modules require Android API **21+**.

| Module | Purpose |
| --- | --- |
| `library` | Legacy wrapper, position/span helpers and scroll callbacks; retained for migration and regression coverage. |
| `sample` | Minimal legacy header/footer example. |
| `samplePlus` | AndroidX grid example first, followed by legacy linear/grid/staggered paging demos. |

Open the root project in Android Studio and run `samplePlus`. In **ConcatAdapter (AndroidX)**, headers and footers span both columns; tapping an item reports its position within the data adapter. The two demo apps have different application IDs and can be installed together.

The modern page is [ConcatExampleActivity.java](samplePlus/src/main/java/com/cundong/recyclerview/sample/ConcatExampleActivity.java). `samplePlus` still depends on `library` for its other demos; copying the modern page does not require the legacy wrapper. This repository does not include a complete Paging or Compose app.

## Compatibility and maintenance

- This branch migrates `android.support` types to AndroidX and raises the minimum SDK from 14 to 21 for `library` and `sample`. Read [the upgrade notes](docs/MODERNIZATION.md) before changing a consumer.
- The original Support-era source remains in [Git history](https://github.com/cundong/HeaderAndFooterRecyclerView/tree/33860effcdfad7b62172f0235f358533a5235fa6). This branch does not publish or replace Maven artifacts.
- Work is focused on reproducible builds, confirmed defects, necessary compatibility fixes and migration documentation/examples. New general-purpose adapter features are outside the maintenance scope.
- Regression tests and CI cover documented paths, not every historical edge case. See the [maintenance policy](docs/MAINTENANCE.md) for known limits and the device smoke-test checklist in [CONTRIBUTING.md](CONTRIBUTING.md).

## History and license

Created by Cundong in 2015. The original implementation, examples, screenshots and Git history are preserved. Thanks to the original contributors and users who helped shape the project.

Copyright 2015 Cundong. Licensed under the [Apache License 2.0](LICENSE).
