package listener;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import controller.Controller;

@WebListener
public class HandlerMappingListener implements ServletContextListener {
   
    public void contextDestroyed(ServletContextEvent sce)  { }

	/**
	 * 미리 생성할 객체를 생성해서 Map에 저장하고 
	 * Map에 application영역에 저장한다.
	 * */
    public void contextInitialized(ServletContextEvent e)  { 
    	//context-param정보 가져오기
    	ServletContext application = e.getServletContext();
    	String fileName = application.getInitParameter("fileName");
    	
    	Map<String, Controller> map = new HashMap<String, Controller>();
    	Map<String, Class<?>> clzMap = new HashMap<String, Class<?>>(); 
    	
       //미리 생성해야하는 객체에 대한 정보를 가지고 있는
    	//~.properties파일을 로딩하기
    	
    	//baseName에 확장자는 생략한다.
    	ResourceBundle rb = ResourceBundle.getBundle(fileName);
    	for(String key : rb.keySet()) {
    		String value = rb.getString(key);
    		//System.out.println("key: " + key + ", value: " + value);
    		
    		//String 형태인 value를 객체로 만든다 
    		try {
    			Class<?> cls = Class.forName(value);
    			Controller controller = (Controller)cls.newInstance();
    			
    			System.out.println("value: " + value);
    			System.out.println("controller: " + controller);

				map.put(key, controller); //Controller 담기 
				clzMap.put(key, cls); //메소드 담기 
				//System.out.println(key + " = " + controller);
				System.out.println("map: " + map);
				System.out.println("clzMap: " + clzMap);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
    	}
    	
    	application.setAttribute("map", map); //scope에 저장 
    	application.setAttribute("clzMap", clzMap); //scope에 저장 
    	 
    	
    }//메소드끝
	
}