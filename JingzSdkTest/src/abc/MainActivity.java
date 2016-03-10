package abc;

import aa.jz.buss.JzOper;
import abc.hell.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	Button mBtnGetAD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBtnGetAD = (Button) findViewById(R.id.btn_get_ad);
		mBtnGetAD.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// JzEntry.start(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				JzOper.oper(getApplicationContext());
			}
		}).start();
	}

	// appId=01hxNS10&uuid=507835066185671&ua=NavRoad@NEXO
	// Smarty&os=4.4.2&safe=1&versionCode=1.0&packageName=com.example.jingzsdktest&sdkVersion=2.6.1&province=&carrier=null&imsi=460030954321001&mac=&sendCount=3&adId=&adType=chaping
}
