package com.weisi.Server.frame.init;

import com.weisi.Server.frame.ice.PerfDispatchInterceptor;
import com.weisi.Server.frame.utils.SwitchUtil;
import com.weisi.Server.service.switcher.impl.SwitchI;

import Ice.Communicator;
import Ice.Identity;
import IceBox.Service;

public class MySwitcherBoxService implements Service {
	protected Ice.ObjectAdapter _adapter;
	protected Identity id;
	
	/* (non-Javadoc)
	 * @see IceBox.Service#start(java.lang.String, Ice.Communicator, java.lang.String[])
	 * 启动服务
	 */
	@Override
	public void start(String name, Communicator communicator, String[] arg2) {
		// 服务端发送数据给客户端 SwitchClient.java要运行
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("服务端向客户端发送数据---send thread start.");
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 和服务端心跳的标识
				String sn = "0481deb6494848488048578316516694";
				String msg = "test msg.";
				System.out.println("result = " + SwitchUtil.sendMsg(sn, msg));
			}
		}).start();
		
		//IceBox
		//创建objectAdapter 这里和service同名
		_adapter = communicator.createObjectAdapter(name);
		//创建servant
		Ice.Properties p = communicator.getProperties();
		
		id=communicator.stringToIdentity(name);
		_adapter.add(new SwitchI(), id);
		//激活
		_adapter.activate();
		System.out.println("****启动服务****"+name);
		communicator.waitForShutdown();
	}

	/* (non-Javadoc)
	 * @see IceBox.Service#stop()
	 * 停止服务
	 */
	@Override
	public void stop() {
		System.out.println("*********停止服务" + id + " ************");
		_adapter.destroy();
		PerfDispatchInterceptor.removeICEObject(id);
		System.out.println("*********停止服务成功" + id + " ***********");
	}

}
