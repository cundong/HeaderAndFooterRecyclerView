# Modernization and migration

## Build baseline

| Component | Before | Now |
| --- | --- | --- |
| Android Gradle Plugin | 2.3.1 | 8.11.1 |
| Gradle | 3.3 | 8.13, official wrapper with distribution SHA-256 |
| Build JDK | legacy | 17 (CI), 21 also supported |
| compileSdk / sample targetSdk | 25 | 36 |
| minSdk | library/sample 14, samplePlus 21 | 21 for all modules |
| UI dependencies | Support 25.3.1 | AndroidX RecyclerView 1.4.0 / AppCompat 1.7.1 |
| Repositories | JCenter | Google Maven / Maven Central |

This is a pinned compatible baseline, not a claim to use the newest release of every component. [AGP compatibility](https://developer.android.com/build/releases/agp-8-11-0-release-notes), [RecyclerView releases](https://developer.android.com/jetpack/androidx/releases/recyclerview), [AppCompat releases](https://developer.android.com/jetpack/androidx/releases/appcompat).

## Consumer changes

- AndroidX migration is source/binary incompatible with legacy `android.support` consumers. Migrate consumer imports and XML widget names, and rebuild against this branch.
- Minimum supported Android version is now API 21. Projects requiring API 14–20 must keep the legacy revision.
- The library package and existing public method names remain; RecyclerView is now exposed transitively via `api` instead of `provided`.
- The extended sample application ID is now `com.cundong.recyclerview.sample.plus`, allowing both demos to be installed together. Its Java namespace stays unchanged.
- No artifact has been published. For local use include `:library` as a project dependency; `./gradlew :library:assembleRelease` produces an AAR. When distributing a bare AAR, consumers must declare AndroidX RecyclerView themselves because a bare AAR does not carry Maven dependency metadata.

## Quality and collaboration

Added bilingual READMEs/migration guides, preserved historical usage/screenshots, agent guidance, architecture/development docs, a centralized version catalog, editor settings, CI, dependency update configuration, and host regression tests. IDE `.iml` files are no longer tracked. Lint errors fail the build. Sample layouts handle system-bar insets for target SDK 36.

The dependency catalog also pins a Kotlin BOM to align AndroidX transitive standard-library dependencies in this Java project.

A ConcatAdapter/ListAdapter grid demo provides a path away from the legacy wrapper; see [maintenance scope](MAINTENANCE.md).

Regression coverage also protects move/payload notification forwarding and invalid holder positions, correcting legacy behavior in those paths. Broader adapter redesigns (for example, replacing the wrapper with ConcatAdapter) and Kotlin/Compose rewrites are outside this migration.
