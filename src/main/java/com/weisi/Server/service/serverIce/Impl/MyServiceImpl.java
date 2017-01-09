package com.weisi.Server.service.serverIce.Impl;

import com.weisi.Server.serverIce._MyServiceDisp;

import Ice.Current;

public class MyServiceImpl extends _MyServiceDisp{

	@Override
	public String hellow(Current __current) {
		String result="*******ICE服务端向客户端发送字符串***********";
		return result;
	}

}
