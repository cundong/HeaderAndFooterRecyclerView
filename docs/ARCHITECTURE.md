# Architecture

`sample` and `samplePlus` depend on `library`. The library exports AndroidX RecyclerView through an `api` dependency because its public types include RecyclerView adapters, holders and layout managers. AppCompat is confined to the sample apps.

`HeaderAndFooterRecyclerViewAdapter` wraps a data adapter. Its visible rows are headers, data rows, then footers. Data binding and notifications must translate inner positions by the current header count. Header/footer rows are actual View instances, not data models.

`RecyclerViewUtils` attaches/removes the first header/footer and translates holder positions into data positions. An unattached or invalid holder must keep `RecyclerView.NO_POSITION`.

`HeaderSpanSizeLookup` expands header/footer rows across a grid. `ExStaggeredGridLayoutManager` is the legacy staggered-grid extension; check it together with the adapter's full-span handling when changing layouts.

`EndlessRecyclerOnScrollListener` detects scroll-to-end for supported layout managers and calls `OnListLoadNextPageListener`. It does not fetch data or own pagination state. `samplePlus` owns loading/retry/end state and simulated data loading through `LoadingFooter` and `RecyclerViewStateUtils`.

Library regression tests use Robolectric at API 28 for deterministic host coverage. They exercise wrapping and notification contracts. They do not prove every layout, lifecycle, multiple-header/view-type combination, or newest-platform behavior; use the manual smoke checklist in CONTRIBUTING.md for UI changes.

`ConcatExampleActivity` is the native AndroidX grid example in `samplePlus`: independent header/data/footer ListAdapters combined by ConcatAdapter, with explicit grid spans and binding-adapter click positions. It does not call the legacy library. `BaseSampleActivity` applies system-bar/cutout insets for target SDK 36; the tiny base is duplicated across the independent sample apps to keep UI scaffolding out of the library.

`samplePlus/src/test/` exercises the real modern page under Robolectric at API 35, including layout/click position contracts and activity recreation. These are host UI tests, not device/emulator instrumentation tests.

The legacy wrapper assumes the simple header/footer and view-type patterns in its demos. Multiple decorations, arbitrary view types, stable IDs and nested adapter lifecycle/state restoration are not fully supported or covered by tests. Its no-argument constructor requires setting a non-null inner adapter before use.
