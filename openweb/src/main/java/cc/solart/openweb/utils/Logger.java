package cc.solart.openweb.utils;

import android.util.Log;


/**
 * -------------------------------------------------------------------------
 * Author: imilk
 * Create: 15/10/14 14:10
 * -------------------------------------------------------------------------
 * Describe:Logging helper class. Logger is a wrapper of {@link Log}
 * But more pretty, simple and powerful.
 * <p/>
 *
 * {@code <android-sdk>/platform-tools/adb shell setprop log.tag.OpenWeb VERBOSE}
 * -------------------------------------------------------------------------
 * Changes:
 * -------------------------------------------------------------------------
 * 15/10/14 14 : Create by imilk
 * -------------------------------------------------------------------------
 */
public final class Logger {
    private static String TAG = "OpenWeb";

    private static boolean DEBUG = Log.isLoggable(TAG, Log.VERBOSE);


    /**
     * Customize the log tag for your application, so that other apps
     * using Volley don't mix their logs with yours.
     * <br />
     * Enable the log property for your tag before starting your app:
     * <br />
     * {@code adb shell setprop log.tag.&lt;tag&gt;}
     */
    static {
        Log.d(TAG, "LOGGER DEBUG = " + DEBUG);
    }

    //no instance
    private Logger() {
    }


    public static void v(String tag, String msg){
        if (DEBUG)
            Log.v(tag,msg);
    }

    public static void d(String tag, String msg){
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg){
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg){
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg){
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg,Throwable tr){
        if (DEBUG)
            Log.e(tag, msg,tr);
    }

}
