package cc.solart.openweb.base;

import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.solart.openweb.utils.Logger;

/**
 * Created by imilk on 15/6/8.
 */
public class BaseWebViewClient extends WebViewClient {

    private BaseWebFragment mWebFragment;

    public BaseWebViewClient(BaseWebFragment webFragment) {
        mWebFragment = webFragment;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Logger.i("BaseWebFragment", "shouldOverrideUrlLoading url=" + url);
        if (TextUtils.isEmpty(url)) {
            return true;
        }

        if (url.startsWith("sms:")) {
            String regex = "sms:([\\d]*?)\\?body=([\\w\\W]*)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(Uri.decode(url).replaceAll(" ", ""));
            if (m.find()) {
                String tel = m.group(1);
                String body = m.group(2);

                Uri smsto = Uri.parse("smsto:" + tel);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsto);
                sendIntent.putExtra("sms_body", body);
                mWebFragment.startActivity(sendIntent);
                return true;
            }
        } else if (url.startsWith("tel:")) {
            Intent telIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            telIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mWebFragment.startActivity(telIntent);
            return true;
        } else if (url.startsWith("mailto:")) {
            MailTo mailTo = MailTo.parse(url);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo.getTo()});
            intent.putExtra(Intent.EXTRA_CC, mailTo.getCc());
            intent.putExtra(Intent.EXTRA_TEXT, mailTo.getBody());
            intent.putExtra(Intent.EXTRA_SUBJECT, mailTo.getSubject());
            intent.setPackage("com.android.email");
            intent.setType("text/plain");
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            mWebFragment.startActivity(intent);
            return true;
        } else if (url.startsWith("intent:")) {

            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                mWebFragment.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }
}
