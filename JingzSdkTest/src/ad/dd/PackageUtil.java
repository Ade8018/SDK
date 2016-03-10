package ad.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

public class PackageUtil {
	public static final String TAG = PackageUtil.class.getName();

	public static List<String> getInstalledUserPackagesForStrings(Context context) {
		ArrayList<String> localArrayList = new ArrayList<String>();
		Iterator<PackageInfo> localIterator = getInstalledPackages(context)
				.iterator();
		while (true) {
			if (!localIterator.hasNext()) {
				Log.e(TAG, "获取到已安装的应用:" + localArrayList.toString());
				return localArrayList;
			}
			PackageInfo localPackageInfo = (PackageInfo) localIterator.next();
			if ((0x1 & localPackageInfo.applicationInfo.flags) != 0)
				continue;
			localArrayList.add(localPackageInfo.packageName);
		}
	}

	public static List<PackageInfo> getInstalledPackages(Context context) {
		return context.getPackageManager().getInstalledPackages(8192);
	}
}
