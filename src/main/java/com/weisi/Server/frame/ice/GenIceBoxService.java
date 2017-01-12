package com.weisi.Server.frame.ice;

/**
 * by song lin & wu zhihui
 */
import java.util.Arrays;

import org.slf4j.LoggerFactory;

import Ice.Communicator;
import Ice.Identity;
import Ice.Object;
import Ice.ObjectAdapter;
import IceBox.Service;

public class GenIceBoxService implements Service {

	protected ObjectAdapter _adapter;
	protected Identity id;
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(GenIceBoxService.class);
	protected static Sl4jLogerI iceLogger = new Sl4jLogerI("communicator");

	@Override
	public void start(String name, Communicator communicator, String[] args) {
		String servantClassName = communicator.getProperties().getProperty("servantClassName");
		System.out.println("load servant class "+servantClassName);
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

	}

	@Override
	public void stop() {
		logger.info("stopping service " + id + " ....");
		_adapter.destroy();
		PerfDispatchInterceptor.removeICEObject(id);
		logger.info("stopped service " + id + " stoped");

	}

}
