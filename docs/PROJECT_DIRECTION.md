# 项目在今天的定位

## 判断

不建议新项目为 Header/Footer 引入这个库。它曾经补齐 RecyclerView 的常见缺口，现在更适合作为遗留代码的可构建参考和迁移案例。工程能构建，并不等于仍有必要维护一套独立的 Adapter 组合实现。

这一判断基于功能与官方 API 的比较，不代表已调查所有下游用户。没有实际使用方数据，不能认定所有旧项目都应该立刻删除依赖，也不应自动归档或发布破坏性升级。

| 原能力 | 今天的选择 | 边界 |
| --- | --- | --- |
| Header + 数据 + Footer | AndroidX ConcatAdapter | 每个部分写一个小 Adapter；无需单独的 Header/Footer 库 |
| 位置偏移与 viewType 隔离 | ConcatAdapter + getBindingAdapterPosition() | 整个列表的位置用 getAbsoluteAdapterPosition()；始终检查 NO_POSITION |
| 增量数据刷新 | ListAdapter / DiffUtil | 提交新的列表快照，不原地修改已提交列表 |
| 分页、加载中、错误重试 | Paging 3 + PagingDataAdapter + LoadStateAdapter | 静态列表或简单的手动分页不必为了页脚引入 Paging |
| Grid / StaggeredGrid 中 Header 占满宽度 | SpanSizeLookup / LayoutParams.setFullSpan | ConcatAdapter 不自动决定跨度，仍需布局适配 |
| 已经采用 Compose 的界面 | LazyColumn / LazyVerticalGrid | 不建议仅为替换此库而重写整页 UI |

## 值得继续做的事

1. 让旧实现可构建、可测试，帮助使用者理解旧行为和迁移风险。
2. 给出不用本库也能实现相同需求的示例，并对照位置、跨度、通知和状态恢复语义。
3. 为明确存在的下游需求按已确认的需求提供兼容修复。只有发现官方方案无法满足且可验证的需求，才考虑新增抽象。

这次分支完成的是工程基础与迁移入口，不承诺已解决所有历史算法和生命周期问题。不要把 AndroidX 迁移误称为对旧 Support 使用方的无缝升级。

## 迁移入口

`samplePlus` 菜单中的 **ConcatAdapter (AndroidX)** 是不调用本库 API 的原生 AndroidX 网格示例，展示 Header、ListAdapter 数据、Footer 和局部点击位置。示例所在模块为了保留其他旧演示仍依赖 `:library`，迁移该页面本身只需 AndroidX RecyclerView、AppCompat 及系统栏适配。

详见 [迁移步骤](MIGRATING_TO_ANDROIDX_CN.md)。

## 依据

- [ConcatAdapter：通知偏移、类型隔离与局部位置](https://developer.android.com/reference/androidx/recyclerview/widget/ConcatAdapter)
- [Paging：加载与展示分页数据](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data)
- [Paging：加载状态与 Header/Footer](https://developer.android.com/topic/libraries/architecture/paging/load-state)
- [Compose 列表与网格](https://developer.android.com/develop/ui/compose/lists)
