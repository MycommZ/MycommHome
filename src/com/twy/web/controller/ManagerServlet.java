package com.twy.web.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.twy.domain.Book;
import com.twy.domain.Category;
import com.twy.domain.Manager;
import com.twy.domain.Menu;
import com.twy.domain.Orders;
import com.twy.service.BusinessService;
import com.twy.service.ManagerService;
import com.twy.service.impl.BusinessServiceImpl;
import com.twy.service.impl.ManagerServiceImpl;
import com.twy.util.IdGenerator;
import com.twy.util.WebUtil;
import com.twy.web.bean.Page;

public class ManagerServlet extends HttpServlet {
	private BusinessService s = new BusinessServiceImpl();
	private ManagerService ms = new ManagerServiceImpl();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String operation = request.getParameter("operation");
		if("addCategory".equals(operation)){
			addCategory(request,response);
		}else if("showAllCategories".equals(operation)){
			showAllCategories(request,response);
		}else if("showAddBook".equals(operation)){
			showAddBook(request,response);
		}else if("addBook".equals(operation)){
			addBook(request,response);
		}else if("showBooks".equals(operation)){
			showBooks(request,response);
		}else if("showPayedOrders".equals(operation)){
			showPayedOrders(request,response);
		}else if("sendBook".equals(operation)){
			sendBook(request,response);
		}else if("login".equals(operation)){
			login(request,response);
		}
	}
	//��̨�û��ĵ�½
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Manager m = ms.login(username,password);
		if(m!=null){
			request.getSession().setAttribute("manager", m);
			//�Ѳ˵��������
			List<Menu> menus = ms.findAllMenus();//��ѯ�����еĲ˵�
			getServletContext().setAttribute("menus", menus);//�ŵ�Ӧ������
			request.getRequestDispatcher("/login/index.jsp").forward(request, response);
		}else{
			request.getRequestDispatcher("/login/login.jsp").forward(request, response);
		}
	}
	//ȷ�Ϸ���
	private void sendBook(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String ordersNum = request.getParameter("ordersNum");
		s.sendBook(ordersNum);
		response.sendRedirect(request.getContextPath()+"/servlet/ManagerServlet?operation=showPayedOrders");
	}
	//�鿴�Ѹ���Ķ���
	private void showPayedOrders(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String status = request.getParameter("status");
		int numStatus = 1;
		if(status!=null){
			numStatus = Integer.parseInt(status);
		}
		List<Orders> orders = s.findOrdersByStatus(numStatus);//1 �Ѹ���
		request.setAttribute("numStatus", numStatus);
		request.setAttribute("os", orders);
		request.getRequestDispatcher("/manager/showOrders.jsp").forward(request, response);
	}
	//��ѯ��ҳ���ݣ��鼮
	private void showBooks(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pagenum = request.getParameter("pagenum");
		Page page = s.findAllBooks(pagenum);
		page.setUrl("/manager/ManagerServlet?operation=showBooks");
		request.setAttribute("page",page);
		request.getRequestDispatcher("/manager/listBooks.jsp").forward(request, response);
	}
	//�����鼮��Ϣ���õ����ļ��ϴ�
	private void addBook(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload sfu = new ServletFileUpload(factory);
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if(!isMultipart)//�Ų���
				throw new RuntimeException("������");
			List<FileItem> items = sfu.parseRequest(request);
			Book book = new Book();
			//�õ����ͼƬ���ļ��е���ʵ·��
			String storeDirectory = getServletContext().getRealPath("/images");
			for(FileItem item:items){
				if(item.isFormField()){
					//��ͨ�ֶ�
					String fieldName = item.getFieldName();//�ֶ���   name
					String fieldValue = item.getString(request.getCharacterEncoding());//java
					BeanUtils.setProperty(book, fieldName, fieldValue);//  book.setName("java");
				}else{
					//�ϴ��ֶ�
					String fileName = item.getName();//   java.jpg
					//��ȡ�ļ�����չ��     jpg
					String extendFilename = fileName.substring(fileName.lastIndexOf(".")+1);//jpg
					String newFileName = IdGenerator.genPK()+"."+extendFilename;//sdfjlkdsflkdskjlk23432.jpg
					book.setPhoto(newFileName);//��������ͼƬ��������Ϣ
					
					//��ʼ�����ļ����ϴ�
					InputStream in = item.getInputStream();
					OutputStream out = new FileOutputStream(storeDirectory+"/"+newFileName);
					int len = -1;
					byte b[] = new byte[1024];
					while((len=in.read(b))!=-1){
						out.write(b, 0, len);
					}
					in.close();
					out.close();
					item.delete();
				}
			}
			s.addBook(book);
			request.setAttribute("message", "<script type='text/javascript'>alert('��ӳɹ�')</script>");
			request.getRequestDispatcher("/manager/addBook.jsp").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	//ת������鼮��ҳ�棬Ҫ��ѯ����
	private void showAddBook(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Category> cs = (List<Category>) getServletContext().getAttribute("cs");
		if(cs==null||cs.size()<1){
			cs = s.findAllCategories();
			getServletContext().setAttribute("cs", cs);
		}
		
		request.getRequestDispatcher("/manager/addBook.jsp").forward(request, response);
	}
	//��ѯ���з���
	private void showAllCategories(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Category> cs = (List<Category>) getServletContext().getAttribute("cs");
		if(cs==null||cs.size()<1){
			cs = s.findAllCategories();
			getServletContext().setAttribute("cs", cs);
		}
		
		request.getRequestDispatcher("/manager/listCategory.jsp").forward(request, response);
	}
	//��ӷ�����Ϣ�����ݿ���
	private void addCategory(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Category c = WebUtil.findFormData(Category.class, request);//��װ������Ϣ����
		s.addCategory(c);
		request.setAttribute("message", "<script type='text/javascript'>alert('��ӳɹ�')</script>");
		request.getRequestDispatcher("/manager/addCategory.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
