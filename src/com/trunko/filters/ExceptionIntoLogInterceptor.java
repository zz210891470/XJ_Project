package com.trunko.filters;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.JFinal;
import org.apache.log4j.Logger;

/**
 * 寮傚父澶勭悊鏃ュ織 鍏ㄥ眬鎷︽埅鍣�
**/
public abstract class ExceptionIntoLogInterceptor implements Interceptor {
    private static final  Logger log = Logger.getLogger(ExceptionIntoLogInterceptor.class);


    public void intercept(Invocation invocation) {
            //Controller controller=invocation.getController();
            //HttpServletRequest request=controller.getRequest();
            try{
                invocation.invoke(); //涓�畾瑕佹敞鎰忥紝鎶婂鐞嗘斁鍦╥nvoke涔嬪悗锛屽洜涓烘斁鍦ㄤ箣鍓嶇殑璇濓紝鏄細绌烘寚閽�
            }catch (Exception e){
                //log 澶勭悊
                logWrite(invocation, e);
            }finally {
                //璁板綍鏃ュ織鍒版暟鎹簱锛屾殏鏈疄鐜�
                try{

                }catch (Exception ee){

                }
            }



    }

    private void logWrite(Invocation inv, Exception e){
        //寮�彂妯″紡 涓嬬洿鎺ユ姏鍑哄紓甯稿埌鎺у埗鍙颁笉璁板綍鍒版棩蹇楁枃浠朵腑
        if (JFinal.me().getConstants().getDevMode()){
            e.printStackTrace();
        }
        StringBuilder sb =new StringBuilder("\n---Exception Log Begin---\n");
        sb.append("Controller:").append(inv.getController().getClass().getName()).append("\n");
        sb.append("Method:").append(inv.getMethodName()).append("\n");
        sb.append("Exception Type:").append(e.getClass().getName()).append("\n");
        sb.append("Exception Details:");
        log.error(sb.toString(),e);

    }


}
