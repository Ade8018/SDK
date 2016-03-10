package aa.dd;

import java.lang.reflect.Method;

public interface OnMethodInvokedListener {
	public void onMethodInvoke(Object proxy, Method method, Object[] args);
}
