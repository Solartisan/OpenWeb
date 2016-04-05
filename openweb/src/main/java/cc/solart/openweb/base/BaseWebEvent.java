package cc.solart.openweb.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.solart.openweb.utils.Logger;

/**
 * web页面基类js定义
 * Created by imilk on 15/6/8.
 */
public abstract class BaseWebEvent {
    private static final String TAG = "BaseWebEvent";
    public static final String BACK_PRESSED_LISTENER = "backPressed";
    public static final String HOME_PRESSED_LISTENER = "homePressed";

    /**
     * 返回主页
     */
    public static final int MESSAGE_GO_HOME = 10;
    /**
     * 返回上一页
     */
    public static final int MESSAGE_GO_BACK = 11;
    /**
     * 设置页面标题
     */
    public static final int MESSAGE_SET_TITLE = 12;

    protected Context mContext;
    protected Handler mHandler;
    private Map mListener;
    private WebUrl mUrlSource;

    public BaseWebEvent(Context context, Handler handler, WebUrl url) {
        this.mContext = context;
        this.mHandler = handler;
        this.mListener = new HashMap();
        this.mUrlSource = url;
    }

    private String getCurrentUrl() {
        return this.mUrlSource.getCurrentUrl();
    }


    private static void ensureOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper())
            throw new RuntimeException("handleWebEvent can only be called from main thread");
    }

    public boolean handleWebEvent(String paramString) {
        ensureOnMainThread();
        return handleWebEvent(paramString, null);
    }

    public boolean handleWebEvent(String event, Object data) {
        List callbacks = (List) mListener.get(getCurrentUrl());
        int what = 0;
        if (callbacks != null) {
            if (callbacks.contains(event)) {
                if (BACK_PRESSED_LISTENER.equals(event)) {
                    what = MESSAGE_GO_BACK;
                } else if (HOME_PRESSED_LISTENER.equals(event)) {
                    what = MESSAGE_GO_HOME;
                } else {
                    Logger.d(TAG, "cannot handle event:" + event);
                    return false;
                }
                sendAsyncCallbackMessage(what, data);
                return true;
            }
        }
        return false;
    }

    public void sendAsyncCallbackMessage(int what, Object obj) {
       sendAsyncCallbackMessageDelay(what,obj,0);
    }


    /**
     *
     * @param what
     * @param obj
     * @param delay
     */
    public void sendAsyncCallbackMessageDelay(int what, Object obj,long delay) {
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage();
            msg.what = what;
            msg.obj = obj;
            mHandler.sendMessageDelayed(msg,delay);
        }
    }

    @JavascriptInterface
    public void setListener(final String listener) {
        if (!TextUtils.isEmpty(listener)) {
            this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //TODO mListener put
                    if (mListener.get(getCurrentUrl()) == null) {
                        ArrayList<String> callbacks = new ArrayList<String>();
                        mListener.put(getCurrentUrl(), callbacks);
                    }else {
                        ((List) mListener.get(getCurrentUrl())).add(listener);
                    }
                }
            });
        }
    }


    @JavascriptInterface
    public void goHome() {
        sendAsyncCallbackMessage(MESSAGE_GO_HOME, null);
    }

    @JavascriptInterface
    public void goBack() {
        this.mHandler.post(new Runnable() {

            @Override
            public void run() {
                mListener.remove(getCurrentUrl());
                sendAsyncCallbackMessage(MESSAGE_GO_BACK, null);
            }
        });
    }

    @JavascriptInterface
    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        sendAsyncCallbackMessage(MESSAGE_SET_TITLE, title);
    }

    /**
     *
     * @param label
     * @param text
     */
    @JavascriptInterface
    public void copyText(String label, String text) {
        ClipboardManager copy = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setPrimaryClip(ClipData.newPlainText(label, text));
    }


}
