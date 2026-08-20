# Solux Android 项目强制维护规则

本文件是公开仓库的维护规则。任何功能开发、官方上游同步和版本发布都必须遵守；规则与实际安全要求冲突时，以更严格的要求为准。

## 1. 项目定位

- Solux 是基于 Telegram Android 开源代码的社区衍生项目，不是 Telegram 官方客户端。
- 胁迫密码是本地隐私和账号使用安全功能：在本机配置备用账号后，输入单独的本地密码可切换到备用账号，并结束当前设备上的主账号会话。
- 胁迫密码只操作当前设备，不应调用“注销所有设备”或执行影响其他设备的操作。
- 不在文档、界面或营销文案中使用对抗取证、规避执法等表达。统一使用“本地隐私保护”“应急访问安全”“设备意外暴露风险”等中性表述。
- 不在客户端内置广告跳转、隐藏收集或任何未在隐私说明中披露的行为。

## 2. 已修改功能与文件边界

### 本地账号与胁迫密码

以下文件共同组成胁迫密码功能，官方上游更新时必须整体审查，不能只覆盖其中一个文件：

- `TMessagesProj/src/main/java/org/telegram/messenger/SharedConfig.java`：本地哈希、盐、备用账号校验和配置持久化。
- `TMessagesProj/src/main/java/org/telegram/ui/PasscodeActivity.java`：设置普通密码、胁迫密码和备用账号的界面。
- `TMessagesProj/src/main/java/org/telegram/ui/Components/PasscodeView.java`：锁屏密码判断和应急解锁状态。
- `TMessagesProj/src/main/java/org/telegram/ui/LaunchActivity.java`：主界面应急切换入口以及更新检查入口。
- `TMessagesProj/src/main/java/org/telegram/ui/BubbleActivity.java`、`TMessagesProj/src/main/java/org/telegram/ui/ExternalActionActivity.java`：外部入口的应急解锁处理。
- `TMessagesProj/src/main/res/values/strings.xml` 及其他语言目录下的 `strings.xml`：功能文案。

### Solux 品牌、包名和图标

- `gradle.properties`：`APP_PACKAGE`、`APP_VERSION_CODE`、`APP_VERSION_NAME`。
- `TMessagesProj/src/main/java/org/telegram/messenger/BuildVars.java`：Solux 构建常量、更新仓库、API 凭据的 BuildConfig 入口和付费入口开关。
- `TMessagesProj/src/main/res/drawable/solux_logo.xml`、`solux_logo_square.xml`、`safe_cloud_icon.xml`、`drawable-xxhdpi/solux_brand.png`、`drawable-xxxhdpi/solux_launcher.png`：应用内品牌图形。
- `TMessagesProj/src/main/res/mipmap-*`、`TMessagesProj_AppStandalone/src/main/res/mipmap-*`：桌面图标及多账号图标资源。图标选择器还依赖 `TMessagesProj/src/main/java/org/telegram/ui/LauncherIconController.java` 和 `TMessagesProj/src/main/java/org/telegram/ui/Cells/AppIconsSelectorCell.java`。
- 各语言 `strings.xml` 中的 `AppName`、`AppNameBeta`、`Page1Title` 和已替换的品牌文案。

### 付费入口

- `BuildVars.IS_BILLING_UNAVAILABLE=true` 和 `PAID_FEATURES_ENABLED=false` 用于关闭本客户端的本地购买入口。
- 这不会改变 Telegram 服务端的 Premium 状态、权限或限制，也不能把服务端 Premium 文案当作客户端功能删除。
- 上游同步时不得为了隐藏界面而破坏 Stars、支付协议、消息权限或服务端兼容性；如需继续关闭入口，优先沿用现有开关。

### GitHub Releases 更新

- `TMessagesProj/src/main/java/org/telegram/messenger/SoluxUpdateController.java`：读取 `soulx-app/solux-android` 最新正式 Release，只接受非草稿、非预发布且包含 APK 的版本。
- `TMessagesProj/src/main/java/org/telegram/ui/LaunchActivity.java`：显示更新提示并打开 Release APK 下载地址。
- Release tag 必须是 `v<官方版本号>-<实际APK versionCode>`，例如 `v12.9.2-69919`。
- 只有正式 Release 参与客户端更新检查。主分支连续构建产生的预发布包仅用于测试，不会被客户端自动选中。

## 3. 绝对禁止公开的内容

以下内容不得提交到 Git、GitHub Issue、Pull Request、Release 附件或公开日志：

- Telegram `api_id`、`api_hash`、登录验证码、用户会话、授权密钥、手机号和账号数据。
- `google-services.json`，尤其是与私有 Firebase 项目、推送配置或内部应用相关的文件。
- `TMessagesProj/config/release.keystore`、`.jks`、`.p12`、签名密码、别名密码和任何私钥。
- GitHub PAT、Actions Token、云服务密钥、Webhook 密钥、服务器凭据和本地 `local.properties`。
- 已登录账号的数据库、缓存、媒体、崩溃日志、备份文件和测试设备导出数据。
- 未经授权的 Telegram 官方闭源资源、第三方商业资源或不满足其许可证的构建材料。

提交前必须执行：

```text
git status --short
git diff --cached --name-only
rg -n "api_hash|api_id|BEGIN (RSA|OPENSSH|PRIVATE)|AIza|keystore|password" --glob '!*.md' .
```

发现真实凭据时立即停止发布，撤销并轮换凭据；不能只依赖删除提交或修改 `.gitignore`。

## 4. 官方上游同步规则

当前仓库是一次公开导入提交，没有保留 Telegram 官方 Git 历史，因此禁止直接执行未经审查的 `merge` 或批量覆盖。每次同步必须：

1. 记录目标 Telegram 官方版本号、上游提交或源码归档校验值。
2. 在独立临时分支导入上游版本，先完成编译和资源冲突清单。
3. 逐项迁移本文件第 2 节列出的 Solux 改动，特别检查锁屏生命周期、账号切换、注销范围、图标入口和 `BuildVars`。
4. 检查官方新增的登录、支付、通知、备份和更新逻辑，确认没有绕过本地隐私保护或重新启用不需要的购买入口。
5. 通过静态检查、至少一个 `afatRelease` 构建和真实设备回归后，才能合并到 `main`。
6. 更新 `gradle.properties` 的 `APP_VERSION_NAME` 和 `APP_VERSION_CODE` 为对应官方基础版本；实际 APK versionCode 由 `APP_VERSION_CODE * 10 + 9` 产生。
7. 在 `CHANGELOG.md` 增加版本条目，提交信息说明上游版本和 Solux 改动。

## 5. 版本与发布规则

- `APP_VERSION_NAME` 必须跟随 Telegram 官方版本号，不添加 `-solux` 等非数字后缀。
- `APP_VERSION_CODE` 使用官方基础 versionCode；`afat` APK 的实际 versionCode 是基础值乘以 10 再加 9。
- 正式 tag 格式固定为 `v<APP_VERSION_NAME>-<实际APK versionCode>`。
- 每个正式 Release 必须包含 APK、SHA-256 校验文件和对应更新日志；Release 正文不得为空。
- `main` 每次提交都会触发 GitHub Actions，编译并创建一个预发布 Release，标签为 `build-<commit sha>`。预发布包用于验收，不参与客户端自动更新。
- 正式发布使用 `git tag v12.9.2-69919 && git push origin v12.9.2-69919`，Actions 会校验版本和变更日志后创建正式 Release。

## 6. GitHub Actions Secrets

仓库 Settings -> Secrets and variables -> Actions 中配置：

```text
ANDROID_KEYSTORE_BASE64
ANDROID_KEYSTORE_PASSWORD
ANDROID_KEY_ALIAS
ANDROID_KEY_PASSWORD
GOOGLE_SERVICES_JSON
TELEGRAM_API_ID
TELEGRAM_API_HASH
```

Actions 只在运行时写入临时文件，任务结束后由运行器销毁。任何 Secret 都不得写入 workflow 输出、构建日志或源码。

## 7. 发布前检查表

- [ ] `APP_VERSION_NAME` 与官方版本一致。
- [ ] `APP_VERSION_CODE` 与实际 APK versionCode 计算结果一致。
- [ ] `CHANGELOG.md` 有对应 tag 的完整条目。
- [ ] 胁迫密码：普通密码、胁迫密码、无备用账号、账号删除、应用强杀和断网场景已测试。
- [ ] 只影响当前设备，未调用注销所有设备。
- [ ] GitHub Releases 更新检查只接受正式 APK。
- [ ] APK 使用稳定签名，且没有任何凭据进入 Git 历史。
- [ ] Release 包含 APK、SHA-256 和更新日志。
