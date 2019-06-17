package com.trunko.web.controller.sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.trunko.anoation.CrossOrigin;
import com.trunko.common.ConstsObject;
import com.trunko.utils.SmsUtils;
import com.trunko.web.dao.sms.SmsModel;
import com.trunko.web.dao.sms.SmsUserModel;

/**
 * 发送短信控制器
 * @author Administrator
 *
 */
@CrossOrigin
public class SmsController extends Controller{
	private static final  Logger log = Logger.getLogger(SmsController.class);
	
	public void sendMsg(){
/*		   JSONArray  users = jo.getJSONArray("receive_users");
		   JSONArray  phones = jo.getJSONArray("receive_mobiles");
		   System.out.println("电话字符串"+phones.toString());
		   List<Record>user_list = new ArrayList<Record>();
			 for (int i = 0; i < users.size(); i++) {
				Record u = new Record();
				u.set("msg_pid", msg_id);
				u.set("user_name", users.get(i));
				u.set("user_mobile", phones.get(i));
				user_list.add(u);
			}*/
			    
	       Map<String,Object> map = new HashedMap();
	       String formStr = HttpKit.readData(getRequest());
	       System.out.println("短信表单:"+formStr);
	       JSONObject jo = JSONObject.fromObject(formStr);
		   String send_username = jo.getString("send_username");
		   String receive_users = jo.getString("receive_users");
		   String receive_mobiles = jo.getString("receive_mobiles");
		   String send_content = jo.getString("send_content");
		   String org_id = jo.getString("org_id");
		   //发送短信
		   String msg = SmsUtils.sendMsg(receive_mobiles, send_content);
			 Document doc;
				try {
					doc = DocumentHelper.parseText(msg);
					 Element root =doc.getRootElement();
					 String result = root.elementText("returnstatus");
					 
					 Record  sms = new Record();
					 sms.set("send_username", send_username);
					 sms.set("send_content", send_content);
					 sms.set("send_time", new Date());
					 sms.set("msg_org_id",org_id);
					 boolean flag = SmsModel.dao.saveMsg(sms);
					 if(flag){
						 int msg_id = sms.getInt("msg_id");
						 String[] users = receive_users.split(",");
						 String [] phones = receive_mobiles.split(",");
						 List<Record>user_list = new ArrayList<Record>();
						 for (int i = 0; i < users.length; i++) {
							Record u = new Record();
							u.set("msg_pid", msg_id);
							u.set("user_name", users[i]);
							u.set("user_mobile", phones[i]);
							user_list.add(u);
						}
						 
						 
						 if(user_list.size()>0){
							 //保存接收用户
							 SmsUserModel.dao.saveReceiveUsers(user_list); 
						 }
					 }
					 
					 if("Success".equals(result)){
						 map.put("code", ConstsObject.SUCCESS_CODE);
			    		 map.put("msg", ConstsObject.SEND_SUCCESS_MSG);
			    		 renderJson(map);
					 }else{
						  map.put("code", ConstsObject.ERROR_CODE);
			    		  map.put("msg", ConstsObject.SEND_ERROR_MSG);
			    		  renderJson(map);
					 }

				} catch (DocumentException e) {
					//如果不写这段代码 由于 捕获了异常   异常拦截器不会将 错误记录到错误日志中  所以 在这里需要手动 记录到日志
			        StringBuilder sb =new StringBuilder("\n---Exception Log Begin---\n");
			        sb.append("Exception Type:").append(e.getClass().getName()).append("\n");
			        sb.append("Exception Details:");
			        log.error(sb.toString(),e);
			        map.put("code", ConstsObject.ERROR_CODE);
			        map.put("msg", ConstsObject.SEND_ERROR_MSG);
					renderJson(map);
					return;
					
				}
	}
	
	//获取短信历史记录
	public void getMsgList(){
		 Map<String,Object> map = new HashedMap();
		try {
			String org_id = getPara("org_id");
			
			if(org_id != null&&!"".equals(org_id)){
				
				   int pageNo = ConstsObject.PAGE_NO;
				   String pageNo_str = getPara("page");
				   if(pageNo_str != null&&!"".equals(pageNo_str)){
					   pageNo = Integer.valueOf(pageNo_str);
				   }
				   
				   int pageSize = ConstsObject.PAGE_SIZE;
				   String pageSize_str = getPara("limit");
				   if(pageSize_str != null&&!"".equals(pageSize_str)){
					   pageSize = Integer.valueOf(pageSize_str);
				   }
				String keyword = getPara("keyword");
				 if(keyword == null){
					 keyword = "";
				 }
				
				Page<Record>msglist= SmsModel.dao.getMsgList(org_id, pageNo, pageSize,keyword);
				map.put("data",msglist);
		        map.put("code", ConstsObject.SUCCESS_CODE);
		        map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
				renderJson(map);
				
			}else{
				 map.put("code", ConstsObject.ERROR_CODE);
			     map.put("msg", ConstsObject.SEARCH_ERROR_MSG);
			     renderJson(map);
				
			}
		
			
		} catch (Exception e) {
			//如果不写这段代码 由于 捕获了异常   异常拦截器不会将 错误记录到错误日志中  所以 在这里需要手动 记录到日志
	        StringBuilder sb =new StringBuilder("\n---Exception Log Begin---\n");
	        sb.append("Exception Type:").append(e.getClass().getName()).append("\n");
	        sb.append("Exception Details:");
	        log.error(sb.toString(),e);
	        map.put("code", ConstsObject.ERROR_CODE);
	        map.put("msg", ConstsObject.SEARCH_ERROR_MSG);
			renderJson(map);
			return;
		}
		
		
	}
	
	//短信详情
	public void getMsgDetail(){
	    Map<String,Object> map = new HashedMap();
		String ms_id = getPara("msg_id");
		if(ms_id != null&&!"".equals(ms_id)){
		    int msg_id = Integer.valueOf(ms_id);
		    Record msg = SmsModel.dao.getMsg(msg_id);
		    List<Record>rec_userlist = SmsUserModel.dao.getRecUserlist(msg_id);
		    Record r = new Record();
		    r.set("msgs", msg).set("users",rec_userlist);
		    map.put("data", r);
		    map.put("code", ConstsObject.SUCCESS_CODE);
	        map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
			renderJson(map);
			
		}else{
			 map.put("code", ConstsObject.ERROR_CODE);
		     map.put("msg", ConstsObject.SEARCH_ERROR_MSG);
		     renderJson(map);
			
		}
		
		
	}
	
	//删除短信
	public void delMsg(){
		Map<String,Object> map = new HashedMap();
		String ms_id = getPara("msg_id");
		if(ms_id != null&&!"".equals(ms_id)){
			int msg_id = Integer.valueOf(ms_id);
			boolean flag = SmsModel.dao.delMsg(msg_id);
			if(flag){
				SmsUserModel.dao.delMsgList(msg_id);
				 map.put("code", ConstsObject.SUCCESS_CODE);
			     map.put("msg", ConstsObject.DEL_SUCCESS_MSG);
			     renderJson(map);
			}
		}else{
			 map.put("code", ConstsObject.ERROR_CODE);
		     map.put("msg", ConstsObject.DEL_ERROR_MSG);
		     renderJson(map);
		}
		
		
		
	}
	
	

}
