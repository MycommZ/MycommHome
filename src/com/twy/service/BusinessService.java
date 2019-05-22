package com.twy.service;

import java.util.List;

import com.twy.domain.Book;
import com.twy.domain.Category;
import com.twy.domain.Orders;
import com.twy.domain.OrdersItem;
import com.twy.domain.User;
import com.twy.web.bean.Page;

public interface BusinessService {

	void addCategory(Category c);

	List<Category> findAllCategories();

	void addBook(Book book);
	/**
	 * ��ѯ�����鼮�ķ�ҳ����
	 * @param pagenum
	 * @return
	 */
	Page findAllBooks(String pagenum);

	Category findCategoryById(String categoryId);
	/**
	 * ���շ����ѯ��ҳ����
	 * @param pagenum
	 * @param categoryId
	 * @return
	 */
	Page findAllBooksByCategory(String pagenum, String categoryId);

	Book findBookById(String bookId);

	void regist(User user);
	/**
	 * �ж��û��������Ƿ���ȷ�����Ҽ���ʱ�ŷ�������
	 * @param username
	 * @param password
	 * @return
	 */
	User login(String username, String password);

	User findUserByCode(String code);

	void active(User user);

	void genOrders(Orders o);

	List<Orders> findOrdersByUserId(String id);
	//��Ϊ�Ѹ���
	void paiedOrders(String r6_Order);
	//���ݶ���id��ѯ�����ͬʱ�Ѷ������������Ҳ�����
	List<OrdersItem> findOrdersItemByOrdersId(String ordersId);
	//����״̬��ѯ������Ϣ����Ҫ���û�
	List<Orders> findOrdersByStatus(int i);
	//����
	void sendBook(String ordersNum);

	
	/**
	 * 业务层删除方法
	 * @param sid
	 */
	void deleteAll(String sid);

	void deleAll(String id);

	void xiuG(String id);

}
