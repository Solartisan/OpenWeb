package cc.solart.openweb.utils;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

/**
 * Created by imilk on 15/6/8.
 */
public class WebSettingsUtil {


    public static void initSettings(Context paramContext, WebView webView)
    {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextZoom(100);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        setGeolocation(paramContext, webSettings);
        setAppCache(paramContext, webSettings);
        setStorage(paramContext, webSettings);
        setUserAgent(paramContext, webSettings);
    }

    private static void setGeolocation(Context paramContext, WebSettings paramWebSettings)
    {
        paramWebSettings.setGeolocationEnabled(true);
        paramWebSettings.setGeolocationDatabasePath(paramContext.getApplicationContext().getDir("geodatabase", 0).getPath());
    }

    private static void setUserAgent(Context paramContext, WebSettings paramWebSettings)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramWebSettings.getUserAgentString());
        paramWebSettings.setUserAgentString(localStringBuilder.toString());
    }

    private static void setAppCache(Context paramContext, WebSettings paramWebSettings)
    {
        paramWebSettings.setAppCacheEnabled(true);
        String path = paramContext.getApplicationContext().getDir("cache", 0).getPath();
        mkdirs(path);
        paramWebSettings.setAppCachePath(path);
    }

    private static void mkdirs(String paramString)
    {
       File file = new File(paramString);
        if (!file.exists())
            file.mkdirs();
    }

    private static void setStorage(Context paramContext, WebSettings paramWebSettings)
    {
        paramWebSettings.setDomStorageEnabled(true);
        paramWebSettings.setDatabaseEnabled(true);
    }
}
