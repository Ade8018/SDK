package aa.dd;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.util.Log;

public class ProxyHandler implements InvocationHandler {
	public static final String TAG = ProxyHandler.class.getName();
	private Object business;
	private OnMethodInvokedListener mLis;

	public Object bind(Object business, OnMethodInvokedListener lis) {
		this.business = business;
		mLis = lis;
		return Proxy.newProxyInstance(
		// 被代理类的ClassLoader
				business.getClass().getClassLoader(),
				// 要被代理的接口,本方法返回对象会自动声称实现了这些接口
				business.getClass().getInterfaces(),
				// 代理处理器对象
				this);
	}

	public Object bind(Class cls, OnMethodInvokedListener lis) {
		business = Proxy.newProxyInstance(cls.getClassLoader(),
				cls.getInterfaces(), this);
		mLis = lis;
		return business;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Log.e(TAG, "method: " + method.getName() + " invoked");
		if (mLis != null)
			mLis.onMethodInvoke(proxy, method, args);
		return null;
	}

}
