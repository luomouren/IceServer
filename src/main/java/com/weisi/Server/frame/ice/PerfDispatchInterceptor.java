package com.weisi.Server.frame.ice;

import java.util.Map;

import org.slf4j.LoggerFactory;

import Ice.DispatchInterceptor;
import Ice.DispatchStatus;
import Ice.Identity;
import Ice.Request;

public class PerfDispatchInterceptor extends DispatchInterceptor {
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(PerfDispatchInterceptor.class);

	private static final Map<Ice.Identity, Ice.Object> id2ObjectMAP = new java.util.concurrent.ConcurrentHashMap<Ice.Identity, Ice.Object>();
	private static final PerfDispatchInterceptor self = new PerfDispatchInterceptor();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static PerfDispatchInterceptor getINSTANCE() {
		return self;
	}

	public static DispatchInterceptor addICEObject(Ice.Identity id,
			Ice.Object iceObj) {
		id2ObjectMAP.put(id, iceObj);
		return self;
	}

	/**
	 * dispatch request to servant
	 */
	@Override
	public DispatchStatus dispatch(Request request) {
		Identity theId = request.getCurrent().id;
		String inf = "dispach req,method:" + request.getCurrent().operation
				+ " service:" + theId.name + " server address:"
				+ request.getCurrent().con;
		logger.info(inf + " begin");
		try {

			DispatchStatus reslt = id2ObjectMAP.get(request.getCurrent().id)
					.ice_dispatch(request);
			logger.info(inf + " success");
			return reslt;
		} catch (RuntimeException e) {
			logger.info(inf + " error " + e);
			throw e;
		}
	}

	public static void removeICEObject(Identity id) {
		logger.info("remove ice object " + id);
		id2ObjectMAP.remove(id);

	}

}
