# Maintenance scope and known limits

[README](../README.md) · [迁移指南](MIGRATING_TO_ANDROIDX_CN.md)

This repository preserves the original library for existing integrations and explains how to migrate to native AndroidX. Maintenance is focused on reproducible builds, confirmed defects, necessary compatibility fixes and migration documentation/examples. It is not a roadmap for a new general-purpose adapter framework. There is no promised release schedule or support period.

## Scope of this branch

- Java, the library package and the three modules are retained.
- AndroidX migration is source/binary incompatible with old Support consumers. API 14–20 support is not retained in this branch.
- No new Maven release, repository rename or archival is part of this change.
- The modern demo uses ConcatAdapter and ListAdapter for one grid screen. Paging and Compose are migration options documented through official sources, not implemented features here.

## Legacy limits

The wrapper is not a drop-in equivalent of ConcatAdapter. Its view-type arithmetic and header/footer classification were designed around the original simple demos; multiple decorations and arbitrary inner view types are not guaranteed. The no-argument wrapper requires a non-null inner adapter before use. Lifecycle forwarding, stable IDs and nested adapter/state-restoration semantics have not been comprehensively modernized. The staggered layout manager retains its historical measurement implementation.

Current library tests cover a single header/footer, data binding, grid spans, observer replacement, insert/remove/change/move offsets, payload forwarding and NO_POSITION. Sample tests cover the AndroidX page's actual layout/click behavior, recreation and inset handling. Passing these tests does not establish coverage of all legacy edge cases or device-specific UI behavior.

## Contributions

For a bug report, include the revision, affected module, build JDK/Android API, layout manager, minimal adapter setup, reproduction steps and expected/actual behavior. A regression test is especially useful. Avoid including private app data or credentials.

Changes should fix an observed problem or make migration easier. Keep the migration guide and compatibility notes aligned with any behavior change. Do not disable lint errors or silently broaden compatibility claims to make a build pass. Run the verification command and device checks described in [CONTRIBUTING.md](../CONTRIBUTING.md); state which checks were actually run.

Archival remains a separate future decision based on actual downstream and maintenance needs. This change preserves the history and legacy implementation while providing a migration path.
