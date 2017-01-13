package com.weisi.Server.frame.ice;

import java.lang.reflect.InvocationTargetException;
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

		Ice.Object test = null;
		if (null != servantClass) {
			try {
				try {
					// 添加属性Ice.Properties p
					//创建servant
					Ice.Properties p = communicator.getProperties();
					try {
						test=(Object) servantClass.getConstructor(Ice.Properties.class).newInstance(p);
					} catch (IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				
			} catch (InstantiationException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		}

		if (null != test) {
			_adapter.add(PerfDispatchInterceptor.addICEObject(id, test), id);
		}

		_adapter.activate();
		logger.info(name + " service started ,with param size " + args.length + " detail:" + Arrays.toString(args));
		communicator.waitForShutdown();
	}

	@Override
	public void stop() {
		logger.info("stopping service " + id + " ....");
		_adapter.destroy();
		PerfDispatchInterceptor.removeICEObject(id);
		logger.info("stopped service " + id + " stoped");
	}

}
