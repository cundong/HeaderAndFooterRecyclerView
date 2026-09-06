# 从本库迁移到原生 AndroidX

[English](MIGRATING_TO_ANDROIDX.md) · [项目首页](../README_CN.md)

本分支的 AndroidX 包迁移只是构建迁移。下面是进一步移除本库依赖的路线，二者不同。

## 1. 用 Adapter 组合代替 View 包装

```java
ConcatAdapter combined = new ConcatAdapter(headerAdapter, dataAdapter, footerAdapter);
recyclerView.setAdapter(combined);
```

Header/Footer 各自是一个单行 Adapter，在 `onCreateViewHolder` 中创建或 inflate View；避免跨 RecyclerView 重用同一个 View 实例。需要隐藏时让该 Adapter 的条目数变为 0 并通知插入/移除，或调用 `combined.removeAdapter(footerAdapter)`。默认保留 ConcatAdapter 的 viewType 隔离。

完整可编译示例见 `samplePlus/src/main/java/com/cundong/recyclerview/sample/ConcatExampleActivity.java`。

## 2. 删除手动位置偏移

在数据 Adapter 的 ViewHolder 点击回调里使用：

```java
int position = holder.getBindingAdapterPosition();
if (position == RecyclerView.NO_POSITION) return;
// position 已是数据 Adapter 的局部位置，不要再减 Header 数。
```

需要整个 RecyclerView 的位置时用 `getAbsoluteAdapterPosition()`。不要把旧 `RecyclerViewUtils` 的位置转换与 ConcatAdapter 叠加使用。

## 3. 明确跨度

GridLayoutManager 的 SpanSizeLookup 可以用 `combined.getWrappedAdapterAndPosition(position)` 判断这一行属于哪个 Adapter，Header/Footer 返回 spanCount，数据行返回其实际跨度。

StaggeredGridLayoutManager 的 Header/Footer Adapter 应在 ViewHolder 附着时检查 LayoutParams 并调用 `setFullSpan(true)`。这项布局策略仍需实现；ConcatAdapter 不会自动替你决定。

## 4. 按需求迁移分页

普通列表直接使用 ListAdapter 和 DiffUtil。需要受管理的分页加载时，使用 PagingSource/Pager、PagingDataAdapter 和 LoadStateAdapter，并通过 `withLoadStateFooter()` 或 `withLoadStateHeaderAndFooter()` 组合加载状态。重试调用 `PagingDataAdapter.retry()`，以 LoadState 区分加载、错误与完成；初始空列表与分页追加错误应分别设计展示。

这不是简单替换一个 OnScrollListener：需要把数据请求、分页键和生命周期一并迁移。旧示例的模拟请求并未在本分支伪装成完整的 Paging 实现。

## 5. 验收后移除旧依赖

验证 Header/Footer 动态增删、点击局部位置、增量更新与移动、grid/staggered 跨度、分页重试/结束、旋转和滚动位置恢复。所有调用点迁移完成后，移除 `:library` 或旧 Maven 依赖，以及不再使用的 RecyclerViewUtils、HeaderSpanSizeLookup 和滚动监听器。

参考：[ConcatAdapter](https://developer.android.com/reference/androidx/recyclerview/widget/ConcatAdapter)、[Paging 加载状态](https://developer.android.com/topic/libraries/architecture/paging/load-state)。
