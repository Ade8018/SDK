package abc;

import ad.jz.JzEntry;
import android.app.Application;

public class App extends Application {
	@Override
	public void onCreate() {
		JzEntry.start(this);
		super.onCreate();
	}
}
