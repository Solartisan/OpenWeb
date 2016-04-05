package cc.solart.openweb.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by imilk on 15/6/19.
 */
public class NetworkUtil {

    public static boolean isNetConnected(Context context) {
        if(context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo workinfo = connectivityManager.getActiveNetworkInfo();
        return (workinfo != null) && (workinfo.isConnectedOrConnecting());
    }

    public static boolean isMobileConnected(Context context) {
        if(context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo workinfo = connectivityManager.getActiveNetworkInfo();
        return (workinfo != null) && (workinfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static int getNetworkType(Context context) {
        if(context == null) {
            return -1;
        }
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo workinfo = connectivityManager.getActiveNetworkInfo();
        if (workinfo != null)
            return workinfo.getType();
        return -1;
    }


    public static String getNetworkStringType(Context context) {
        if(context == null) {
            return null;
        }
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo workinfo = connectivityManager.getActiveNetworkInfo();
        if (workinfo != null) {
            if (workinfo.getType() == ConnectivityManager.TYPE_BLUETOOTH) {
                return "b";
            }
            if (workinfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return "m";
            }
            if (workinfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return "w";
            }
        }
        return null;
    }

    public static boolean isWifiConnected(Context context) {
        if(context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo workinfo = connectivityManager.getActiveNetworkInfo();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            return (!connectivityManager.isActiveNetworkMetered()) && (workinfo != null) && (workinfo.isConnected());
        }else{
            return (workinfo != null) && (workinfo.isConnected());
        }
    }

}
