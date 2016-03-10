package aa.dd;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import aa.FileUtil;
import aa.jz.buss.Utils;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class Dex {
	public static final String TAG = Dex.class.getName();
	public static final String DEX_FILE_NAME = "sdk.dex";
	private static DexClassLoader sDcl;;

	public static void copyFileToLocal(Context context) {
		copyDexFile(context);
	}

	private static void copyDexFile(Context context) {
		String destFile = context.getFilesDir() + "/" + DEX_FILE_NAME;
		if (new File(destFile).exists())
			return;
		InputStream is = null;
		try {
			is = context.getAssets().open(DEX_FILE_NAME);
			FileUtil.copy(is, destFile);
		} catch (IOException e) {
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
	}

	public static void loadDex(Context context) {
		if (sDcl != null)
			return;
		String odexDir = context.getFilesDir().getAbsolutePath() + "/odex";
		new File(odexDir).mkdirs();
		sDcl = new DexClassLoader(context.getFilesDir() + "/" + DEX_FILE_NAME,
				odexDir, null, context.getClassLoader());
		if (sDcl == null) {
			throw new RuntimeException("Can't load dex file.");
		}
	}

	public static DexClassLoader getClassLoader() {
		return sDcl;
	}

	public static void fillConfigParams(TelephonyManager tm, Context context) {
		Class cls_Config = null;
		try {
			cls_Config = sDcl.loadClass("cs.network.configs.Config");
			Field f_appID = cls_Config.getDeclaredField("appID");
			Field f_appPassword = cls_Config.getDeclaredField("appPassword");
			Field f_clientUUID = cls_Config.getDeclaredField("clientUUID");
			Field f_imsi = cls_Config.getDeclaredField("imsi");
			Field f_appPackageName = cls_Config
					.getDeclaredField("appPackageName");
			f_appID.setAccessible(true);
			f_appPassword.setAccessible(true);
			f_clientUUID.setAccessible(true);
			f_imsi.setAccessible(true);
			f_appPackageName.setAccessible(true);

			f_appID.set(null, "23044");
			f_appPassword.set(null, "WMAQLoHHmpsmxu5x");
			String imei = tm.getDeviceId();
			Log.e(TAG, "imei: " + imei);
			f_clientUUID.set(null, imei);
			String imsi = tm.getSubscriberId();
			if (imsi == null) {
				imsi = Utils.getRandomIMSIAndCarrier()[0];
			}
			f_imsi.set(null, imsi);
			String pn = context.getPackageName();
//			pn = pn.substring(0, 2) + "pp" + pn.substring(2);
			f_appPackageName.set(null, pn);

		} catch (Exception e) {
			Log.e(TAG, "填写设置信息出错");
			e.printStackTrace();
		}
	}
}
