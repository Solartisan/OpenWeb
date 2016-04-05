package cc.solart.openweb.simple;

import android.os.Bundle;
import android.os.Message;
import android.view.Menu;

import cc.solart.openweb.simple.R;


/**
 * Created by imilk on 15/6/9.
 */
public class OpenWebActivity extends BaseWebActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mWebFragment != null) {
            ((MyOpenWebFragment) this.mWebFragment).allowCreateWebMenu();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void customizeActionBar() {
        if (mActionBar == null) {
            mActionBar = getActionBar();
        }
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
    }


    @Override
    public void onBackPressed() {
        if (!this.mWebFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.openweb_activity;
    }

    @Override
    protected int getWebFragmentResId() {
        return R.id.yellowpage_fragment_container;
    }

    @Override
    protected void onMenuHome() {
        if (!this.mWebFragment.onMenuHome()) {
            finish();
        }
    }


    @Override
    protected void handleMessage(Message msg) {
    }
}
