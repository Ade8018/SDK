package aa.jz.buss;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

public class HttpUtil {
	public static final int ACTION_TYPE_SHOW = 0;
	public static final int ACTION_TYPE_CLICK = 1;
	public static final int ACTION_TYPE_INSTALL = 2;
	public static String pn;

	public static boolean goJzNext(Context context, int actionType) {
		if (pn == null) {
			pn = context.getPackageName();
		}
		String result = doRequest("jz", pn, actionType);
		if (result != null && result.trim().equals("1")) {
			return true;
		}
		return false;
	}

	public static boolean goDdNext(Context context, int actionType) {
		if (pn == null) {
			pn = context.getPackageName();
		}
		String result = doRequest("dd", pn, actionType);
		if (result != null && result.trim().equals("1")) {
			return true;
		}
		return false;
	}

	private static String doRequest(String adType, String packageName, int actionType) {
		InputStream is = null;
		try {
			URL url = new URL("http://www.kalala.xyz:8080/AdServer/" + adType + "?pn=_" + packageName + "&type=" + actionType);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(5000);
			is = conn.getInputStream();
			byte[] buf = new byte[10];
			is.read(buf);
			return new String(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
