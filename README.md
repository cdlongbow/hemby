<h1 align="center">Hemby for Android TV</h1>
<h3 align="center">Emby 客户端 for Android TV</h3>

---

<p align="center">
<br/><br/>
<a href="https://github.com/cdlongbow/hemby">
<img alt="License" src="https://img.shields.io/github/license/cdlongbow/hemby"/>
</a>
<a href="https://github.com/cdlongbow/hemby/releases">
<img alt="Current Release" src="https://img.shields.io/github/release/cdlongbow/hemby"/>
</a>
</p>

Hemby 是一个基于 Jellyfin Android TV 源码改造的 Emby 客户端，适用于 Android TV、Nvidia Shield 和 Amazon Fire TV 设备。

## 构建

项目使用 Gradle 并需要 Android SDK。推荐使用 Android Studio 进行开发和构建。手动构建请确保 JDK 和 Android SDK 已安装并配置在 PATH 中，然后使用 Gradle wrapper：

```shell
./gradlew assembleDebug
```

生成的 APK 文件位于 `/app/build/outputs/apk/debug` 目录。

## 特性

- 基于 Jellyfin Android TV 最新源码，功能完整
- 自动适配 Emby 服务器 API（OkHttp 拦截器处理 `/emby` 前缀）
- 播放速度扩展至 0.25x~3.0x（12 档）
- 全局播放速度设置持久化
- 屏显信息叠加层（时钟、播放时长、网速）
- 手动标记片头片尾 + 自动跳过
- 播放器浮层文字按钮，更直观
- 独立选集按钮，从服务器异步获取剧集列表

## 分支

| 分支 | 说明 |
|------|------|
| `hemby` | 主开发分支，基于 `260725-feat-emby-url-adapter` 合并 |
| `260725-*` | 功能分支，按日期命名 |

## 致谢

- [Jellyfin Project](https://jellyfin.org) — 原始项目
- [Emby](https://emby.media) — 媒体服务器