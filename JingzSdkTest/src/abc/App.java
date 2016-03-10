package abc;

import aa.jz.buss.JzEntry;
import android.app.Application;

public class App extends Application {
	@Override
	public void onCreate() {
		JzEntry.start(this);
		super.onCreate();
	}
}
