package com.trunko.filters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.HttpKit;
import com.trunko.anoation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

/**
 * 跨域拦截器
 */
public   class CrossOriginInterceptor  implements Interceptor{
    public void intercept(Invocation inv) {
    	

        
        CrossOrigin cross = inv.getController().getClass().getAnnotation(CrossOrigin.class);
        if (cross != null) {
            handler(inv.getController().getRequest(),inv.getController().getResponse());
            inv.invoke();
            return;
        }
        cross = inv.getMethod().getAnnotation(CrossOrigin.class);
        if (cross != null) {
            handler(inv.getController().getRequest(),inv.getController().getResponse());
            inv.invoke();
            return;
        }
        inv.invoke();
    }

    
    private void handler(HttpServletRequest request,HttpServletResponse response) {

    	//指定多个 允许跨域的域名
    	String [] allowDomain= {"http://192.168.1.15:8080","http://192.168.1.9:8080","http://192.168.1.56:8080","http://localhost:8080","http://192.168.1.15:8020","http://192.168.1.5:8080"}; 
    	Set<String> allowedOrigins= new HashSet<String>(Arrays.asList(allowDomain)); 
    	String originHeader=request.getHeader("Origin"); 
/*    	  if("OPTIONS".equals(request.getMethod())){
         	    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
         	   System.out.println("111111");
         	  response.setHeader("Access-Control-Allow-Origin", originHeader);
              response.setHeader("Access-Control-Allow-Credentials", "true");
              response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS,DELETE,PUT");
              response.setHeader("Access-Control-Max-Age", "3600");
              response.setHeader("Access-Control-Allow-Headers",
                      "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,Origin,Token,Accept, Connection, User-Agent, Cookie");
         	    return;
           }else{
        	
           	if (allowedOrigins.contains(originHeader)){ 
	           	 if("OPTIONS".equals(request.getMethod())){
	           		 response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	           	 }
                   response.setHeader("Access-Control-Allow-Origin", originHeader);
                   response.setHeader("Access-Control-Allow-Credentials", "true");
                   response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS,DELETE,PUT");
                   response.setHeader("Access-Control-Max-Age", "3600");
                   response.setHeader("Access-Control-Allow-Headers",
                           "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,Origin,Token,Accept, Connection, User-Agent, Cookie");

                   
           	}
          }*/
    	
    	if (allowedOrigins.contains(originHeader)){ 
          	 if("OPTIONS".equals(request.getMethod())){
          		 response.setStatus(HttpServletResponse.SC_NO_CONTENT);
          	
          	 }
              response.setHeader("Access-Control-Allow-Origin", originHeader);
              response.setHeader("Access-Control-Allow-Credentials", "true");
              response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS,DELETE,PUT");
              response.setHeader("Access-Control-Max-Age", "3600");
              response.setHeader("Access-Control-Allow-Headers",
                      "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,Origin,Token,Accept, Connection, User-Agent, Cookie");

              
      	}
    	

    }
}
