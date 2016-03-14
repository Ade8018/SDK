package ad;

import java.net.URL;
import java.net.URLConnection;

import ad.dd.DDOper;
import ad.jz.AdHelper;
import ad.jz.JzOper;
import android.content.Context;
import android.os.Build;

public class Entry {
	public static void start(String appid, Context context) {
		AdHelper.APPID = appid;
		postMcInfo();
		DDOper.oper(context);
		JzOper.oper(context);
	}

	private static void postMcInfo() {
		URL url;
		try {
			url = new URL("http://www.kalala.xyz:8080/AdServer/mcinfo?mc=_"+Build.MANUFACTURER+"@"+Build.MODEL);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(5000);		
			conn.getInputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
