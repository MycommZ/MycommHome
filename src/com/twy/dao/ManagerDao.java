package com.twy.dao;

import java.util.List;

import com.twy.domain.Manager;
import com.twy.domain.Role;

public interface ManagerDao {

	Manager findUser(String username, String password);
	/**
	 * �����û�id��ѯ��ɫ
	 * @param id
	 * @return
	 */
	List<Role> findRoleById(String id);

}
