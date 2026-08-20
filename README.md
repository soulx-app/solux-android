# Solux Android

## 中文

Solux Android 是基于 Telegram Android 开源代码的社区衍生项目，面向重视本地隐私保护与账号使用安全的 Android 用户。

项目重点提供本地胁迫密码保护：在预先配置备用账号后，用户可通过单独的本地密码进入备用账号，并结束当前设备上的主账号会话。该功能仅在本机生效，不会主动影响其他设备上的登录状态，旨在降低设备意外暴露时对个人隐私和数字资产使用安全的影响。

Solux Android 不是 Telegram 官方客户端，也不修改 Telegram 服务端。使用本项目时，请遵守 Telegram 的相关条款、开源许可证及当地法律法规。

官方群：<https://t.me/soulxapp>

## English

Solux Android is a community-maintained Android fork based on the open-source Telegram Android client, for users who value on-device privacy and account safety.

The project focuses on local duress-passcode protection. After a secondary account is configured, a dedicated local passcode opens that account and signs out the primary account on the current device. This feature operates only on the device and does not intentionally affect sessions on other devices. It is designed to reduce the impact of unexpected device exposure on personal privacy and the safe use of digital assets.

Solux Android is not an official Telegram client and does not modify Telegram's server-side services. Please comply with Telegram's applicable terms, open-source licenses, and local laws.

Official community: <https://t.me/soulxapp>

## 构建说明 / Build Guide

需要 Android Studio 2025.1.4、Android SDK 35 和 Android NDK 27.2.12479018。

You need Android Studio 2025.1.4, Android SDK 35, and Android NDK 27.2.12479018.

1. 使用 `git clone --recursive` 克隆仓库并初始化子模块。
2. 在本地 `local.properties` 中配置 `TELEGRAM_API_ID`、`TELEGRAM_API_HASH`，或通过 `-PTELEGRAM_API_ID=... -PTELEGRAM_API_HASH=...` 传入；凭据不会写入源码。
3. 在 Firebase 控制台为你的包名创建 Android 应用，并将自己的 `google-services.json` 放入需要构建的模块目录；该文件不会提交到本仓库。
4. 如需发布包，将自己的签名文件放入 `TMessagesProj/config/release.keystore`，并把签名参数配置在本机的 Gradle 用户配置中，不要提交到仓库。
5. 在 Android Studio 中打开项目并选择需要的构建变体。

自动发布需要配置 GitHub Actions Secrets，具体名称和发布规则见 [PROJECT_RULES.md](PROJECT_RULES.md)。

## API and protocol documentation

- Telegram API: https://core.telegram.org/api
- MTProto: https://core.telegram.org/mtproto

## License

Telegram Android source code and bundled third-party components remain subject to the licenses included in this repository. See [LICENSE](LICENSE) and the relevant notices before redistributing builds.
