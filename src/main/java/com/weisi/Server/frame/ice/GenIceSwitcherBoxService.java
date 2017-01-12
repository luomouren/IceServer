package com.weisi.Server.frame.ice;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.weisi.Server.frame.utils.SwitchUtil;

import Ice.Communicator;
import Ice.Identity;
import Ice.Object;
import Ice.ObjectAdapter;
import IceBox.Service;

public class GenIceSwitcherBoxService implements Service {
	private static Logger LOGGER = Logger.getLogger(GenIceSwitcherBoxService.class);
	protected ObjectAdapter _adapter;
	protected Identity id;
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(GenIceSwitcherBoxService.class);
	protected static Sl4jLogerI iceLogger = new Sl4jLogerI("communicator");

	@Override
	public void start(String name, Communicator communicator, String[] args) {
		TimerSendSms();
		
		String servantClassName = communicator.getProperties().getProperty("servantClassName");
		LOGGER.info("load servant class "+servantClassName);
		Ice.Util.setProcessLogger(iceLogger);
		_adapter = communicator.createObjectAdapter(name);
		id = communicator.stringToIdentity(name);
		Class<?> servantClass = null;
		try {
			servantClass = ClassHelper.forName(servantClassName);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			return;
		}

		Ice.Object object = null;
		if (null != servantClass) {
			try {
				object = (Object) servantClass.newInstance();
			} catch (InstantiationException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		}

		if (null != object) {
			_adapter.add(PerfDispatchInterceptor.addICEObject(id, object), id);
		}

		_adapter.activate();
		logger.info(name + " service started ,with param size " + args.length + " detail:" + Arrays.toString(args));
		communicator.waitForShutdown();
	}

	
	public void TimerSendSms(){
		// 服务端发送数据给客户端 SwitchClient.java要运行
		new Thread(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("服务端向客户端发送数据---send thread start.");
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 和服务端心跳的标识
				String sn = "0481deb6494848488048578316516694";
				String msg = "test msg.";
				LOGGER.info("result = " + SwitchUtil.sendMsg(sn, msg));
			}
		}).start();
	}
	
	@Override
	public void stop() {
		logger.info("stopping service " + id + " ....");
		_adapter.destroy();
		PerfDispatchInterceptor.removeICEObject(id);
		logger.info("stopped service " + id + " stoped");
	}

}
