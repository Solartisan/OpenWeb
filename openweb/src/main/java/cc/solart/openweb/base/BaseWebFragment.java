package cc.solart.openweb.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cc.solart.openweb.utils.Logger;
import cc.solart.openweb.utils.NetworkUtil;
import cc.solart.openweb.utils.ObjEnsureUtil;
import cc.solart.openweb.utils.WebSettingsUtil;


/**
 * Created by imilk on 15/6/8.
 */
public abstract class BaseWebFragment extends Fragment {
    protected static final String TAG = "BaseWebFragment";
    protected static final String BLANK_SCREEN_URL = "about:blank";
    protected BaseWebEvent mWebEvent;
    protected WebView mWebView;
    protected boolean mNetworkConnected;
    private NetworkConnectivityReceiver mNetworkConnectivityReceiver;

    /**
     * TargetApi{@link Build.VERSION_CODES.KITKAT}
     */
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    protected Activity mActivity;


    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
    }

    protected abstract int loadLayoutRes();

    protected abstract int getWebViewId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
        registerConnectivityReceiver();
        mNetworkConnected = NetworkUtil.isNetConnected(mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int i = loadLayoutRes();
        if (i <= 0)
            throw new IllegalArgumentException("You must provide a valid category_more");
        View view = inflater.inflate(i, container, false);

        mWebView = (WebView) view.findViewById(getWebViewId());
        if (mWebView == null)
            throw new IllegalArgumentException("As a web view fragment, you must provide a webview named browser");

        onInitWebViewSettings();
        return view;
    }

    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unregisterConnectivityReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
        }
    }

    protected void onInitWebViewSettings() {
        WebSettingsUtil.initSettings(this.mActivity, this.mWebView);
        WebChromeClient webChromeClient = onCreateWebChromeClient();
        if (webChromeClient != null) {
            mWebView.setWebChromeClient(webChromeClient);
        }
        WebViewClient webViewClient = onCreateWebViewClient();
        if (webViewClient != null) {
            mWebView.setWebViewClient(webViewClient);
        }
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mWebView.requestFocus();
        mWebView.setDownloadListener(new WebDownloadListener(this));
    }

    private boolean ensureNonNull() {
        return ObjEnsureUtil.ensureNonNull(new Object[]{this.mActivity, this.mWebView, this.mWebEvent});
    }

    public void loadUrl(String url) {
        if (!ensureNonNull())
            return;
        if (TextUtils.isEmpty(url)) {
            Logger.e(TAG, "The url should not be null, load nothing");
            return;
        }
        Logger.d(TAG, "loadUrl: " + url);
        this.mWebView.loadUrl(url);
    }

    /**
     * webview reload
     */
    public void reload() {
        if (!ensureNonNull())
            return;
        Logger.d("BaseWebFragment", "webview reload");
        this.mWebView.reload();
    }

    protected void onNetworkConnected() {
        reload();
    }


    protected WebChromeClient onCreateWebChromeClient() {
        return new BaseWebChromeClient(this);
    }

    protected WebViewClient onCreateWebViewClient() {
        return new BaseWebViewClient(this);
    }

    private int stepsToGoBack() {
        int j = 1;
        WebBackForwardList webBackForwardList = mWebView.copyBackForwardList();
        int k = webBackForwardList.getCurrentIndex();

        Logger.d(TAG, "k = " + k);
        for (int i = 0; i <= k; i++) {
            String url = webBackForwardList.getItemAtIndex(k - i).getUrl();
            if ((!BLANK_SCREEN_URL.equalsIgnoreCase(url)) && (TextUtils.equals(mWebView.getUrl(), url)))
                break;
            j += 1;
            Logger.d(TAG, "j = " + j);
        }
        return j;
    }

    /**
     * 回退H5
     *
     * @return
     */
    protected boolean goBack() {
        int i;
        int j;
        if (mWebView.canGoBack()) {
            WebBackForwardList backForwardList = mWebView.copyBackForwardList();
            i = stepsToGoBack();
            j = backForwardList.getCurrentIndex();
            Logger.d(TAG, "i = " + i);
            Logger.d(TAG, "j = " + j);
            if (i <= j) {
                String title = backForwardList.getItemAtIndex(j - i).getTitle();
                if (!TextUtils.isEmpty(title)) {
                    mActivity.setTitle(title);
                }
                mWebView.goBackOrForward(-i);
                return true;
            }
        }
        return false;
    }


    /**
     * 前进H5页面
     *
     * @return
     */
    protected boolean goForward() {
        int i;
        int j;
        if (mWebView.canGoForward()) {
            WebBackForwardList backForwardList = mWebView.copyBackForwardList();
            i = 1;
            j = backForwardList.getCurrentIndex();
            if (backForwardList.getSize() > j) {
                String title = backForwardList.getItemAtIndex(j + i).getTitle();
                if (!TextUtils.isEmpty(title)) {
                    mActivity.setTitle(title);
                }
                mWebView.goBackOrForward(i);
                return true;
            }
        }
        return false;
    }

    /**
     * ActionBar返回监听处理，子类应该覆写该方法
     * @return
     */
    public boolean onMenuHome(){
        return false;
    }


    public boolean onBackPressed() {
        if (!ensureNonNull())
            return false;
        return goBack();
    }

    public boolean onForwardPressed() {
        if (!ensureNonNull())
            return false;
        return goForward();
    }


    private void registerConnectivityReceiver() {
        Logger.d(TAG, "Register network connectivity changed receiver");
        if (mNetworkConnectivityReceiver == null) {
            mNetworkConnectivityReceiver = new NetworkConnectivityReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mActivity.registerReceiver(mNetworkConnectivityReceiver, intentFilter);
    }

    private void unregisterConnectivityReceiver() {
        Logger.d(TAG, "Unregister network connectivity changed receiver");
        mActivity.unregisterReceiver(mNetworkConnectivityReceiver);
    }

    /**
     * DownloadListener
     */
    class WebDownloadListener implements DownloadListener {
        private BaseWebFragment mWebFragment;

        private WebDownloadListener(BaseWebFragment mWebFragment) {
            this.mWebFragment = mWebFragment;
        }


        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mWebFragment.startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class NetworkConnectivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean bool = NetworkUtil.isNetConnected(mActivity);
            if ((!mNetworkConnected) && (bool)) {
                onNetworkConnected();
            }
            mNetworkConnected = bool;
        }
    }
}
