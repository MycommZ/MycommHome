package com.twy.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.twy.dao.OrdersDao;
import com.twy.domain.Book;
import com.twy.domain.Orders;
import com.twy.domain.OrdersItem;
import com.twy.domain.User;
import com.twy.exception.DaoException;
import com.twy.util.DBCPUtil;

public class OrdersDaoImpl implements OrdersDao {
	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	
	
	public void addOrders(Orders o) {

		try{
			//���涩���Ļ�����Ϣ
			qr.update("insert into orders(id,ordersnum,num,money,status,userId) values(?,?,?,?,?,?)", 
					o.getId(),o.getOrdersnum(),o.getNum(),o.getMoney(),o.getStatus(),o.getUser().getId());
			//�ж϶�������ľ�ж��������б��涩������Ϣ
			List<OrdersItem> items = o.getItems();
			if(items!=null&&items.size()>0){
				Object params[][] = new Object[items.size()][];
				String sql = "insert into orders_item(id,num,price,bookId,ordersId) values(?,?,?,?,?)";
				for(int i=0;i<items.size();i++){
					OrdersItem item = items.get(i);
					params[i] = new Object[]{item.getId(),item.getNum(),item.getPrice(),item.getBook().getId(),o.getId()};
				}
				qr.batch(sql, params);
			}
		}catch(Exception e){
			throw new DaoException(e);
		}
	}
	public List<Orders> findOrdersByUserId(String userId) {
		try{
			return qr.query("select * from orders where userId=? order by createtime desc", new BeanListHandler<Orders>(Orders.class), userId);
		}catch(Exception e){
			throw new DaoException(e);
		}
	}
	public void updateOrders(String ordersnum,int status) {
		try{
			qr.update("update orders set status=? where ordersnum=?", 
					status,ordersnum);
		}catch(Exception e){
			throw new DaoException(e);
		}
	}
	public List<OrdersItem> findOrdersItemByOrdersId(String ordersId) {
		try{
			List<OrdersItem> items = qr.query("select * from orders_item where ordersId=?", new BeanListHandler<OrdersItem>(OrdersItem.class), ordersId);
			//�������������ѯ����
			if(items!=null&&items.size()>0){
				for(OrdersItem item:items){
					Book book = qr.query("select * from book where id=(select bookId from orders_item where id=?)", new BeanHandler<Book>(Book.class), item.getId());
					item.setBook(book);
				}
			}
			return items;
		}catch(Exception e){
			throw new DaoException(e);
		}
	}
	public List<Orders> findOrdersByStatus(int i) {
		try{
			//���ݶ���״̬��ѯ����
			List<Orders> orders = qr.query("select * from orders where status=?", new BeanListHandler<Orders>(Orders.class), i);
			//�Ѷ����Ĺ����˲�ѯ����
			if(orders!=null&&orders.size()>0){
				for(Orders o:orders){
					User user = qr.query("select u.* from user u,orders o where u.id=o.userId and o.id=?", new BeanHandler<User>(User.class), o.getId());
					o.setUser(user);
				}
			}
			return orders;
		}catch(Exception e){
			throw new DaoException(e);
		}
	}
	@Override
	public void deleteAll(String sid) {
		try {
//			qr.update(DBCPUtil.getConnection(), "DELETE  FROM  ORDERS where id in (SELECT  ordersId from  ORDERS_item where BOOKID =?)", sid);
			qr.update(DBCPUtil.getConnection(), "DELETE FROM   ORDERS_item  WHERE  BOOKID=?", sid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void deleAll(String id) {
		try {
			qr.update(DBCPUtil.getConnection(), "DELETE FROM   ORDERS  WHERE  id=?", id);
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		
	}
	@Override
	public void xiuG(String id) {
		try {
			qr.update("update orders set (?,?,?,?,?,?) where id=?",id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
