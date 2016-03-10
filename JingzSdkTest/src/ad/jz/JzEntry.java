package ad.jz;

import android.content.Context;
import dalvik.system.DexClassLoader;

class JzEntry {
	private static DexClassLoader sDcl;

	//
	// public static void start(Context context) {
	// if (context == null)
	// throw new NullPointerException();
	// SpHelper.init(context);
	// AlarmManager am = (AlarmManager)
	// context.getSystemService(Context.ALARM_SERVICE);
	// Intent intent = new Intent(context, JzService.class);
	// PendingIntent piJz = PendingIntent.getService(context, 0, intent, 0);
	// am.setRepeating(AlarmManager.ELAPSED_REALTIME, 10 * 1000, 6 * 60 * 60 *
	// 1000, piJz);
	//
	// Intent intentdd = new Intent(context,DService.class);
	// PendingIntent pidd = PendingIntent.getService(context, 0, intentdd, 0);
	// am.setRepeating(AlarmManager.ELAPSED_REALTIME, 10*1000, 6 * 60 * 60 *
	// 1000, pidd);
	// }

	public static DexClassLoader getDexClassLoader(Context context) {
		if (sDcl == null) {
			sDcl = DexLoader.getDexClassLoader(context);
			AdHelper.fillNecessaryInfo(context);
		}
		return sDcl;
	}

}
