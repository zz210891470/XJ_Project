package com.trunko.web.controller.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Record;
import com.trunko.anoation.CrossOrigin;
import com.trunko.common.ConstsObject;
import com.trunko.web.dao.info.InfoModel;
import com.trunko.web.dao.info.InfoUserModel;
/**
 * 信息发布控制器
 * @author Administrator
 *
 */
@CrossOrigin
public class InfoController extends Controller{
	
	private static final  Logger log = Logger.getLogger(InfoController.class);
	public void saveInfo(){
		Map<String,Object> map = new HashedMap();
	    String formStr = HttpKit.readData(getRequest());
	    System.out.println("保存消息字符串:"+formStr);
	    JSONObject jo = JSONObject.fromObject(formStr);
	    try {
	    	Record info =  new Record();
			info.set("info_title", jo.get("info_title"))
			.set("info_content",jo.get("info_content"))
			.set("info_type",jo.get("info_type"))
			.set("info_org_id",jo.get("info_org_id"));
			boolean flag = InfoModel.dao.saveInfo(info);
			
			if(flag){
				JSONArray ja = jo.getJSONArray("org_user");
				List<Record>user_list = new ArrayList<Record>();
				for (int i = 0; i < ja.size(); i++) {
					Record user = new Record();
					user.set("info_pid", info.get("info_id"));
					user.set("info_username", ja.get(i));
					user_list.add(user);
				}
				
				if(user_list.size()>0){
					InfoUserModel.dao.saveUsers(user_list);
				}
				    map.put("data",info.get("info_id")); //上传附件 使用
				    map.put("code", ConstsObject.SUCCESS_CODE);
			        map.put("msg", ConstsObject.SAVE_SUCCESS_MSG);
					renderJson(map);
			}else{
				
				    map.put("code", ConstsObject.ERROR_CODE);
			        map.put("msg", ConstsObject.SAVE_ERROR_MSG);
					renderJson(map);
				
			}
		} catch (Exception e) {
			//如果不写这段代码 由于 捕获了异常   异常拦截器不会将 错误记录到错误日志中  所以 在这里需要手动 记录到日志
	        StringBuilder sb =new StringBuilder("\n---Exception Log Begin---\n");
	        sb.append("Exception Type:").append(e.getClass().getName()).append("\n");
	        sb.append("Exception Details:");
	        log.error(sb.toString(),e);
	        map.put("code", ConstsObject.ERROR_CODE);
	        map.put("msg", ConstsObject.SAVE_ERROR_MSG);
			renderJson(map);
			return;
			
		}
	    
	}
	
	public void getInfo(){
		 Map<String,Object> map = new HashedMap();
		String info_id = getPara("info_id");
		if(info_id != null && !"".equals(info_id)){
			int id = Integer.valueOf(info_id); 
			Record info = InfoModel.dao.getInfo(id);
			List<Record>rec_user = InfoUserModel.dao.getUsers(id);
			map.put("info",info);
			map.put("users",rec_user);
	        map.put("code", ConstsObject.SUCCESS_CODE);
	        map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
			renderJson(map);
		}else{
			 map.put("code", ConstsObject.ERROR_CODE);
		     map.put("msg", ConstsObject.SEARCH_ERROR_MSG);
		     renderJson(map);
			
		}
		
		
	}
	
	public void updateInfo(){
		
	    Map<String,Object> map = new HashedMap();
	    String formStr = HttpKit.readData(getRequest());
	    System.out.println("更新消息字符串:"+formStr);
	    JSONObject jo = JSONObject.fromObject(formStr);
		String info_id = jo.getString("info_id");
		if(info_id != null && !"".equals(info_id)){
			Record info =  new Record();
			info.set("info_title", jo.get("info_title"))
			.set("info_content",jo.get("info_content"))
			.set("info_type",jo.get("info_type"))
			.set("info_id", info_id);
			boolean flag = InfoModel.dao.updateInfo(info);
			if(flag){
				JSONArray ja = jo.getJSONArray("org_user");
				List<Record>user_list = new ArrayList<Record>();
				for (int i = 0; i < ja.size(); i++) {
					Record user = new Record();
					JSONObject j = ja.getJSONObject(i);
					user.set("info_username",j.getString("user_name") );
					user.set("info_user_id",j.getString("user_id") );
					user_list.add(user);
				}
				
				if(user_list.size()>0){
					InfoUserModel.dao.updateUsers(user_list);
				}
				
				 map.put("data",info_id);
				 map.put("code", ConstsObject.SUCCESS_CODE);
			     map.put("msg", ConstsObject.UPDATE_SUCCESS_MSG);
			     renderJson(map);
		  }else{
			 map.put("code", ConstsObject.ERROR_CODE);
		     map.put("msg", ConstsObject.UPDATE_ERROR_MSG);
		     renderJson(map);
		 }
	  }else{
		  
		  map.put("code", ConstsObject.ERROR_CODE);
		  map.put("msg", ConstsObject.UPDATE_ERROR_MSG);
		  renderJson(map);
		  
	  }
	}
	public void delInfo(){
		Map<String,Object> map = new HashedMap();
		String info_id = getPara("info_id");
		if(info_id != null && !"".equals(info_id)){
			boolean flag = InfoModel.dao.delInfo(Integer.valueOf(info_id));
			if(flag){
				//删除接收用户
				 InfoUserModel.dao.delUsers(Integer.valueOf(info_id));
				 map.put("code", ConstsObject.SUCCESS_CODE);
			     map.put("msg", ConstsObject.DEL_SUCCESS_MSG);
			     renderJson(map);
			}else{
				 map.put("code", ConstsObject.ERROR_CODE);
			     map.put("msg", ConstsObject.DEL_ERROR_MSG);
			     renderJson(map);
			}
			
		}else{
			 map.put("code", ConstsObject.ERROR_CODE);
		     map.put("msg", ConstsObject.DEL_ERROR_MSG);
		     renderJson(map);
		}
		
		
	}
	
	

}
