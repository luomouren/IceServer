package com.weisi.Server.service.user;

import java.util.List;

import com.weisi.Server.bean.ClientUserInfo;

public interface UserService {
	void save(ClientUserInfo user) ;
	boolean update(ClientUserInfo user);
	boolean delete(int id);
	ClientUserInfo findById(int id);
	List<ClientUserInfo> findAll();
	ClientUserInfo findByUserNamePwd(String userName,String pwd);
}
