# Solux Android 更新日志

所有正式 Release 必须在这里增加对应版本条目，Release 正文必须引用同一条目。版本号与 tag 规则见 [PROJECT_RULES.md](PROJECT_RULES.md)。

## [v12.9.2-69919] - 2026-08-20

### 新增

- 增加本地胁迫密码与备用账号切换能力。
- 增加 Solux 品牌名称、图标和应用内品牌资源。
- 增加 GitHub Releases 更新检查，正式版本从 `soulx-app/solux-android` 获取 APK。
- 增加 GitHub Actions 自动构建与 Release 发布流程。

### 调整

- 应用包名为 `com.solux.cloud`。
- 关闭客户端本地 Premium 购买入口，不改变服务端 Premium 状态和限制。
- GitHub 项目介绍增加官方社区链接：<https://t.me/soulxapp>。

### 安全与兼容性

- Telegram API 凭据改为从本地配置或 CI Secrets 注入，不写入公开源码。
- 仅影响当前设备上的本地账号会话，不主动影响其他设备。

## 后续版本模板

```markdown
## [v<官方版本号>-<实际APK versionCode>] - YYYY-MM-DD

### 上游同步

- Telegram 官方版本：
- 上游提交或源码校验值：

### 新增

-

### 修复

-

### 调整

-

### 安全与兼容性

-
```
