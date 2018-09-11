package com.azhon.appupdate.manager;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.dialog.UpdateDialog;
import com.azhon.appupdate.utils.ApkUtil;
import com.azhon.appupdate.utils.Constant;
import com.azhon.appupdate.utils.LogUtil;

/**
 * 项目名:    AppUpdate
 * 包名       com.azhon.appupdate.manager
 * 文件名:    DownloadManager
 * 创建时间:  2018/1/27 on 10:27
 * 描述:     TODO
 *
 * @author 阿钟
 */


public class DownloadManager {

    private static final String TAG = "DownloadManager";

    /**
     * 上下文
     */
    private Context context;
    /**
     * 要更新apk的下载地址
     */
    private String apkUrl = "";
    /**
     * apk下载好的名字 .apk 结尾
     */
    private String apkName = "";
    /**
     * apk 下载存放的位置
     */
    private String downloadPath;
    /**
     * 是否提示用户 "当前已是最新版本"
     * <p>
     * {@link #download()}
     */
    private boolean showNewerToast = false;
    /**
     * 通知栏的图标 资源路径
     */
    private int smallIcon = -1;
    /**
     * 整个库的一些配置属性，可以从这里配置
     */
    private UpdateConfiguration configuration;
    /**
     * 要更新apk的versionCode
     */
    private int apkVersionCode = 0;
    /**
     * 显示给用户的版本号
     */
    private String apkVersionName = "";
    /**
     * 更新描述
     */
    private String apkDescription = "";
    /**
     * 安装包大小 单位 M
     */
    private String apkSize = "";
    /**
     * 兼容Android N 添加uri权限 authorities
     */
    private String authorities = "";

    private static DownloadManager manager;

    public static DownloadManager getInstance(Context context) {
        if (manager == null) {
            synchronized (DownloadManager.class) {
                if (manager == null) {
                    manager = new DownloadManager(context);
                }
            }
        }
        return manager;
    }

    private DownloadManager(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 供此依赖库自己使用.
     *
     * @return {@link DownloadManager}
     * @hide
     */
    public static DownloadManager getInstance() {
        if (manager == null) {
            throw new RuntimeException("请先调用 getInstance(Context context) !");
        }
        return manager;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public DownloadManager setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
        return this;
    }

    public int getApkVersionCode() {
        return apkVersionCode;
    }

    public DownloadManager setApkVersionCode(int apkVersionCode) {
        this.apkVersionCode = apkVersionCode;
        return this;
    }

    public String getApkName() {
        return apkName;
    }

    public DownloadManager setApkName(String apkName) {
        this.apkName = apkName;
        return this;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public DownloadManager setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
        return this;
    }

    public DownloadManager setShowNewerToast(boolean showNewerToast) {
        this.showNewerToast = showNewerToast;
        return this;
    }

    public boolean isShowNewerToast() {
        return showNewerToast;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public DownloadManager setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
        return this;
    }

    public DownloadManager setConfiguration(UpdateConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public UpdateConfiguration getConfiguration() {
        return configuration;
    }

    public String getApkVersionName() {
        return apkVersionName;
    }

    public DownloadManager setApkVersionName(String apkVersionName) {
        this.apkVersionName = apkVersionName;
        return this;
    }

    public String getApkDescription() {
        return apkDescription;
    }

    public DownloadManager setApkDescription(String apkDescription) {
        this.apkDescription = apkDescription;
        return this;
    }

    public String getApkSize() {
        return apkSize;
    }

    public DownloadManager setApkSize(String apkSize) {
        this.apkSize = apkSize;
        return this;
    }

    public String getAuthorities() {
        return authorities;
    }

    public DownloadManager setAuthorities(String authorities) {
        this.authorities = authorities;
        return this;
    }

    /**
     * 开始下载
     */
    public void download(Activity activity) {
        checkParams();

        boolean hasUpdate = true;
        // 若设置过versionCode就仅使用它判断
        if (getApkVersionCode() > 0) {
            hasUpdate = apkVersionCode > ApkUtil.getVersionCode(context);
        }

        if (hasUpdate) {
            UpdateDialog dialog = new UpdateDialog(activity);
            dialog.show();
        } else {
            if (showNewerToast) {
                Toast.makeText(context, "当前已是最新版本!", Toast.LENGTH_SHORT).show();
            }
            LogUtil.e(TAG, "当前已是最新版本");
        }
    }

    /**
     * 检查参数
     */
    private void checkParams() {
        if (TextUtils.isEmpty(apkUrl)) {
            throw new RuntimeException("apkUrl can not be empty!");
        }
        if (TextUtils.isEmpty(apkName)) {
            throw new RuntimeException("apkName can not be empty!");
        }
        if (!apkName.endsWith(Constant.APK_SUFFIX)) {
            throw new RuntimeException("apkName must endsWith .apk!");
        }
        if (TextUtils.isEmpty(downloadPath)) {
            throw new RuntimeException("downloadPath can not be empty!");
        }
        if (smallIcon == -1) {
            throw new RuntimeException("smallIcon can not be empty!");
        }
        //如果用户没有进行配置，则使用默认的配置
        if (configuration == null) {
            configuration = new UpdateConfiguration();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        manager = null;
    }
}
