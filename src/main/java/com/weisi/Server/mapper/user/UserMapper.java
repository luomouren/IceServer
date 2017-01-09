package com.weisi.Server.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weisi.Server.bean.ClientUserInfo;

public interface UserMapper {
	void save(ClientUserInfo user) ;
	boolean update(ClientUserInfo user);
	boolean delete(int id);
	ClientUserInfo findById(int id);
	List<ClientUserInfo> findAll();
	
	ClientUserInfo findByUserNamePwd(@Param("userName")String userName,@Param("password")String pwd);
}
