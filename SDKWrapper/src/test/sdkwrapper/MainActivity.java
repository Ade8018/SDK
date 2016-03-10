package test.sdkwrapper;

import aaa.bbb.App;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.hello).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				App.init(getApplicationContext());
			}
		});
	}
}
