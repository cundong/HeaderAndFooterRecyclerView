# HeaderAndFooterRecyclerView

**RecyclerView Header/Footer 历史实现与 AndroidX 迁移示例。**

[English](README.md) · [迁移指南](docs/MIGRATING_TO_ANDROIDX_CN.md) · [开发指南](CONTRIBUTING.md)

这个项目最初用于给 RecyclerView 添加 Header、Footer，并演示分页加载状态。今天，仓库保留可构建的旧实现，同时提供 AndroidX 对照示例，帮助已有项目逐步迁移。

**新项目建议直接使用 AndroidX `ConcatAdapter` 实现 Header/Footer。** 需要完整的分页管理时再引入 Paging。已有项目可以逐步替换；本分支不是旧 Android Support 版本的无缝升级。

## 从这里开始

| 你的情况 | 推荐入口 |
| --- | --- |
| 正在开发新的 RecyclerView 页面 | 运行 `samplePlus` 中的 **ConcatAdapter (AndroidX)**，该页面不调用本库 API。 |
| 正在维护已经接入本库的项目 | 先读[兼容性变化](docs/MODERNIZATION.md)，再按[迁移指南](docs/MIGRATING_TO_ANDROIDX_CN.md)逐步替换。 |
| 查找当年的用法和截图 | 查看[历史用法与截图](docs/LEGACY_USAGE_CN.md)。 |
| 准备贡献代码，或使用 AI 协作 | 阅读[开发指南](CONTRIBUTING.md)、[架构说明](docs/ARCHITECTURE.md)和 [AGENTS.md](AGENTS.md)。 |

## 今天如何实现这些需求？

| 需求 | 推荐方案 |
| --- | --- |
| Header + 数据 + Footer | `ConcatAdapter(headerAdapter, dataAdapter, footerAdapter)` |
| 点击事件中的数据位置 | `getBindingAdapterPosition()`，并检查 `NO_POSITION` |
| 列表增量刷新 | `ListAdapter` / `DiffUtil` |
| 分页管理与错误重试 | Paging + `PagingDataAdapter` + `LoadStateAdapter` |
| 网格、瀑布流中占满宽度的 Header/Footer | 显式配置跨度或 full-span 布局参数 |

`ConcatAdapter` 负责组合 Adapter、转发通知偏移和隔离 viewType，但不会替你决定布局跨度。迁移到 Paging 还需要整理请求和分页状态，不能只替换滚动监听器。参见官方 [ConcatAdapter 文档](https://developer.android.com/reference/androidx/recyclerview/widget/ConcatAdapter)和 [Paging 加载状态指南](https://developer.android.com/topic/libraries/architecture/paging/load-state)。

## 运行示例

准备 JDK **17 或 21**、Android SDK **36** 和 Build Tools **35.0.0**。设置 `ANDROID_HOME` 或在不提交到 Git 的 `local.properties` 中配置 SDK 路径，详见[环境配置](CONTRIBUTING.md)。

```sh
sdkmanager "platforms;android-36" "build-tools;35.0.0" "platform-tools"
./gradlew testDebugUnitTest assembleDebug lintDebug --console=plain
```

Windows 使用 `gradlew.bat`。首次构建需要下载依赖，单元测试在本机运行，无需模拟器。工程固定使用 Gradle 8.13、AGP 8.11.1、RecyclerView 1.4.0 和 AppCompat 1.7.1，所有模块最低支持 Android **API 21**。

| 模块 | 用途 |
| --- | --- |
| `library` | 保留旧 Adapter 包装器、位置/跨度辅助方法和滚动回调，用于迁移与回归验证。 |
| `sample` | 最小的旧版 Header/Footer 示例。 |
| `samplePlus` | 首项为 AndroidX 网格示例，其后为旧版线性、网格和瀑布流分页演示。 |

用 Android Studio 打开仓库根目录，运行 `samplePlus`。进入 **ConcatAdapter (AndroidX)** 后，可以看到跨两列的 Header/Footer；点击数据项会显示其在数据 Adapter 内的位置。两个示例使用不同的应用 ID，可以同时安装。

现代示例位于 [ConcatExampleActivity.java](samplePlus/src/main/java/com/cundong/recyclerview/sample/ConcatExampleActivity.java)。`samplePlus` 为保留其他旧演示仍依赖 `library`，复制现代页面本身不需要旧包装器。仓库没有提供完整的 Paging 或 Compose 应用。

## 兼容性与维护范围

- 本分支将 `android.support` 迁移到 AndroidX，并将 `library` 和 `sample` 的最低 SDK 从 14 提高到 21。升级使用方前请读[改造与兼容性说明](docs/MODERNIZATION.md)。
- 原始 Support 版本保留在 [Git 历史](https://github.com/cundong/HeaderAndFooterRecyclerView/tree/33860effcdfad7b62172f0235f358533a5235fa6)中。本分支不发布或替换 Maven 制品。
- 维护重点是可复现构建、已确认的缺陷、必要的兼容修复，以及迁移文档和示例；不再扩张通用 Adapter 功能。
- 测试和 CI 覆盖已记录的场景，并不代表所有历史边界问题都已解决。参见[维护范围与已知限制](docs/MAINTENANCE.md)，设备验收清单见[开发指南](CONTRIBUTING.md)。

关于这些取舍，另见[项目定位](docs/PROJECT_DIRECTION.md)。
