package com.weisi.Server.frame.init;

import com.weisi.Server.service.serverIce.Impl.MyServiceImpl;

import Ice.Communicator;
import Ice.Identity;
import IceBox.Service;

public class MyBoxService implements Service {
	protected Ice.ObjectAdapter _adapter;
	protected Identity id;
	
	/* (non-Javadoc)
	 * @see IceBox.Service#start(java.lang.String, Ice.Communicator, java.lang.String[])
	 * 启动服务
	 */
	@Override
	public void start(String name, Communicator communicator, String[] arg2) {
		//IceBox
		//创建objectAdapter 这里和service同名
		_adapter = communicator.createObjectAdapter(name);
		//创建servant
		Ice.Object object = new MyServiceImpl();
		id=communicator.stringToIdentity(name);
		_adapter.add(object, id);
		//激活
		_adapter.activate();
		System.out.println("****启动服务****"+name);
	}

	/* (non-Javadoc)
	 * @see IceBox.Service#stop()
	 * 停止服务
	 */
	@Override
	public void stop() {
		System.out.println("*********停止服务************");
		_adapter.destroy();
		System.out.println("*********停止服务成功***********");
	}

}
