package com.trunko.web.controller.project;

import java.util.ArrayList;
import java.util.Date;
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
import com.trunko.web.dao.project.ProjectDefineModel;
import com.trunko.web.dao.project.ProjectModel;
import com.trunko.web.dao.project.ProjectPlanDefModel;
import com.trunko.web.dao.project.ProjectPlanModel;

@CrossOrigin
public class ProjectController extends Controller {
	private static final  Logger log = Logger.getLogger(ProjectController.class);

	
     //保存项目信息
    public  void saveProject(){
    	Map<String,Object> map = new HashedMap();
		try {

	    	//String username = getSessionAttr("username");
	    	//String org_id = getSessionAttr("org_id");
	       String username = "zhangsan";
	       String org_id ="abcd";
	       
	       Date createDate = new Date();
	       String formStr = HttpKit.readData(getRequest());
	       //String formStr = getPara("project_json");
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
	       set("pro_username", username).set("pro_createtime", createDate).set("pro_audit_state", auditState).
	       set("pro_org_id", org_id);
	       boolean flag =  ProjectModel.dao.saveProject(project_record);
	       if(flag){
	    	   // 开始保存项目自定义信息
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
	    	   
	    	  // 结束 保存项目自定义信息
	    	  // 开始保存 项目年度计划
	    	   JSONObject plan =(JSONObject) jo.get("project_plan");
	    	   Record plan_record = new Record();
	    	   plan_record.set("project_id", pro_id);
	    	   plan_record.set("plan_year", plan.get("plan_year"));
	    	   plan_record.set("plan_progress", plan.get("plan_progress"));
	    	   plan_record.set("plan_investment", plan.get("plan_investment"));
	    	   plan_record.set("plan_investment1", plan.get("plan_investment1"));
	    	   plan_record.set("plan_investment2", plan.get("plan_investment2"));
	    	   plan_record.set("plan_investment3", plan.get("plan_investment3"));
	    	   plan_record.set("plan_investment4", plan.get("plan_investment4"));
	    	   plan_record.set("plan_investment5", plan.get("plan_investment5"));
	    	   plan_record.set("plan_investment6", plan.get("plan_investment6"));
	    	   plan_record.set("plan_investment7", plan.get("plan_investment7"));
	    	   plan_record.set("plan_investment8", plan.get("plan_investment8"));
	    	   plan_record.set("plan_investment9", plan.get("plan_investment9"));
	    	   plan_record.set("plan_investment10", plan.get("plan_investment10"));
	    	   plan_record.set("plan_investment11", plan.get("plan_investment11"));
	    	   plan_record.set("plan_investment12", plan.get("plan_investment12"));
	    	   plan_record.set("plan_start_month", plan.get("plan_start_month"));
	    	   plan_record.set("plan_end_month", plan.get("plan_end_month"));
	    	   boolean save_flag = ProjectPlanModel.dao.saveProjectPlan(plan_record);
	    	   //保存年度计划结束
	    	   if(save_flag){
	    		   JSONArray plan_arr =(JSONArray) jo.get("project_plan_define");
	    	    	  List<Record>plan_def_list = new ArrayList<Record>();
	    	    	   for(int i =0;i<plan_arr.size();i++){
	    	    	    	  JSONObject pd =(JSONObject) plan_arr.get(i);
	    	    	    	  Record r = new Record();
	    	    	    	  r.set("field_name", pd.get("field_name"));
	    	    	    	  r.set("field_content", pd.get("field_content"));
	    	    	    	  r.set("plan_id", plan_record.get("plan_id"));
	    	    	    	  plan_def_list.add(r);
	    	  
	    	    	    }
	    	    	   if(plan_def_list.size()>0){
	    	    		  ProjectPlanDefModel.dao.saveProjectPlanDef(plan_def_list);

	    	    	   }
	    		   
	    	   }
	    	   
	       }
		} catch (Exception e) {
			
			//如果不写这段代码 由于 捕获了异常   异常拦截器不会将 错误记录到错误日志中  所以 在这里需要手动 记录到日志
	        StringBuilder sb =new StringBuilder("\n---Exception Log Begin---\n");
	        sb.append("Exception Type:").append(e.getClass().getName()).append("\n");
	        sb.append("Exception Details:");
	        log.error(sb.toString(),e);
	        map.put("code", 1);
	        map.put("msg", "项目保存出错");
			renderJson(map);
			return;
		}
        map.put("code", 0);
        map.put("msg", "保存成功");
		renderJson(map);
    	
    }
    
    // 获取项目详情
    public void getProject(){
    	Map<String,Object> map = new HashedMap();
    	String  pro_id = getPara("pro_id");
    	if(pro_id!=null&&!"".equals(pro_id)){
    		int project_id = Integer.valueOf(pro_id); 
    		//项目
    		Record project = ProjectModel.dao.getProjectById(project_id);
    		if(project != null){
        		//项目自定义
        		List<Record>project_def = ProjectDefineModel.dao.getProjectDefine(project_id);
        		//年度计划
        		Record project_plan = ProjectPlanModel.dao.getProjectPlan(project_id);
        		//年度计划自定义
        		List<Record>project_plan_def = new ArrayList<Record>(); 
        		
        		
        		if(project_plan!=null){
        			int plan_id = project_plan.getInt("plan_id");
        			project_plan_def = ProjectPlanDefModel.dao.getProjectPlanDefine(plan_id);
        			
        		}
        		
        		// 将自定义数据组成一个对象
        		Record pro_def = new Record();
        		if(project_def.size()>0){
        			for(int i = 0; i < project_def.size();i++){
        				pro_def.set(project_def.get(i).getStr("field_name"), project_def.get(i).getStr("field_content"));
        			}
        			
        		}
        		
        		Record plan_def = new Record();
        		if(project_plan_def.size()>0){
        			for(int i = 0; i < project_plan_def.size();i++){
        				plan_def.set(project_plan_def.get(i).getStr("field_name"), project_plan_def.get(i).getStr("field_content"));
        			}
        			
        		}
        		
        	
    			Record r = project.setColumns(pro_def);
    			//年度计划作为子对象 嵌入 项目对象中，防止两个自定义表字段重复无法区分问题
    			Record m =  new Record();
    			if(project_plan!=null){
        			project_plan.setColumns(plan_def);
        			m.set("pro_plan", project_plan);
    			}

    			//合并成一个对象
    			Record r2 = r.setColumns(m);
    			//结束
    			map.put("project", r2);
    		    map.put("code", 0);
       	        map.put("msg", "成功");
       		    renderJson(map);
    		}else{
    			
    		   map.put("code", 1);
     	       map.put("msg", "查询不到该项目");
     		   renderJson(map);
    			
    		}
    		

    		
    		
    	}else{
    		   map.put("code", 1);
    	       map.put("msg", "查询失败,id为空");
    		   renderJson(map);
    		
    	}
    	
    	
    	
    	
    }
    
    //编辑项目
    public void updateProject(){
    	
    	
    	
    	
    }
    
    //查询项目列表
   public void getProjectDraftList(){
	   String year = getPara("year");
	   String pageNo = getPara("page");
	   String pageSize = getPara("limit");
	   

	   
   }
    


}
