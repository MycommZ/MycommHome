package com.twy.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.twy.domain.Book;
import com.twy.domain.Category;
import com.twy.domain.Orders;
import com.twy.domain.OrdersItem;
import com.twy.domain.User;
import com.twy.service.BusinessService;
import com.twy.service.impl.BusinessServiceImpl;
import com.twy.util.IdGenerator;
import com.twy.util.PaymentUtil;
import com.twy.util.PropertyUtil;
import com.twy.util.SendMail;
import com.twy.util.WebUtil;
import com.twy.web.bean.Cart;
import com.twy.web.bean.CartItem;
import com.twy.web.bean.Page;

public class ClientServlet extends HttpServlet {
	private BusinessService s = new BusinessServiceImpl();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String operation = request.getParameter("operation");
		if("showAllBooks".equals(operation)){
			showAllBooks(request,response);
		}else if("showCategoryBooks".equals(operation)){
			showCategoryBooks(request,response);
		}else if("buy".equals(operation)){
			buy(request,response);
		}else if("genOrders".equals(operation)){
			genOrders(request,response);
		}else if("regist".equals(operation)){
			regist(request,response);
		}else if("login".equals(operation)){
			login(request,response);
		}else if("active".equals(operation)){
			active(request,response);
		}else if("logout".equals(operation)){
			logout(request,response);
		}else if("showSelfOrders".equals(operation)){
			showSelfOrders(request,response);
		}else if("paySuccess".equals(operation)){
			paySuccess(request,response);
		}else if("showOrdersDetail".equals(operation)){
			showOrdersDetail(request,response);
		}
	}
	//锟斤拷锟捷讹拷锟斤拷锟斤拷id锟斤拷询锟斤拷锟斤拷锟斤拷
	private void showOrdersDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String ordersId = request.getParameter("ordersId");
		//锟介订锟斤拷锟斤拷
		List<OrdersItem> items = s.findOrdersItemByOrdersId(ordersId);
		request.setAttribute("items", items);
		request.getRequestDispatcher("/showOrdersDetail.jsp").forward(request, response);
	}
	//锟斤拷锟斤拷支锟斤拷锟斤拷锟斤拷模锟斤拷锟斤拷锟街э拷锟斤拷锟斤拷锟矫讹拷锟斤拷状态
	private void paySuccess(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");//支锟斤拷锟斤拷锟斤拷锟�锟斤拷锟角成癸拷
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
//		为锟斤拷1锟斤拷: 锟斤拷锟斤拷锟斤拷囟锟斤拷锟�
//		 为锟斤拷2锟斤拷: 锟斤拷锟斤拷锟斤拷锟斤拷缘锟酵ㄑ�
		String  hmac= request.getParameter("hmac");
		
		//锟斤拷证锟斤拷锟斤拷锟角凤拷锟斤拷确
		boolean b = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, PropertyUtil.getValue("keyValue"));
		if(!b){
			response.getWriter().write("锟斤拷锟斤拷失锟杰ｏ拷锟斤拷锟杰达拷锟节凤拷锟秸ｏ拷");
			return;
		}
		//锟斤拷锟斤拷没锟斤拷锟斤拷锟斤拷
		if("1".equals(r1_Code)){
			//支锟斤拷锟缴癸拷锟斤拷锟揭碉拷锟斤拷锟斤拷锟斤拷锟斤拷锟侥讹拷锟斤拷锟斤拷状态锟斤拷锟斤拷止锟斤拷锟斤拷锟截革拷锟结交r6_Order
			
//			if("2".equals(r9_BType)){
//				response.getWriter().write("success");
//			}
			//锟揭碉拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷状态为1 锟窖革拷锟斤拷
			s.paiedOrders(r6_Order);
			response.getWriter().write("支锟斤拷锟缴癸拷锟斤拷2锟斤拷锟斤拷远锟阶拷锟斤拷锟斤拷站锟斤拷锟斤拷页");
			response.setHeader("Refresh", "2;url="+request.getContextPath());
		}
	}
	//锟介看锟斤拷前锟矫伙拷锟侥讹拷锟斤拷
	private void showSelfOrders(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		//锟叫讹拷锟矫伙拷锟角凤拷锟铰�		
		User user = (User) request.getSession().getAttribute("user");
		if(user==null){
			//没锟叫碉拷陆锟斤拷转锟斤拷锟铰揭筹拷锟�			
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		}else{
			//锟窖碉拷录锟斤拷锟介看锟斤拷锟斤拷锟斤拷锟斤拷息锟斤拷锟斤拷锟斤拷锟斤拷锟节斤拷锟斤拷锟斤拷锟斤拷 锟斤拷锟斤拷
			List<Orders> orders = s.findOrdersByUserId(user.getId());
			request.setAttribute("os", orders);
			request.getRequestDispatcher("/showOrders.jsp").forward(request, response);
		}
	}
	//注锟斤拷
	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute("user");
		response.getWriter().write("注锟斤拷锟缴癸拷锟斤拷2锟斤拷锟斤拷远锟阶拷锟斤拷锟斤拷站锟斤拷锟斤拷页");
		response.setHeader("Refresh", "2;url="+request.getContextPath());
	}
	//锟斤拷锟斤拷
	private void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");
		if(code!=null){
			User user = s.findUserByCode(code);
			if(user==null){
				request.setAttribute("message", "<script type='text/javascript'>alert('锟斤拷锟斤拷锟诫不锟斤拷确锟斤拷锟斤拷失效')</script>");
				request.getRequestDispatcher("/").forward(request, response);
			}else{
				s.active(user);//锟斤拷锟斤拷锟剿伙拷
				response.getWriter().write("锟斤拷锟斤拷晒锟斤拷锟�锟斤拷锟斤拷远锟阶拷锟斤拷锟斤拷站锟斤拷锟斤拷页");
				response.setHeader("Refresh", "2;url="+request.getContextPath());
			}
		}
	}
	//锟矫伙拷锟斤拷陆
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = s.login(username,password);
		if(user==null){
			request.setAttribute("message", "<script type='text/javascript'>alert('锟斤拷锟斤拷锟斤拷没锟斤拷锟斤拷锟斤拷锟斤拷耄拷锟斤拷锟斤拷锟斤拷锟斤拷嘶锟斤拷锟侥撅拷屑锟斤拷锟�)</script>");
		}else{
			request.getSession().setAttribute("user", user);
		}
		request.getRequestDispatcher("/").forward(request, response);
	}
	//锟斤拷锟矫伙拷注锟斤拷
	private void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = WebUtil.findFormData(User.class, request);
		//锟斤拷锟酵硷拷锟斤拷锟斤拷证锟斤拷:取锟斤拷user锟叫碉拷email锟斤拷锟皆ｏ拷锟斤拷锟斤拷锟绞硷拷锟斤拷锟斤拷锟斤拷锟绞硷拷锟斤拷锟脚碉拷一锟斤拷锟斤拷锟斤拷锟斤拷锟竭筹拷锟叫ｏ拷
		String code = IdGenerator.genPK();
		user.setCode(code);
		
		//SendMail sm = new SendMail(user,request.getContextPath());
		//sm.start();
		user.setActived(true);
		
		s.regist(user);
		request.setAttribute("message", "<script type='text/javascript'>alert('注锟斤拷晒锟�)</script>");
		request.getRequestDispatcher("/").forward(request, response);
		
	}
	//锟斤拷锟斤拷锟矫伙拷锟侥讹拷锟斤拷
	private void genOrders(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		//锟叫讹拷锟矫伙拷锟角凤拷锟铰�		
		User user = (User) request.getSession().getAttribute("user");
		if(user!=null){
			//锟斤拷陆锟剿ｏ拷锟窖癸拷锟斤车锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟较拷锟斤拷娴斤拷锟斤拷菘锟斤拷锟�				//锟窖癸拷锟斤车锟叫碉拷锟斤拷息取锟斤拷锟斤拷锟斤拷锟芥到实锟斤拷锟斤拷
				Cart cart = (Cart)request.getSession().getAttribute("cart");
				if(cart!=null){
					//锟斤拷锟斤拷实锟斤拷锟斤拷锟捷碉拷锟斤拷锟捷匡拷锟斤拷
					Orders o = new Orders();
					o.setNum(cart.getTotalNum());
					o.setMoney(cart.getTotalPrice());
					o.setUser(user);//锟斤拷锟斤拷锟斤拷锟矫伙拷锟侥癸拷系
					//锟斤拷锟斤拷锟斤拷
					for(Map.Entry<String, CartItem> me:cart.getItems().entrySet()){
						OrdersItem item = new OrdersItem();
						item.setNum(me.getValue().getNum());
						item.setPrice(me.getValue().getPrice());
						item.setBook(me.getValue().getBook());
						o.getItems().add(item);//锟窖讹拷锟斤拷锟斤拷锟斤拷氲斤拷锟斤拷锟斤拷锟�					}
					s.genOrders(o);//锟斤拷锟缴讹拷锟斤拷
					}
				}else{
					throw new RuntimeException("锟斤拷锟斤车锟斤拷锟斤拷");
				}
				request.setAttribute("message", "<script type='text/javascript'>alert('锟斤拷锟斤拷锟斤拷锟缴成癸拷')</script>");
				request.getRequestDispatcher("/showOrdersDetail.jsp").forward(request, response);
		}else{
			//没锟叫碉拷陆锟斤拷转锟斤拷锟铰揭筹拷锟�			response.sendRedirect(request.getContextPath()+"/login.jsp");
		}
	}
	//璐拱鏂规硶
	private void buy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//鑾峰彇BOOKID
		String bookId = request.getParameter("bookId");
		//閫氳繃ID鍘绘煡鎵炬暣鏉℃暟鎹�		
		Book book = s.findBookById(bookId);
		//锟饺得癸拷锟斤车,没锟叫ｏ拷锟斤拷锟斤拷一锟斤拷弄锟斤拷去
		HttpSession session = request.getSession();
		Cart cart = (Cart)session.getAttribute("cart");
		if(cart==null){
			cart = new Cart();
			session.setAttribute("cart", cart);
		}
		cart.addBook(book);
		//锟斤拷示锟斤拷锟斤拷晒锟�		request.setAttribute("message", "<script type='text/javascript'>alert('璐拱涓�..')</script>");
     	request.getRequestDispatcher("/showCart.jsp").forward(request, response);
	/*	response.sendRedirect("/client/ClientServlet?operation=showSelfOrders");*/
	
	}
	//锟斤拷锟秸凤拷锟斤拷锟窖拷锟揭筹拷锟斤拷锟�	
	private void showCategoryBooks(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String pagenum = request.getParameter("pagenum");
		String categoryId = request.getParameter("categoryId");
		Page page = s.findAllBooksByCategory(pagenum,categoryId);
		page.setUrl("/client/ClientServlet?operation=showCategoryBooks&categoryId="+categoryId);
		request.setAttribute("page", page);
		request.getRequestDispatcher("/listBooks.jsp").forward(request, response);
	}
	//棣栭〉灞曠ず
	private void showAllBooks(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pagenum = request.getParameter("pagenum");
		Page page = s.findAllBooks(pagenum);
		page.setUrl("/client/ClientServlet?operation=showAllBooks");
		request.setAttribute("page", page);
		//锟斤拷询锟斤拷锟叫凤拷锟斤拷锟斤拷息
		List<Category> cs = (List<Category>) getServletContext().getAttribute("cs");
		if(cs==null||cs.size()<1){
			cs = s.findAllCategories();
			getServletContext().setAttribute("cs", cs);
		}
		request.getRequestDispatcher("/listBooks.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
