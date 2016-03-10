package ad.jz;

import java.util.Random;

public class Utils {
	public static final Random sRandom = new Random();
	public static String[] cmcc = new String[] { "46000", "46002", "46007" };

	// public static String[] cucc = new String[] { "46001", "46006" };
	// public static String[] ctcc = new String[] { "46003", "46005" };
	// public static String[] all = new String[] { "46000", "46002", "46007",
	// "46001", "46006", "46003", "46005" };

	public static String getRandomIMEI() {
		return "50783506" + (sRandom.nextInt(9000000) + 1000000);
	}

	public static String[] getRandomIMSIAndCarrier() {
		String[] strs = new String[2];
		strs[0] = "46003" + (sRandom.nextInt(9000000) + 1000000)
				+ (sRandom.nextInt(900) + 100);
		strs[1] = "cmcc";
		return strs;
	}

	public static boolean goClickAd() {
		// return getPercentTrue(0.5);
		return true;
	}

	public static boolean goInstallAd() {
		// return getPercentTrue(0.8);
		return true;
	}

	public static boolean getPercentTrue(double x) {
		if (sRandom.nextDouble() < x) {
			return true;
		}
		return false;
	}

	public static void sleepForTime(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String getRandomMac() {
		return "00:08:22:1a:" + sRandom.nextInt(10) + sRandom.nextInt(10) + ":"
				+ sRandom.nextInt(10) + sRandom.nextInt(10);
	}
}
