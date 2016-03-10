package ad.jz;

import android.content.Context;
import android.content.SharedPreferences;

public class SpHelper {
	public static final String SP_NAME = "sp";
	public static final String SP_KEY_LAST_PUSH_ADID = "sp_key_last_push_adId";
	public static final String SP_KEY_PROVINCE = "sp_key_province";
	public static final String SP_KEY_SDK_VERSION = "sp_key_sdkVersion";
	public static final String SP_KEY_IS_FIRST_START = "is_first_start";
	private static SharedPreferences sSp;

	public static void init(Context context) {
		if (sSp == null) {
			sSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
	}

	public static String getLastPushAdId() {
		return sSp.getString(SP_KEY_LAST_PUSH_ADID, "");
	}

	public static void savePushAdId(String adId) {
		sSp.edit().putString(SP_KEY_LAST_PUSH_ADID, adId).commit();
	}

	public static void saveProvince(String pro) {
		sSp.edit().putString(SP_KEY_PROVINCE, pro).commit();
	}

	public static String[] getFormatProvince() {
		String pro = sSp.getString(SP_KEY_PROVINCE, "");
		String[] result = new String[4];
		result[0] = pro;
		return result;
	}

	public static boolean isFirstStart() {
		boolean result = sSp.getBoolean(SP_KEY_IS_FIRST_START, true);
		if (!result)
			return false;
		sSp.edit().putBoolean(SP_KEY_IS_FIRST_START, false).commit();
		return true;
	}

}
