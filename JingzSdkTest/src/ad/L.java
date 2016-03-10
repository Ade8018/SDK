package ad;

import android.util.Log;

public class L {
	public static final boolean DEBUG = true;

	public static void e(String TAG, String msg) {
		if (DEBUG)
			Log.e(TAG, msg);
	}

	public static void d(String TAG, String msg) {
		if (DEBUG)
			Log.d(TAG, msg);
	}

	public static void i(String TAG, String msg) {
		if (DEBUG)
			Log.i(TAG, msg);
	}

}
