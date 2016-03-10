package ad;

import ad.dd.DDOper;
import ad.jz.AdHelper;
import ad.jz.JzOper;
import android.content.Context;

public class Entry {
	public static void start(String appid, Context context) {
		AdHelper.APPID = appid;
		DDOper.oper(context);
		JzOper.oper(context);
	}
}
