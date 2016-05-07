package cc.solart.openweb;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

import cc.solart.openweb.base.BaseWebChromeClient;
import cc.solart.openweb.base.BaseWebEvent;
import cc.solart.openweb.base.BaseWebFragment;
import cc.solart.openweb.base.BaseWebViewClient;
import cc.solart.openweb.base.WebUrl;
import cc.solart.openweb.utils.Logger;
import cc.solart.openweb.utils.ObjEnsureUtil;

/**
 * Created by imilk on 15/6/9.
 */
public abstract class OpenWebFragment extends BaseWebFragment {

    private static final int REQUEST_CODE_SSL_ERROR = 101;
    private static final int REQUEST_CODE_GPS_JUMP = 102;
    private static final String TAG = "OpenWebFragment";


    private WebFragmentHandler mHandler = new WebFragmentHandler(this);


    private String backPressedCallback;
    private String topBackCallback;


    class WebFragmentHandler extends Handler {
        private final WeakReference wrFragment;

        WebFragmentHandler(OpenWebFragment openWebFragment) {
            this.wrFragment = new WeakReference(openWebFragment);
        }

        public void clearReference() {
            this.wrFragment.clear();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseWebEvent.MESSAGE_GO_HOME:
                    if (mActivity != null) {
                        mActivity.finish();
                    }
                    break;
                case BaseWebEvent.MESSAGE_GO_BACK:
                    goBack();
                    break;
                case BaseWebEvent.MESSAGE_SET_TITLE:
                    String title = (String) msg.obj;
                    if (mActivity != null) {
                        mActivity.setTitle(title);
                    }
                    break;
            }
        }
    }


    /**
     * if you need pull to refresh, you can coding like this, otherwise return null.
     * Careful use of pull down refresh, it may lead to a sliding conflict with the web page.
     * You can also disable and enable the pull to refresh to circumvent this problem by JavaScript
     * override {@link #enablePullToRefresh()} and {@link #disablePullToRefresh()}
     * @return
     */
    protected OnRefreshStatusListener getOnRefreshStatusListener(){
        return null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //清理掉fragment的引用
        mHandler.clearReference();
    }


    /**
     * 检测
     *
     * @return
     */
    private boolean ensureNonNull() {
        return ObjEnsureUtil.ensureNonNull(new Object[]{this.mActivity, this.mWebView, this.mWebEvent, this.mHandler});
    }


    @Override
    public boolean onMenuHome() {
        if (topBackCallback != null) {
            mWebView.loadUrl(
                    "javascript:" + topBackCallback + "()");
            Logger.d(TAG,"load js: javascript:" + topBackCallback + "()");
            return true;
        }
        return super.onMenuHome();
    }

    @Override
    public boolean onBackPressed() {
        if (!ensureNonNull())
            return false;
        if (backPressedCallback != null) {
            mWebView.loadUrl(
                    "javascript:" + backPressedCallback + "()");
            Logger.d(TAG, "load js: javascript:" + backPressedCallback + "()");
            return true;
        }
        return super.onBackPressed();
    }


    protected void setCookie() {

    }

    protected void disablePullToRefresh(){

    }

    protected void enablePullToRefresh(){

    }


    @Override
    public void loadUrl(String url) {
        setCookie();
        super.loadUrl(url);
        if (mProgressBar.getVisibility() == View.GONE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void reload() {
        super.reload();
        if (mProgressBar.getVisibility() == View.GONE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void onInitWebViewSettings() {
        super.onInitWebViewSettings();
        this.mHandler = new WebFragmentHandler(this);
        this.mWebEvent = new OpenWebEvent(this.mActivity, this.mHandler, new WebUrl() {
            @Override
            public String getCurrentUrl() {
                return mWebView.getUrl();
            }
        });
        this.mWebView.addJavascriptInterface(this.mWebEvent, "MyJsBridge");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!ensureNonNull())
            return;
        Logger.d(TAG,"onCreateOptionsMenu");
    }

    public void allowCreateWebMenu() {
        if (!ensureNonNull())
            return;
        setHasOptionsMenu(true);
    }

    @Override
    protected WebChromeClient onCreateWebChromeClient() {
        return new OpenWebChromeClient(this);
    }

    @Override
    protected WebViewClient onCreateWebViewClient() {
        return new OpenWebViewClient(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.i(TAG, "onActivityResult requestCode = " + requestCode + "   resultCode = " + resultCode);

        switch (requestCode) {
            case REQUEST_CODE_SSL_ERROR:
            case REQUEST_CODE_GPS_JUMP:
                reload();
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    class OpenWebViewClient extends BaseWebViewClient {

        private OpenWebViewClient(BaseWebFragment webFragment) {
            super(webFragment);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            boolean flag = super.shouldOverrideUrlLoading(view, url);
            if (!flag) {
                Uri uri = Uri.parse(url);
                String scheme = uri.getScheme();
                if (!TextUtils.isEmpty(scheme)) {
                    if (TextUtils.equals(scheme, "http") || TextUtils.equals(scheme, "https")) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        disablePullToRefresh();
                        backPressedCallback = null;
                        topBackCallback = null;
                    }
                }
                return false;
            } else {
                return true;
            }

        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (getOnRefreshStatusListener()!=null && getOnRefreshStatusListener().isRefreshing()) {
                getOnRefreshStatusListener().refreshComplete();
            }
            if (mProgressBar.getVisibility() == View.VISIBLE) {
                mProgressBar.setVisibility(View.GONE);
                enablePullToRefresh();
            }

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }
    }


    class OpenWebChromeClient extends BaseWebChromeClient {

        private OpenWebChromeClient(BaseWebFragment webFragment) {
            super(webFragment);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            LocationManager locationManager = (LocationManager) mActivity.
                    getSystemService(Context.LOCATION_SERVICE);
            boolean enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enable) {
                AlertDialog dialog = new AlertDialog.Builder(mActivity).setTitle("温馨提示").
                        setMessage("检测到您的GPS已经关闭，是否需要打开?").
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("打开", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, REQUEST_CODE_GPS_JUMP);
                    }
                }).create();
                dialog.show();
            } else {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (getOnRefreshStatusListener() == null || (getOnRefreshStatusListener() !=null && !getOnRefreshStatusListener().isRefreshing())) {
                mProgressBar.setProgress(newProgress);
            }

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mActivity.setTitle(title);
        }
    }
}