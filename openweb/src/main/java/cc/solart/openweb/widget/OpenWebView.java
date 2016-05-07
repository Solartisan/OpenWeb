package cc.solart.openweb.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * -------------------------------------------------------------------------
 * Author: imilk
 * Create:  20:53
 * -------------------------------------------------------------------------
 * Describe:
 * -------------------------------------------------------------------------
 * Changes:
 * -------------------------------------------------------------------------
 * 20 : Create by imilk
 * -------------------------------------------------------------------------
 */
public class OpenWebView extends WebView {

    private OnWebScrollListener mListener;

    public OpenWebView(Context context) {
        super(context);
    }

    public OpenWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OpenWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mListener!=null){
            mListener.onScrollChanged(l,t,oldl,oldt);
        }
    }


    public void setOnWebScrollListener(OnWebScrollListener listener) {
        this.mListener = listener;
    }

    public interface OnWebScrollListener {

        void onScrollChanged(int l, int t, int oldl, int oldt);

    }
}
