package com.trunko.web.controller.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;

import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Record;
import com.trunko.anoation.CrossOrigin;
import com.trunko.common.ConstsObject;
import com.trunko.web.dao.form.FormModel;

/**
 * 表单控制器
 */
@CrossOrigin
public class FormController extends Controller{
	
	//给分配的组织  配置表单信息
	public void initForm(){
		Map<String,Object> map = new HashedMap();
		String org_id = getPara("org_id");
		if(org_id!=null && !"".equals(org_id)){
			List<Record>initList = FormModel.dao.getForms(ConstsObject.INIT_ORG_ID,"");
			for(int i = 0; i < initList.size(); i ++){
				initList.get(i).set("org_id", org_id);
			}
			boolean  flag = FormModel.dao.insertForms(initList);
			
			if(flag){
				  map.put("code", ConstsObject.SUCCESS_CODE);
			      map.put("msg", ConstsObject.SAVE_SUCCESS_MSG);
				  renderJson(map);
				
			}else{
				  map.put("code", ConstsObject.ERROR_CODE);
			      map.put("msg", ConstsObject.SAVE_ERROR_MSG);
				  renderJson(map);
				
			}
			
		}else{
			  map.put("code", ConstsObject.ERROR_CODE);
		      map.put("msg", ConstsObject.SAVE_ERROR_MSG);
			  renderJson(map);
			
		}
	
		
	}
	
     //获取所有表单
     public  void getForm(){
    	
    	 Map<String,Object> map = new HashedMap();
    	 String org_id = getPara("org_id");
 		if(org_id!=null && !"".equals(org_id)){
 			List<Record>form_list = FormModel.dao.getForms(org_id, "all");
            map.put("data", form_list);
		    map.put("code", ConstsObject.SUCCESS_CODE);
   	        map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
   		    renderJson(map);
 		}else{
 			map.put("code", ConstsObject.ERROR_CODE);
    	    map.put("msg",  ConstsObject.SEARCH_NULL_MSG);
    	    renderJson(map);
 		}
      
     }
     
     //更新表单
     public void updateForm(){
    	 
      Map<String,Object> map = new HashedMap();
      String form =  HttpKit.readData(getRequest());
      JSONObject jo = JSONObject.fromObject(form);
      JSONArray ja =(JSONArray) jo.get("form_str");
      List<Record>form_list = new ArrayList<Record>();
      for(int i =0;i < ja.size();i++){
    	  Record r = new Record();
    	  JSONObject obj = (JSONObject)ja.get(i);
    	  r.set("form_id", obj.get("form_id"));
    	  r.set("form_title", obj.get("form_title"));
    	  r.set("form_desc", obj.get("form_desc"));
    	  r.set("form_data", obj.get("form_data"));
    	 form_list.add(r);
      }
       boolean flag = FormModel.dao.updateForms(form_list);
       if(flag){
    	   map.put("code", ConstsObject.SUCCESS_CODE);
		   map.put("msg", ConstsObject.SAVE_SUCCESS_MSG);
		   renderJson(map);
       }else{
    	   map.put("code", ConstsObject.ERROR_CODE);
		   map.put("msg", ConstsObject.SAVE_ERROR_MSG);
		   renderJson(map);
       }
    
    	 
     }


}
