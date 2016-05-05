package cc.solart.openweb.simple;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.solart.openweb.OnRefreshStatusListener;
import cc.solart.openweb.OpenWebFragment;
import cc.solart.openweb.simple.R;

/**
 * -------------------------------------------------------------------------
 * Author: imilk
 * Create:  19:32
 * -------------------------------------------------------------------------
 * Describe:
 * -------------------------------------------------------------------------
 * Changes:
 * -------------------------------------------------------------------------
 * 19 : Create by imilk
 * -------------------------------------------------------------------------
 */
public class MyOpenWebFragment extends OpenWebFragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * if you need pull to refresh, you can coding like this, otherwise return null.
     * Careful use of pull down refresh, it may lead to a sliding conflict with the web page.
     * @return
     */
//    @Override
//    protected OnRefreshStatusListener getOnRefreshStatusListener() {
//        return new OnRefreshStatusListener() {
//
//            @Override
//            public boolean isRefreshing() {
//                return mSwipeRefreshLayout.isRefreshing();
//            }
//
//            @Override
//            public void refreshComplete() {
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        };
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mWebView.reload();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
        return view;
    }

    @Override
    protected int loadLayoutRes() {
        return R.layout.openweb_fragment;
    }

    @Override
    protected int getWebViewId() {
        return R.id.webview;
    }
}
