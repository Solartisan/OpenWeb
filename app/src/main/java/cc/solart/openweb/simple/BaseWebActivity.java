package cc.solart.openweb.simple;

import android.os.Bundle;
import android.text.TextUtils;

import cc.solart.openweb.base.BaseWebFragment;

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

        this.mWebFragment.loadUrl("http://news.163.com/");
    }


}
