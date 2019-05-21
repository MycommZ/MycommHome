package com.twy.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orders implements Serializable {
	private String id;
	private String ordersnum;//������
	private int num;//�ĵ��е���Ʒ����
	private float money;//Ӧ�����
	private Date createtime;//�µ�ʱ��   sql��timestamp
	private int status = 0;//0 δ���� 1�Ѹ��� 2�ѷ���
	private User user;
	private List<OrdersItem> items = new ArrayList<OrdersItem>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrdersnum() {
		return ordersnum;
	}
	public void setOrdersnum(String ordersnum) {
		this.ordersnum = ordersnum;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<OrdersItem> getItems() {
		return items;
	}
	public void setItems(List<OrdersItem> items) {
		this.items = items;
	}
	
}
