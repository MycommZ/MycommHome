package com.twy.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.twy.domain.Manager;
import com.twy.domain.Menu;
import com.twy.domain.Role;
import com.twy.service.ManagerService;
import com.twy.service.impl.ManagerServiceImpl;
//����URI��Ȩ������
public class PermissionFilter implements Filter {
	private ManagerService s = new ManagerServiceImpl();
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		String uri = request.getRequestURI();//    /day19_shopping/manager/showDetails.jsp
			//�ж��û��Ƿ��½
			HttpSession session = request.getSession();
			Manager manager = (Manager) session.getAttribute("manager");
			//û�е�½��ת���½ҳ��
			if(manager==null){
				response.sendRedirect(request.getContextPath()+"/login/login.jsp");
				return;
			}else{
				//�е�½�������û���ѯ��Ӧ�Ľ�ɫ��������ɫ���õ��ܷ��ʵĲ˵���Set<Menu>��
				Set<Menu> menus = new HashSet<Menu>();//�û����Է��ʵĲ˵�
				List<Role> roles = s.findRoleByManagerId(manager.getId());//���ݵ�ǰ��¼�û���ѯ�û����Է��ʵĽ�ɫ
				if(roles!=null&&roles.size()>0){
					for(Role r:roles){
						//���ݽ�ɫ��ѯ���Է��ʵĲ˵�
						List<Menu> ms = s.findMenuByRoleId(r.getId());
						if(ms!=null&&ms.size()>0){
							for(Menu m:ms)
								menus.add(m);//�ѿ��Է��ʵĲ˵��ŵ�Set��
						}
					}
				}
				//�õ��û���ǰ���ʵ���Դ��uri��  /day19_shopping/manager/showDetails.jsp--->/manager/showDetails.jsp
				uri = uri.replace(request.getContextPath(), "");//   /manager/showDetails.jsp
				//�����Ǹ�Set���������а���uri��
				boolean hasPermission = false;//�Ƿ���Ȩ��
				for(Menu m:menus){
					if(uri.equals(m.getUri())){
						hasPermission = true;
						break;
					}
				}
				
				if(!hasPermission){
					//��������֪ͨû��Ȩ��
					response.getWriter().write("�Բ�����û��Ȩ��");
					response.setHeader("Refresh", "2;URL="+request.getContextPath()+"/login/index.jsp");
					return;
				}
			}
		chain.doFilter(request, response);
	}
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	

	public void destroy() {

	}

}
