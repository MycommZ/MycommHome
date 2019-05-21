package com.twy.web.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.twy.domain.Book;

public class Cart implements Serializable {
	//key:�鼮��id  value���鼮��Ӧ�Ĺ�����
	private Map<String, CartItem> items = new HashMap<String, CartItem>();
	private int totalNum;//������
	private float totalPrice;//�ܼƣ�Ӧ�����
	public Map<String, CartItem> getItems() {
		return items;
	}
	public int getTotalNum() {
		totalNum = 0;
		for(Map.Entry<String, CartItem> me:items.entrySet()){
			totalNum+=me.getValue().getNum();
		}
		return totalNum;
	}
	public float getTotalPrice() {
		totalPrice = 0;
		for(Map.Entry<String, CartItem> me:items.entrySet()){
			totalPrice+=me.getValue().getPrice();
		}
		return totalPrice;
	}
	//��һ������ӵ�items��
	public void addBook(Book book){
		if(items.containsKey(book.getId())){
			CartItem item = items.get(book.getId());
			item.setNum(item.getNum()+1);
		}else{
			CartItem item = new CartItem(book);
			item.setNum(1);
			items.put(book.getId(), item);
		}
	}
}
