package com.weisi.Server.service.switcher.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.weisi.Server.bean.SwitchCallbackPrxCache;
import com.weisi.Server.frame.utils.SwitchUtil;
import com.weisi.Server.switcher.ISwitchCallbackPrx;
import com.weisi.Server.switcher.ISwitchCallbackPrxHelper;
import com.weisi.Server.switcher.SwitchException;
import com.weisi.Server.switcher._ISwitchDisp;

import Ice.Current;
import Ice.Identity;

/**
 * 服务端接口定义(客户端调用服务端的接口)
 */
public class SwitchI extends _ISwitchDisp {

	private static Logger LOGGER = Logger.getLogger(SwitchI.class);
	private static final long serialVersionUID = 1L;

	// 客户端连接缓存
	public static ConcurrentHashMap<String, SwitchCallbackPrxCache> switchCallbackPrxCacheMap = new ConcurrentHashMap<>();
	private SwitchCallbackPrxCache switchCallbackPrxCache = null;

	public SwitchI(Ice.Properties p) {
	}
	public SwitchI() {
	}
	
	/**
	 * 心跳(如果用户自己定时做心跳可以心跳时传参)
	 */
	@Override
	public boolean heartbeat(Identity id, String sn, int netMode, int netStrength, long time,Current current) {
		LOGGER.info(switchCallbackPrxCacheMap.size());
		LOGGER.info("TCP心跳开始——参数——tcp heartbeat begin params sn = " + sn + " id.name = " + id.name + ", category = "
				+ id.category + ", netMode = " + netMode + ", netStrength = " + netStrength);
		Ice.Connection con = current.con;
		Ice.IPConnectionInfo ipConn = (Ice.IPConnectionInfo) con.getInfo();
		if (null != ipConn) {
			LOGGER.info("连接服务器地址为——ipConn remote:" + ipConn.remoteAddress + ":" + ipConn.remotePort);
			LOGGER.info("连接服务器断开为——ipConn local:" + ipConn.localAddress + ":" + ipConn.localPort);
		}

		LOGGER.info("heartbeat");
		// 心跳业务处理
		
		//客户端注册结束后  回调客户端
		String msg = "客户端注册结束后  回调客户端 test msg.";
		//发送信息前刷新下Map，以防Client掉线
		long currentTime = System.currentTimeMillis();//毫秒级别，13位
		refreshMap(switchCallbackPrxCacheMap,currentTime);
		LOGGER.info("客户端注册结束后  回调客户端 = " + SwitchUtil.sendMsg(sn, msg));
		
		/*ACM s= con.getACM();
		System.out.println(s.heartbeat);
		System.out.println(s.timeout);//60
		System.out.println(con.timeout());//60000
*/		
		// 如果已经存在不更新缓存
		if (switchCallbackPrxCacheMap.containsKey(sn)) {
			SwitchCallbackPrxCache switchCallbackPrxCache = switchCallbackPrxCacheMap.get(sn);
			if (ipConn.remoteAddress.equals(switchCallbackPrxCache.getIp())
					&& switchCallbackPrxCache.getPort() == ipConn.remotePort) {
				LOGGER.info("该客户端已经存在——already exist cache, return true\n");
				//更新 switchCallbackPrxCache中的时间
				switchCallbackPrxCache.setLastTime(time);
				switchCallbackPrxCache.setLastServerTime(currentTime);
				switchCallbackPrxCacheMap.put(sn, switchCallbackPrxCache);
				return true;
			} else {
				switchCallbackPrxCacheMap.remove(sn);
			}
		}
		ISwitchCallbackPrx switchCallbackPrx = ISwitchCallbackPrxHelper.checkedCast(con.createProxy(id));
		
		
		/*RouterPrx tournt = RouterPrxHelper.checkedCast(con.createProxy(id));
		try {
			SessionPrx session = tournt.createSession("", sn);
		} catch (CannotCreateSessionException e) {
			e.printStackTrace();
			LOGGER.error("创建Session时出错，错误为:"+e);
		} catch (PermissionDeniedException e) {
			e.printStackTrace();
			LOGGER.error("创建Session时出错，错误为:"+e);
		}
		int ACMTimeout =  tournt.getACMTimeout();
		System.out.println("ACMTimeout==============="+ACMTimeout);*/
		
		
		switchCallbackPrxCache = new SwitchCallbackPrxCache();
		switchCallbackPrxCache.setiSwitchCallbackPrx(switchCallbackPrx);
		switchCallbackPrxCache.setIp(ipConn.remoteAddress);
		switchCallbackPrxCache.setPort(ipConn.remotePort);
		switchCallbackPrxCache.setLastTime(time);
		switchCallbackPrxCache.setSn(sn);
		switchCallbackPrxCache.setLastServerTime(currentTime);
		
		switchCallbackPrxCacheMap.put(sn, switchCallbackPrxCache);
		// 如果用户不是定时心跳，而是使用ice自带的心跳必须执行以下代码
		holdHeartbeat(current.con);
		LOGGER.info("注册结束————register end, return true. \n");
		
		return true;
	}

	/**
	 * 刷新客户端信息Map超时的，则移除Map
	 * @param switchCallbackPrxCacheMap  存储客户端连接信息
	 * @param currentTime 当前时间戳，13位 毫秒级别
	 */
	private void refreshMap(ConcurrentHashMap<String, SwitchCallbackPrxCache> switchCallbackPrxCacheMap,long currentTime){
		Iterator<Map.Entry<String, SwitchCallbackPrxCache>> it = switchCallbackPrxCacheMap.entrySet().iterator();  
        while(it.hasNext()){  
            Entry<String, SwitchCallbackPrxCache> entry=it.next();  
            //时间戳之差DT判断
			SwitchCallbackPrxCache cache = entry.getValue();
			long lastTime = cache.getLastServerTime();
			long DT=currentTime-lastTime;
			
			if(DT>20000){
				LOGGER.info("客户端信息超时移除Map————"+entry.getKey());
				it.remove();
			}
        }  
	}
	
	
	/**
	 * ice自带保持心跳
	 * @param con
	 */
	private void holdHeartbeat(Ice.Connection con) {
		con.setCallback(new Ice.ConnectionCallback() {
			@Override
			public void heartbeat(Ice.Connection c) {
				LOGGER.debug("服务开启心跳——service heartbeat...");
			}
			@Override
			public void closed(Ice.Connection c) {
				LOGGER.debug("服务关闭——service close!");
			}
		});
	}

	/**
	 * 客户端可以回调这个接口
	 */
	@Override
	public boolean callBack(String msg, Current current) {
		LOGGER.info("回调开始--ice tcp callBack start. params msg = " + msg);
		boolean result = true;
		Ice.Connection con = current.con;
		Ice.IPConnectionInfo ipConn = (Ice.IPConnectionInfo) con.getInfo();
		if (null != ipConn) {
			LOGGER.info("远程连接地址:ipConn remote:" + ipConn.remoteAddress + ":" + ipConn.remotePort);
			LOGGER.info("本地地址:ipConn local:" + ipConn.localAddress + ":" + ipConn.localPort);
		}

		LOGGER.info("回调结束--ice tcp callBack end. return result = " + result);
		return result;
	}
	/* 
	 * 向客户端sn发送消息msg
	 */
	@Override
	public boolean sendMsgToOtherClient(String otherSn, String msg, Current __current) throws SwitchException {
		//发送信息前刷新下Map，以防Client掉线
		long currentTime = System.currentTimeMillis();//毫秒级别，13位
		refreshMap(switchCallbackPrxCacheMap,currentTime);
		
		//向sn,发送消息
		boolean result = SwitchUtil.sendMsg(otherSn, msg);
		return result;
	}
	

}
