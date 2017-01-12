package com.weisi.Server.service.iceGrid.impl;

import com.weisi.Server.gridServer._MyServiceDisp;

import Ice.Current;

public class MyServiceImpl2 extends _MyServiceDisp {

	@Override
	public String hellow(Current __current) {
		return "Hello world2!";
	}

}
