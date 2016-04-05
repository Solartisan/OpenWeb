package cc.solart.openweb;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import cc.solart.openweb.base.BaseWebEvent;
import cc.solart.openweb.base.WebUrl;
import cc.solart.openweb.utils.NetworkUtil;


/**
 * Created by imilk on 15/6/9.
 */
public class OpenWebEvent extends BaseWebEvent {
    private static final String TAG = "OpenWebEvent";
    public OpenWebEvent(Context context, Handler handler, WebUrl url) {
        super(context, handler, url);
    }

    @JavascriptInterface
    public void logHTML(String message) {
        Log.i(TAG, "html: " +message);
    }


    /**
     * 判断当前网络是否为Wifi
     *
     * @return
     */
    @JavascriptInterface
    public boolean isWifiDataEnable() {
        return NetworkUtil.isWifiConnected(mContext);
    }


    /**
     * 通过toast显示信息
     *
     * @param message
     */
    @JavascriptInterface
    public void toastMessage(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
            }
        });
    }



}
