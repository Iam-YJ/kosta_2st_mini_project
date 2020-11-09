package controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 사용자 요청을 중앙집중적으로 관리해줄 FrontController : 사용자 요청을 받아서 그에 해당하는 Controller를 찾아서
 * 호출하고 그 결과를(ModelAndView) 받아서 해당하는 뷰페이지로 이동시킨다
 */
@WebServlet(urlPatterns = "/dispatcher", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
	private Map<String, Controller> map;
	private Map<String, Class<?>> clzMap;

	@Override
	public void init() throws ServletException {
		System.out.println("init call");
		map = (Map<String, Controller>) super.getServletContext().getAttribute("map");
		clzMap = (Map<String, Class<?>>) super.getServletContext().getAttribute("clzMap");

	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("service 요청됨");

		String key = request.getParameter("key");
		System.out.println("key: " + key);
		String methodName = request.getParameter("methodName");
		System.out.println("mathodName: " + methodName);

		Controller con = map.get(key);
		Class<?> cls = clzMap.get(key);
		System.out.println("con: " + con);
		System.out.println("cls: " + cls);

		try {
			Method method = cls.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			System.out.println("method: " + method);
			// cls 안에서 methodName에 해당하는 정보를 얻어온 것이다

			ModelAndView mv = (ModelAndView) method.invoke(con, request, response);
			// 메소드를 호출하는 것
			System.out.println(mv.getViewName());

			if (mv.isRedirect()) {
				response.sendRedirect(mv.getViewName());
			} else {
				request.getRequestDispatcher(mv.getViewName()).forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}

