package com.trunko.web.controller.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.trunko.web.dao.project.ProjectDefineModel;
import com.trunko.web.dao.project.ProjectModel;

public class ProjectController extends Controller {

    public  void saveProject(){
    	//String username = getSessionAttr("username");
    	//String org_id = getSessionAttr("org_id");
       String username = "zhangsan";
       String org_id ="abcd";
       
       Date createDate = new Date();
       
       String formStr = getPara("project_json");
       JSONObject jo = JSONObject.fromObject(formStr);
       JSONObject project =(JSONObject) jo.get("project");
       
       // start  根据页面保存的 是否保存草稿还是保存并上报
       //  String state = project.getString("state");
       String state = "未上报";
       String auditState = "未上报";
       if(!state.equals(auditState)){
    	   auditState = "待审核";
       }
      // end 
       
       Record project_record = new Record();
       project_record.set("pro_name", project.get("pro_name")).
       set("pro_year", project.get("pro_year")).set("pro_state", project.get("pro_state")).
       set("pro_investment", project.get("pro_investment")).set("pro_content", project.get("pro_content")).
       set("pro_province", project.get("pro_province")).set("pro_city", project.get("pro_city")).
       set("pro_county", project.get("pro_county")).set("pro_town", project.get("pro_town")).
       set("pro_address", project.get("pro_address")).set("pro_location", project.get("pro_location")).
       set("pro_industry", project.get("pro_industry")).set("pro_subsectors", project.get("pro_subsectors")).
       set("pro_way", project.get("pro_way")).set("pro_start_year", project.get("pro_start_year")).
       set("pro_end_year", project.get("pro_end_year")).set("pro_owner", project.get("pro_owner")).
       set("pro_responsibility", project.get("pro_responsibility")).set("pro_type", project.get("pro_type")).
       set("pro_username", username).set("pro_createtime", createDate).set("pro_audit_state", createDate).
       set("pro_org_id", org_id);
       boolean flag =  ProjectModel.dao.saveProject(project_record);
       if(flag){
    	  int pro_id = project_record.getInt("pro_id");
    	  JSONArray ja =(JSONArray) jo.get("project_define");
    	  List<Record>list = new ArrayList<Record>();
    	   for(int i =0;i<ja.size();i++){
    	    	  JSONObject js =(JSONObject) ja.get(i);
    	    	  Record r = new Record();
    	    	  r.set("field_name", js.get("field_name"));
    	    	  r.set("field_content", js.get("field_content"));
    	    	  r.set("project_id", pro_id);
    	    	  list.add(r);
  
    	    }
    	   if(list.size()>0){
    		   ProjectDefineModel.dao.saveProjectDefine(list);
    		   
    	   }
       }

        renderJson("麻痹");
    }


}
