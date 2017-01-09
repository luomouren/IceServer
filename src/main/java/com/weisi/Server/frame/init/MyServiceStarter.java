package com.weisi.Server.frame.init;

import com.weisi.Server.service.serverIce.Impl.MyServiceImpl;

public class MyServiceStarter {
	//启动Ice Service
	public static void main(String[] args) {
		int status=0;
		Ice.Communicator ic =null;
		try {
			//初始化Communication对象，args可以传一些初始化参数，如连接超时，初始化客户端连接池的数量等
			ic=Ice.Util.initialize(args);
			//创建名为MyServiceAdapter的ObjectAdapter，使用缺省的通信协议（Tcp/IP端口为20000的请求）
			Ice.ObjectAdapter adapter = ic.createObjectAdapterWithEndpoints
					("MySeviceAdapter", "default -p 20000");
			//实例化一个MyService服务对象（Servant）
			MyServiceImpl servant = new MyServiceImpl();
			//将servant增加到objectAdapter中，并将servant关联到Id为MyService的Ice Object
			adapter.add(servant, Ice.Util.stringToIdentity("MyService"));
			//激活ObjectAdapter
			adapter.activate();
			System.out.println("Ice服务已启动");
			//让服务在退出之前，一直持续对请求的监听
			ic.waitForShutdown();
		} catch (Exception e) {
			e.printStackTrace();
			status=1;
		}finally{
			if(ic!=null){
				ic.destroy();
			}
		}
		System.exit(status);
	}
}
