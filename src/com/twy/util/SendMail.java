package com.twy.util;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.twy.domain.User;

public class SendMail extends Thread {
	private User user;
	private String contextPath;
	public SendMail(User user, String contextPath) {
		this.user = user;
		this.contextPath =  contextPath;
	}
	public void run() {
		try {
			//�õ�Session
			Session session = MailSessionUtil.getSession();
			session.setDebug(true);
			//�����ʼ�����
			MimeMessage msg = new MimeMessage(session);
//			msg.setFrom(new InternetAddress("itheimacloud@163.com"));//�ٷ��˻�noreply@163.com webmaster@163.com
			msg.setFrom(new InternetAddress("master@itheima.com"));//�ٷ��˻�noreply@163.com webmaster@163.com
			msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(user.getEmail()));
			msg.setSubject("��ӭ����СС��꣬�뼤�������ʺ�");
			
			msg.setContent("�װ��Ļ�Ա��"+user.getUsername()+"<br/>��ӭ���룬�����������Ӽ��������˻�<br/><a href='http://localhost:8080"+contextPath+"/client/ClientServlet?operation=active&code="+user.getCode()+"'>����</a><br/>����һ��ϵͳ�ʼ�������ظ���", "text/html;charset=UTF-8");
			msg.saveChanges();
			
			//�����ʼ�
			Transport ts = session.getTransport();
//			ts.connect("itheimacloud", "iamsorry");//����
			ts.connect("master", "123");
			ts.sendMessage(msg, msg.getAllRecipients());
			ts.close();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	

}
