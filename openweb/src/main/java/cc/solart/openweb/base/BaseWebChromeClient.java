package cc.solart.openweb.base;

import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import cc.solart.openweb.utils.Logger;


/**
 * Created by imilk on 15/6/8.
 */
public class BaseWebChromeClient extends WebChromeClient {

    protected static final String TAG = "BaseWebChromeClient";
    private BaseWebFragment mWebFragment;
    public BaseWebChromeClient(BaseWebFragment webFragment) {
        mWebFragment = webFragment;
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
        callback.invoke(origin, true, false);

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        Logger.i(TAG, "onProgressChanged newProgress=" + newProgress);

    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        Logger.i(TAG, "onJsPrompt message=" + message);
        return true;
    }
}
