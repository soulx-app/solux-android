# Solux Android

## 中文

Solux Android 是基于 Telegram Android 开源代码的社区衍生项目，面向 Android 平台。

项目包含本地多账号管理、胁迫密码保护以及 Solux 品牌界面。胁迫密码功能仅作用于当前设备，设计目标是在输入特定密码后切换到备用账号，并退出当前设备上的主账号会话，不主动影响其他设备上的登录状态。

Solux Android 不是 Telegram 官方客户端，也不修改 Telegram 服务端。使用本项目时，请遵守 Telegram 的相关条款、开源许可证及当地法律法规。

## English

Solux Android is a community-maintained Android fork based on the open-source Telegram Android client.

The project includes local multi-account management, duress-passcode protection, and Solux branding. The duress-passcode feature is designed to switch to a secondary account and sign out the primary account on the current device only; it does not intentionally affect sessions on other devices.

Solux Android is not an official Telegram client and does not modify Telegram's server-side services. Please comply with Telegram's applicable terms, open-source licenses, and local laws.

## 构建说明 / Build Guide

需要 Android Studio 2025.1.4、Android SDK 35 和 Android NDK 27.2.12479018。

You need Android Studio 2025.1.4, Android SDK 35, and Android NDK 27.2.12479018.

1. 使用 `git clone --recursive` 克隆仓库并初始化子模块。
2. 在 `TMessagesProj/src/main/java/org/telegram/messenger/BuildVars.java` 中配置你自己的 Telegram `api_id` 和 `api_hash`。
3. 在 Firebase 控制台为你的包名创建 Android 应用，并将自己的 `google-services.json` 放入需要构建的模块目录；该文件不会提交到本仓库。
4. 如需发布包，将自己的签名文件放入 `TMessagesProj/config/release.keystore`，并把签名参数配置在本机的 Gradle 用户配置中，不要提交到仓库。
5. 在 Android Studio 中打开项目并选择需要的构建变体。

## API and protocol documentation

- Telegram API: https://core.telegram.org/api
- MTProto: https://core.telegram.org/mtproto

## License

Telegram Android source code and bundled third-party components remain subject to the licenses included in this repository. See [LICENSE](LICENSE) and the relevant notices before redistributing builds.
