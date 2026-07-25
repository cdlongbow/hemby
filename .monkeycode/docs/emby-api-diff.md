# Emby vs Jellyfin API 差异清单

> 对比来源：Emby DEV API 文档 (https://dev.emby.media) vs Jellyfin SDK v1.8.11

## 一、基础路径差异（最关键）

| 项目 | Emby | Jellyfin |
|------|------|----------|
| Base URL 前缀 | `/emby` | 无前缀 |
| 完整示例 | `http://host:8096/emby/Users/AuthenticateByName` | `http://host:8096/Users/AuthenticateByName` |

**影响**：所有 API 请求都需要在路径前加 `/emby`。Jellyfin SDK 构造 URL 时不加此前缀，直接连 Emby 服务器会报 404。

## 二、认证头格式

| 项目 | Emby | Jellyfin SDK |
|------|------|-------------|
| Header 名 | `X-Emby-Authorization` | `X-Emby-Authorization` |
| 格式 | `MediaBrowser Client="...", Device="...", DeviceId="...", Version="...", Token="..."` | 完全相同 |

**结论**：格式兼容，无需修改。

## 三、核心 API 端点路径对比

### 3.1 用户认证

| 操作 | Emby 路径 | Jellyfin SDK 路径 | 兼容性 |
|------|----------|-----------------|--------|
| 密码登录 | `POST /emby/Users/AuthenticateByName` | `POST /Users/AuthenticateByName` | 仅差前缀 |
| QuickConnect | `POST /emby/QuickConnect/Initiate` | `POST /QuickConnect/Initiate` | 仅差前缀 |
| 获取公开用户 | `GET /emby/Users/Public` | `GET /Users/Public` | 仅差前缀 |

### 3.2 条目浏览

| 操作 | Emby 路径 | Jellyfin SDK 路径 | 兼容性 |
|------|----------|-----------------|--------|
| 查询条目 | `GET /emby/Users/{UserId}/Items` | `GET /Users/{UserId}/Items` | 仅差前缀 |
| 获取条目详情 | `GET /emby/Users/{UserId}/Items/{Id}` | `GET /Users/{UserId}/Items/{Id}` | 仅差前缀 |
| 获取续播 | `GET /emby/Users/{UserId}/Items/Resume` | `GET /Users/{UserId}/Items/Resume` | 仅差前缀 |
| 获取最新媒体 | `GET /emby/Users/{UserId}/Items/Latest` | `GET /Users/{UserId}/Items/Latest` | 仅差前缀 |
| 获取用户视图 | `GET /emby/Users/{UserId}/Views` | `GET /Users/{UserId}/Views` | 仅差前缀 |

### 3.3 剧集

| 操作 | Emby 路径 | Jellyfin SDK 路径 | 兼容性 |
|------|----------|-----------------|--------|
| 获取剧集 | `GET /emby/Shows/{Id}/Episodes` | `GET /Shows/{Id}/Episodes` | 仅差前缀 |
| 获取季 | `GET /emby/Shows/{Id}/Seasons` | `GET /Shows/{Id}/Seasons` | 仅差前缀 |
| 下一集 | `GET /emby/Shows/NextUp` | `GET /Shows/NextUp` | 仅差前缀 |
| 即将播出 | `GET /emby/Shows/Upcoming` | `GET /Shows/Upcoming` | 仅差前缀 |

### 3.4 直播电视

| 操作 | Emby 路径 | Jellyfin SDK 路径 | 兼容性 |
|------|----------|-----------------|--------|
| 获取频道 | `GET /emby/LiveTv/Channels` | `GET /LiveTv/Channels` | 仅差前缀 |
| 获取节目 | `GET /emby/LiveTv/Programs` | `GET /LiveTv/Programs` | 仅差前缀 |
| 推荐节目 | `GET /emby/LiveTv/Programs/Recommended` | `GET /LiveTv/Programs/Recommended` | 仅差前缀 |
| 获取录制 | `GET /emby/LiveTv/Recordings` | `GET /LiveTv/Recordings` | 仅差前缀 |
| 获取定时器 | `GET /emby/LiveTv/Timers` | `GET /LiveTv/Timers` | 仅差前缀 |
| 系列定时器 | `GET /emby/LiveTv/SeriesTimers` | `GET /LiveTv/SeriesTimers` | 仅差前缀 |
| 默认定时器 | `GET /emby/LiveTv/Timers/Defaults` | `GET /LiveTv/Timers/Defaults` | 仅差前缀 |
| 创建定时器 | `POST /emby/LiveTv/Timers` | `POST /LiveTv/Timers` | 仅差前缀 |
| 创建系列定时器 | `POST /emby/LiveTv/SeriesTimers` | `POST /LiveTv/SeriesTimers` | 仅差前缀 |
| 取消定时器 | `POST /emby/LiveTv/Timers/{Id}/Delete` | `POST /LiveTv/Timers/{Id}/Delete` | 仅差前缀 |

### 3.5 播放状态上报

| 操作 | Emby 路径 | Jellyfin SDK 路径 | 兼容性 |
|------|----------|-----------------|--------|
| 播放开始 | `POST /emby/Sessions/Playing` | `POST /Sessions/Playing` | 仅差前缀 |
| 播放进度 | `POST /emby/Sessions/Playing/Progress` | `POST /Sessions/Playing/Progress` | 仅差前缀 |
| 播放停止 | `POST /emby/Sessions/Playing/Stopped` | `POST /Sessions/Playing/Stopped` | 仅差前缀 |
| 标记已播 | `POST /emby/Users/{UserId}/PlayedItems/{Id}` | `POST /Users/{UserId}/PlayedItems/{Id}` | 仅差前缀 |

### 3.6 图片

| 操作 | Emby 路径 | Jellyfin SDK 路径 | 兼容性 |
|------|----------|-----------------|--------|
| 获取图片 | `GET /emby/Items/{Id}/Images/{Type}` | `GET /Items/{Id}/Images/{Type}` | 仅差前缀 |
| 用户头像 | `GET /emby/Users/{Id}/Images/{Type}` | `GET /Users/{Id}/Images/{Type}` | 仅差前缀 |

### 3.7 系统

| 操作 | Emby 路径 | Jellyfin SDK 路径 | 兼容性 |
|------|----------|-----------------|--------|
| 公开系统信息 | `GET /emby/System/Info/Public` | `GET /System/Info/Public` | 仅差前缀 |
| 系统信息 | `GET /emby/System/Info` | `GET /System/Info` | 仅差前缀 |

### 3.8 播放信息

| 操作 | Emby 路径 | Jellyfin SDK 路径 | 兼容性 |
|------|----------|-----------------|--------|
| 获取播放信息 | `POST /emby/Items/{Id}/PlaybackInfo` | `POST /Items/{Id}/PlaybackInfo` | 仅差前缀 |

## 四、响应模型差异

### 4.1 BaseItemDto 字段

| 字段 | Jellyfin SDK 期望 | Emby 实际返回 | 风险 |
|------|-----------------|-------------|------|
| `Type` | 枚举 `BaseItemKind` | 字符串（如 `"Movie"`） | SDK 自动反序列化，类型名相同 |
| `isMovie` | 布尔字段 | 可能不存在（Emby 用 `Type == "Movie"`） | **中风险** |
| `isSeries` | 布尔字段 | 可能不存在 | **中风险** |
| `Chapters` | `List<ChapterInfo>` | 格式可能不同 | **低风险** |
| `MediaSources` | `List<MediaSourceInfo>` | 格式可能不同 | **低风险** |
| `UserData` | `UserItemDataDto` | 格式可能不同 | **低风险** |
| `SeriesId` | UUID | UUID | 兼容 |
| `SeasonId` | UUID | UUID | 兼容 |
| `IndexNumber` | Int | Int | 兼容 |
| `ParentIndexNumber` | Int | Int | 兼容 |

### 4.2 认证响应

| 字段 | Jellyfin SDK 期望 | Emby 实际返回 | 风险 |
|------|-----------------|-------------|------|
| `AccessToken` | 字符串 | `AccessToken` | 兼容 |
| `User` | `UserDto` | `User` | 兼容 |

### 4.3 播放信息响应

| 字段 | Jellyfin SDK 期望 | Emby 实际返回 | 风险 |
|------|-----------------|-------------|------|
| `MediaSources` | `List<MediaSourceInfo>` | `MediaSources` | 兼容 |
| `PlaySessionId` | 字符串 | `PlaySessionId` | 兼容 |

## 五、服务器发现差异

| 协议 | Emby | Jellyfin | 兼容性 |
|------|------|----------|--------|
| BSDP (Bonjour) | 支持 | 支持 | 兼容 |
| UDP 多播 | 同协议 | 同协议 | 兼容 |
| HTTP 探测 | 支持 | 支持 | 兼容 |

**结论**：Jellyfin SDK 的 `jellyfin.discovery` 模块使用 BSDP 协议，与 Emby 同源，兼容。

## 六、插件系统

| 功能 | Emby | Jellyfin | 备注 |
|------|------|----------|------|
| 插件管理 API | `GET /emby/Plugins` | `GET /Plugins` | App 未使用此功能 |
| 插件安装 | 通过 Web 界面 | 通过 Web 界面 | 两者相同 |

**结论**：App 不涉及服务器端插件管理功能，无影响。

## 七、差异总结

### 必须处理的问题

| # | 问题 | 影响范围 | 解决方案 |
|---|------|---------|---------|
| 1 | **Base URL 缺 `/emby` 前缀** | 所有 API 请求（24个 API 类） | OkHttp 拦截器自动添加 |
| 2 | **`isMovie`/`isSeries` 等快捷字段** | `GuideFilters.java` 等文件 | 适配层做字段映射 |

### 不需要处理的问题

| # | 问题 | 原因 |
|---|------|------|
| 1 | 认证头格式 | Jellyfin SDK 已使用 `X-Emby-Authorization`，与 Emby 兼容 |
| 2 | 端点路径（除前缀外） | Jellyfin 继承自 Emby，路径一致 |
| 3 | 服务器发现协议 | BSDP 协议同源 |
| 4 | 插件系统 | App 不涉及 |
| 5 | 响应 DTO 结构 | SDK 自动反序列化，字段名兼容 |