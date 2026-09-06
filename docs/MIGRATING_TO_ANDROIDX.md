# Migrate from the wrapper to AndroidX

[简体中文](MIGRATING_TO_ANDROIDX_CN.md) · [README](../README.md)

Updating Support imports to AndroidX makes the old implementation build again. Removing the wrapper is a separate migration. Do one screen at a time, with regression coverage for the behavior that screen needs.

## 1. Compose adapters instead of wrapping Views

```java
ConcatAdapter combined = new ConcatAdapter(headerAdapter, dataAdapter, footerAdapter);
recyclerView.setAdapter(combined);
```

Create or inflate each header/footer View in a small single-row adapter. Do not reuse one View instance across RecyclerViews. To hide a section, update its item count and send the corresponding insert/remove notification, or remove its adapter with `combined.removeAdapter(footerAdapter)`. Keep default view-type isolation unless adapters deliberately share compatible holders.

See the complete [AndroidX grid example](../samplePlus/src/main/java/com/cundong/recyclerview/sample/ConcatExampleActivity.java). Its module retains `:library` for other demos; the example page only uses AndroidX APIs and the sample's system-bar inset base activity.

## 2. Remove manual position offsets

Inside a data holder's click handler:

```java
int position = holder.getBindingAdapterPosition();
if (position == RecyclerView.NO_POSITION) return;
// position is local to the data adapter. Do not subtract the header count.
```

Use `getAbsoluteAdapterPosition()` only when you need the position across the entire RecyclerView. Do not combine `RecyclerViewUtils` position translation with ConcatAdapter. For updating ordinary data, use `ListAdapter`/`DiffUtil` and submit new list snapshots; do not mutate a submitted list.

## 3. Preserve layout spans explicitly

For GridLayoutManager, use `combined.getWrappedAdapterAndPosition(position)` inside SpanSizeLookup to identify the row's adapter. Return spanCount for full-width decorations and the data row's actual span otherwise.

For StaggeredGridLayoutManager, the header/footer adapter can check its holder's layout parameters in `onViewAttachedToWindow()` and set `StaggeredGridLayoutManager.LayoutParams.setFullSpan(true)`. ConcatAdapter does not set layout spans. The runnable modern example covers the grid case; the legacy staggered example is not evidence of a completed ConcatAdapter staggered migration.

## 4. Move pagination only when needed

A static list or simple manual pagination may not need Paging. For managed paging, move requests and page keys into PagingSource/Pager, present data with PagingDataAdapter, and use LoadStateAdapter with `withLoadStateFooter()` or `withLoadStateHeaderAndFooter()`. Retry via `PagingDataAdapter.retry()` and design initial empty/loading/error presentation separately from append failures.

This requires migrating data ownership, cancellation and lifecycle handling, not just replacing OnScrollListener. The repository's simulated legacy requests remain examples; this branch does not include a full Paging implementation.

## 5. Verify behavior, then remove the dependency

Check dynamic header/footer changes, local click positions, inserts/removals/moves, payloads, grid/staggered spans, retry/end states, rotation and scroll restoration. After all call sites have migrated, remove `:library` or the old dependency and obsolete RecyclerViewUtils/HeaderSpanSizeLookup/scroll listener usages.

References: [ConcatAdapter](https://developer.android.com/reference/androidx/recyclerview/widget/ConcatAdapter), [Paging loading states](https://developer.android.com/topic/libraries/architecture/paging/load-state).
