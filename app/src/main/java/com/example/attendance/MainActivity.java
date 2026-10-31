package com.example.attendance;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        WebSettings ws = webView.getSettings();

        // 网页是单页应用（SPA），必须开启以下项
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);      // localStorage（页面会用到）
        ws.setDatabaseEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        ws.setBuiltInZoomControls(false);
        ws.setDisplayZoomControls(false);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // 自定义 UA 标记：网页据此识别“是否处于本 App 内”，以隐藏“下载App”入口
        ws.setUserAgentString(ws.getUserAgentString() + " AttendanceAppAPK/1.0");

        // 所有链接/跳转都在本 WebView 内打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient());

        // 报表导出（下载 Excel/图片）落地到系统“下载”目录
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                       String mimetype, long contentLength) {
                DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
                req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                String fn = URLUtil.guessFileName(url, contentDisposition, mimetype);
                req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fn);
                req.setMimeType(mimetype);
                DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                dm.enqueue(req);
            }
        });

        webView.loadUrl(getString(R.string.web_url));
    }

    // 返回键：先关助手面板，再退网页历史，最后才退出App
    @Override
    public void onBackPressed() {
        webView.evaluateJavascript(
            "(function(){var p=document.getElementById('assistant-panel');" +
            "if(p && p.style.display!=='none'){toggleAssistant();return 'close';}" +
            "return 'none';})()",
            value -> {
                if ("close".equals(value)) return;            // 关掉了助手，留在App内
                if (webView.canGoBack()) webView.goBack();
                else super.onBackPressed();
            });
    }
}
