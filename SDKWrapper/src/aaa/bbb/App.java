package aaa.bbb;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class App extends Application {

	private static String dexFile;
	private static String odexDir;

	@Override
	public void onCreate() {
		super.onCreate();
		App.init(this);
	}

	public static void init(final Context context) {
		dexFile = context.getFilesDir() + File.separator + "classes.dex";
		odexDir = context.getFilesDir().getAbsolutePath() + "/odex";
		new File(odexDir).mkdirs();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 装载dex
				DexClassLoader dcl = null;
				try {
					FileUtil.copy(context.getAssets().open("classes.dex"),
							dexFile);
					if (new File(dexFile).exists()) {
						dcl = new DexClassLoader(dexFile, odexDir, null,
								context.getClassLoader());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (dcl != null) {
					// 设置Appid
					setAppid(dcl);
					// 调用me.lkt.sdk.dd.DDOper.oper(Context)
					call(dcl, "me.lkt.sdk.dd.DDOper", context);
					// 调用 me.lkt.sdk.jz.buss.JzOper.oper(Context)
					call(dcl, "me.lkt.sdk.jz.buss.JzOper", context);
				}
			}
		}).start();
	}

	private static void setAppid(DexClassLoader dcl) {
		try {
			Class cls = dcl.loadClass("me.lkt.sdk.jz.buss.AdHelper");
			Field f = cls.getDeclaredField("APPID");
			f.setAccessible(true);
			f.set(null, "01hbPe10");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void call(DexClassLoader dcl, String pkg, Context context) {
		try {
			Class cls = dcl.loadClass(pkg);
			Method m = cls.getDeclaredMethod("oper", Context.class);
			m.setAccessible(true);
			m.invoke(null, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
