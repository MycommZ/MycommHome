package com.twy.service;

import java.util.List;

import com.twy.domain.Manager;
import com.twy.domain.Menu;
import com.twy.domain.Role;

public interface ManagerService {

	Manager login(String username, String password);

	List<Menu> findAllMenus();
	/**
	 * ����U�û���id��ѯ���Ľ�ɫ
	 * @param id
	 * @return
	 */
	List<Role> findRoleByManagerId(String id);
	/**
	 * ���ݽ�ɫid��ѯ���Ĳ˵�
	 * @param id
	 * @return
	 */
	List<Menu> findMenuByRoleId(String id);

}
