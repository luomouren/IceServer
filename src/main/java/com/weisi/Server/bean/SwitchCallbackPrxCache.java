package com.weisi.Server.bean;

import com.weisi.Server.switcher.ISwitchCallbackPrx;

/**
 * ice客户端缓存信息
 */
public class SwitchCallbackPrxCache {

	private ISwitchCallbackPrx iSwitchCallbackPrx;
	private String ip;
	private long lastTime;
	private int port;

	public ISwitchCallbackPrx getiSwitchCallbackPrx() {
		return iSwitchCallbackPrx;
	}

	public void setiSwitchCallbackPrx(ISwitchCallbackPrx iSwitchCallbackPrx) {
		this.iSwitchCallbackPrx = iSwitchCallbackPrx;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
