# 考勤助手 · Android WebView APK 壳

把现有的网页考勤系统（智能助手 + 出勤表）套进一个 Android App，手机连办公室同一 WiFi 即可像原生 App 一样打开使用。

## 目录结构
```
AttendanceAppAPK/
├── settings.gradle
├── build.gradle
├── gradle.properties
├── gradle/wrapper/gradle-wrapper.properties
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/attendance/MainActivity.java
│       ├── res/layout/activity_main.xml
│       ├── res/values/strings.xml          ← 改服务器地址在这里
│       └── res/xml/network_security_config.xml
└── README.md
```

## 改服务器地址（重要）
默认加载 `http://10.201.2.31:8083`（与服务器同办公室 WiFi 时）。
若服务器 IP 变化，编辑 `app/src/main/res/values/strings.xml` 里的 `web_url` 后重新打包即可。

## 用 Android Studio 打包 APK
1. 安装 [Android Studio](https://developer.android.com/studio)（含 Android SDK）。
2. `File → Open` 选择本目录 `AttendanceAppAPK`，等待 Gradle 同步完成。
   （首次打开会自动补齐 Gradle Wrapper；若提示下载 Gradle，按提示允许。）
3. 菜单 `Build → Build Bundle(s) / APK(s) → Build APK(s)`。
4. 完成后右下角弹窗 `locate`，得到 `app-debug.apk`（或 release 版需先 `Build → Generate Signed Bundle / APK` 配置签名）。
5. 把 APK 传到手机安装（需允许“未知来源”安装）。

## 手机端使用前提
- 手机必须连到**与服务器相同的 WiFi/局域网**，能访问 `10.201.2.31:8083`。
- 打开 App 默认进入「智能助手」聊天界面：发自然语言命令即可完成录入/请假/旷工/离职等，
  每条命令都会回显“📅 日期·动作 + 操作结果”，方便你当场核对有没有操作错。
- 输入姓名片段会弹出匹配名单，点击选择，减少手输与错字。
- 发送“今天出勤表 / 7/21出勤表”可在聊天里直接查看并下载出勤报表图片。

## 已知说明
- 仅用系统自带图标（`ic_menu_agenda`），无需额外图片资源即可编译。
- 已开启 `usesCleartextTraffic` 与网络安全配置，允许 http 明文访问内网 IP（Android 9+ 默认禁止）。
- 报表导出通过 `DownloadManager` 落地到系统“下载”目录。
- 返回键先退回网页历史，再退出 App。
