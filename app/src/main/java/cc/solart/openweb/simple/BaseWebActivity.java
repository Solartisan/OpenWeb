package cc.solart.openweb.simple;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;


import cc.solart.openweb.base.BaseWebFragment;
import cc.solart.openweb.utils.Logger;

/**
 * Created by imilk on 15/6/9.
 */
public abstract class BaseWebActivity extends BaseActivity {

    public static final String LINK = "link";
    public static final String TITLE = "title";
    private static final String TAG = "BaseWebActivity";
    private String mTitle;
    private String mUrl;
    protected BaseWebFragment mWebFragment;

    private void initTitleBar() {
        mActionBar.setHomeButtonEnabled(true);
        if (!TextUtils.isEmpty(mTitle))
            setTitle(mTitle);
    }

    /**
     * 定制actionbar
     */
    protected abstract void customizeActionBar();

    /**
     *
     * @return  ContentViewID
     */
    protected abstract int getContentViewResId();

    /**
     *
     * @return WebFragmentID
     */
    protected abstract int getWebFragmentResId();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
        this.mWebFragment = ((BaseWebFragment) getFragmentManager().findFragmentById(getWebFragmentResId()));
        if (parseIntent(getIntent())) {
            this.mWebFragment = ((BaseWebFragment) getFragmentManager().findFragmentById(getWebFragmentResId()));
            customizeActionBar();
            initTitleBar();
            this.mWebFragment.loadUrl(this.mUrl);
            return;
        }
        this.mWebFragment.loadUrl("http://www.baidu.com");
    }

    protected boolean parseIntent(Intent intent) {
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri == null) {
                mUrl = intent.getStringExtra(LINK);
                mTitle = intent.getStringExtra(TITLE);
            } else {
                mUrl = uri.toString();
                mTitle = intent.getStringExtra(TITLE);
            }
        }

        if (TextUtils.isEmpty(this.mUrl)) {
            Logger.e("BaseWebActivity", "url should not be null");
            return false;
        }
        return true;
    }

}
