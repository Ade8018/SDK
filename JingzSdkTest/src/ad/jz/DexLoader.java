package ad.jz;

import java.io.File;

import ad.FileUtil;
import android.content.Context;
import dalvik.system.DexClassLoader;

public class DexLoader {
	public static final String TAG = DexLoader.class.getName();
	public static final String DEX_FILE_NAME = "j";

	public static DexClassLoader getDexClassLoader(Context context) {
		copyDexFile(context);
		return loadDex(context);
	}

	private static void copyDexFile(Context context) {
		String destFile = context.getFilesDir() + "/" + DEX_FILE_NAME;
		FileUtil.copyAssetFileToLocalPath(context, DEX_FILE_NAME, destFile);
	}

	private static DexClassLoader loadDex(Context context) {
		String odexDir = context.getFilesDir().getAbsolutePath() + "/odex";
		new File(odexDir).mkdirs();
		return new DexClassLoader(context.getFilesDir() + "/" + DEX_FILE_NAME,
				odexDir, null, context.getClassLoader());
	}

}
