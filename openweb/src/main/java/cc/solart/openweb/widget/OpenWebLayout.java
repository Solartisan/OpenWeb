package cc.solart.openweb.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import cc.solart.openweb.R;

/**
 * -------------------------------------------------------------------------
 * Author: imilk
 * Create:  14:38
 * -------------------------------------------------------------------------
 * Describe:
 * -------------------------------------------------------------------------
 * Changes:
 * -------------------------------------------------------------------------
 * 14: Create by imilk
 * -------------------------------------------------------------------------
 */
public class OpenWebLayout extends FrameLayout {

    private ProgressBar mProgressBar;
    private OpenWebView mWebView;
    private int mProgressHeight;
    private Drawable mProgressDrawable;
    private ViewGroup mRefreshView;
    private OpenWebView.OnWebScrollListener mOnWebScrollListener = new OpenWebView.OnWebScrollListener() {
        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            if (mRefreshView != null) {
                if (mWebView.getScrollY() == 0) {
                    mRefreshView.setEnabled(true);
                } else {
                    mRefreshView.setEnabled(false);
                }
            }
        }
    };

    public OpenWebLayout(Context context) {
        this(context, null);
    }

    public OpenWebLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OpenWebLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.open_web_layout, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OpenWebLayout);
        mProgressHeight = ta.getDimensionPixelOffset(R.styleable.OpenWebLayout_progressHeight, context.getResources().getDimensionPixelOffset(R.dimen.dp_progress_height));
        mProgressDrawable = ta.getDrawable(R.styleable.OpenWebLayout_progressDrawable);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mWebView = (OpenWebView) findViewById(R.id.open_webview);
        mWebView.setOnWebScrollListener(mOnWebScrollListener);
        mProgressBar = (ProgressBar) findViewById(R.id.open_progress_bar);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mProgressHeight);
        mProgressBar.setLayoutParams(lp);
        if (mProgressDrawable != null) {
            if (needsTileify(mProgressDrawable) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mProgressBar.setProgressDrawableTiled(mProgressDrawable);
            } else {
                mProgressBar.setProgressDrawable(mProgressDrawable);
            }
        }
        mProgressBar.setIndeterminate(false);

    }

    public WebView getWebView() {
        return mWebView;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    private static boolean needsTileify(Drawable dr) {
        if (dr instanceof LayerDrawable) {
            final LayerDrawable orig = (LayerDrawable) dr;
            final int N = orig.getNumberOfLayers();
            for (int i = 0; i < N; i++) {
                if (needsTileify(orig.getDrawable(i))) {
                    return true;
                }
            }
            return false;
        }

        // If there's a bitmap that's not wrapped with a ClipDrawable or
        // ScaleDrawable, we'll need to wrap it and apply tiling.
        if (dr instanceof BitmapDrawable) {
            return true;
        }

        return false;
    }

    public void setRefreshView(ViewGroup refreshView) {
        mRefreshView = refreshView;
    }


    public void setOnWebScrollListener(OpenWebView.OnWebScrollListener listener) {
        this.mOnWebScrollListener = listener;
    }
}
