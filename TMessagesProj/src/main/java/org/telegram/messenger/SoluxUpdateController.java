package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.ui.web.HttpGetTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SoluxUpdateController {

    private static final long CHECK_INTERVAL = 20 * 60 * 1000L;
    private static final Pattern RELEASE_TAG = Pattern.compile("^v(\\d+(?:\\.\\d+){1,3})-(\\d+)$");
    private static boolean checking;

    private SoluxUpdateController() {
    }

    public static void check(boolean force, Utilities.Callback<UpdateInfo> callback) {
        if (checking) {
            if (callback != null) {
                callback.run(null);
            }
            return;
        }

        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("solux_updates", Context.MODE_PRIVATE);
        long lastCheck = preferences.getLong("lastCheck", 0L);
        if (!force && System.currentTimeMillis() - lastCheck < CHECK_INTERVAL) {
            if (callback != null) {
                callback.run(null);
            }
            return;
        }

        checking = true;
        new HttpGetTask(response -> AndroidUtilities.runOnUIThread(() -> {
            checking = false;
            preferences.edit().putLong("lastCheck", System.currentTimeMillis()).apply();
            if (callback != null) {
                callback.run(parse(response));
            }
        }))
                .setHeader("Accept", "application/vnd.github+json")
                .setHeader("User-Agent", "Solux-Android")
                .execute(BuildVars.GITHUB_RELEASES_API);
    }

    private static UpdateInfo parse(String response) {
        if (TextUtils.isEmpty(response)) {
            return null;
        }
        try {
            JSONObject release = new JSONObject(response);
            if (release.optBoolean("draft", true) || release.optBoolean("prerelease", true)) {
                return null;
            }

            Matcher matcher = RELEASE_TAG.matcher(release.optString("tag_name"));
            if (!matcher.matches()) {
                return null;
            }

            int versionCode = Integer.parseInt(matcher.group(2));
            if (versionCode <= getCurrentVersionCode()) {
                return null;
            }

            String apkUrl = null;
            JSONArray assets = release.optJSONArray("assets");
            if (assets != null) {
                for (int i = 0; i < assets.length(); i++) {
                    JSONObject asset = assets.optJSONObject(i);
                    if (asset == null) {
                        continue;
                    }
                    String name = asset.optString("name");
                    if (name.toLowerCase().endsWith(".apk")) {
                        apkUrl = asset.optString("browser_download_url", null);
                        break;
                    }
                }
            }
            if (TextUtils.isEmpty(apkUrl)) {
                return null;
            }

            String changelog = release.optString("body", "").trim();
            return new UpdateInfo(matcher.group(1), versionCode, changelog, apkUrl);
        } catch (Exception e) {
            FileLog.e("Failed to parse Solux GitHub release", e);
            return null;
        }
    }

    private static int getCurrentVersionCode() {
        try {
            PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            FileLog.e(e);
            return 0;
        }
    }

    public static final class UpdateInfo {
        public final String version;
        public final int versionCode;
        public final String changelog;
        public final String apkUrl;

        private UpdateInfo(String version, int versionCode, String changelog, String apkUrl) {
            this.version = version;
            this.versionCode = versionCode;
            this.changelog = changelog;
            this.apkUrl = apkUrl;
        }
    }
}
