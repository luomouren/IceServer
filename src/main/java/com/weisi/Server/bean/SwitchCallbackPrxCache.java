package com.weisi.Server.bean;

import com.weisi.Server.switcher.ISwitchCallbackPrx;

/**
 * ice客户端缓存信息
 */
public class SwitchCallbackPrxCache {
	private static final long serialVersionUID = -6579533328390250520L;
	private ISwitchCallbackPrx iSwitchCallbackPrx;
	private String ip;//客户端连接ip
	private String sn;//客户端唯一标识
	private long lastTime;//客户端最后一次请求时间
	private int port;//客户端连接端口

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

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

}
