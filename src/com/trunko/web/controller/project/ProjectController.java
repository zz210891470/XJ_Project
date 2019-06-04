package com.trunko.web.controller.project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.trunko.anoation.CrossOrigin;
import com.trunko.common.ConstsObject;
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
	       
	       Date createDate = new Date();
	       String formStr = HttpKit.readData(getRequest());
	       System.out.println("表单字符串:"+formStr);
	       JSONObject jo = JSONObject.fromObject(formStr);
	       JSONObject project =(JSONObject) jo.get("project");
	       
	       // start  根据页面保存的 是否保存草稿还是保存并上报
	       String auditState = ConstsObject.AUDIT_STATE_UNAUDIT;
	       //页面保存标识 区分草稿 还是 上报保存
	       String state = jo.getString("state");
	       if("sb".equals(state)){
	    	   auditState = "待审核";
	       }
	       
	      // end 
	       
	       JSONArray province = project.getJSONArray("pro_province"); //省市县镇前端过来 的是一个数组
	       JSONArray industry = project.getJSONArray("pro_industry"); //大行业，子行业是一个数组
	       
	       Record project_record = new Record();
	       project_record.set("pro_name", project.get("pro_name")).
	       set("pro_year", project.get("pro_year")).set("pro_state", project.get("pro_state")).
	       set("pro_investment", project.get("pro_investment")).set("pro_content", project.get("pro_content")).
	      
	       set("pro_province", province.get(0)).set("pro_city", province.get(1)).
	       set("pro_county", province.get(2)).set("pro_town",province.get(3)).   
	       set("pro_address", project.get("pro_address")).set("pro_location", project.get("pro_location")).
	       set("pro_industry", industry.get(0)).set("pro_subsectors", industry.get(1)).
	       set("pro_way", project.get("pro_way")).set("pro_start_year", project.get("pro_start_year")).
	       set("pro_end_year", project.get("pro_end_year")).set("pro_owner", project.get("pro_owner")).
	       set("pro_responsibility", project.get("pro_responsibility")).set("pro_type", project.get("pro_type")).
	      //前台传用户名 组织ID过来
	       set("pro_username", jo.get("pro_username")).set("pro_createtime", createDate).set("pro_audit_state", auditState).
	       set("pro_org_id",jo.get("pro_org_id"));
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
	    	   JSONObject plan =(JSONObject) jo.get("plan");
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
	    		   JSONArray plan_arr =(JSONArray) jo.get("plan_define");
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
	        map.put("code", ConstsObject.ERROR_CODE);
	        map.put("msg", ConstsObject.SAVE_ERROR_MSG);
			renderJson(map);
			return;
		}
        map.put("code", ConstsObject.SUCCESS_CODE);
        map.put("msg", ConstsObject.SAVE_SUCCESS_MSG);
		renderJson(map);
    	
    }
    
    //保存项目信息
   public  void saveProject1(){
   	Map<String,Object> map = new HashedMap();
		//try {
	       
	       Date createDate = new Date();
	       String formStr = getPara("project_json");
	       System.out.println(formStr);
	       renderNull();
	     /*  JSONObject jo = JSONObject.fromObject(formStr);
	       JSONObject project =(JSONObject) jo.get("project");
	       
	       // start  根据页面保存的 是否保存草稿还是保存并上报
	       String auditState = ConstsObject.AUDIT_STATE_UNAUDIT;
	       //页面保存标识 区分草稿 还是 上报保存
	       String state = jo.getString("state");
	       if("sb".equals(state)){
	    	   auditState = "待审核";
	       }
	       
	      // end 
	       
	       JSONArray province = project.getJSONArray("pro_province"); //省市县镇前端过来 的是一个数组
	       JSONArray industry = project.getJSONArray("pro_industry"); //大行业，子行业是一个数组
	       
	       Record project_record = new Record();
	       project_record.set("pro_name", project.get("pro_name")).
	       set("pro_year", project.get("pro_year")).set("pro_state", project.get("pro_state")).
	       set("pro_investment", project.get("pro_investment")).set("pro_content", project.get("pro_content")).
	      
	       set("pro_province", province.get(0)).set("pro_city", province.get(1)).
	       set("pro_county", province.get(2)).set("pro_town",province.get(3)).   
	       set("pro_address", project.get("pro_address")).set("pro_location", project.get("pro_location")).
	       set("pro_industry", industry.get(0)).set("pro_subsectors", industry.get(1)).
	       set("pro_way", project.get("pro_way")).set("pro_start_year", project.get("pro_start_year")).
	       set("pro_end_year", project.get("pro_end_year")).set("pro_owner", project.get("pro_owner")).
	       set("pro_responsibility", project.get("pro_responsibility")).set("pro_type", project.get("pro_type")).
	      //前台传用户名 组织ID过来
	       set("pro_username", project.get("pro_username")).set("pro_createtime", createDate).set("pro_audit_state", auditState).
	       set("pro_org_id",jo.get("pro_org_id"));
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
	    	   JSONObject plan =(JSONObject) jo.get("plan");
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
	    		   JSONArray plan_arr =(JSONArray) jo.get("plan_define");
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
	        map.put("code", ConstsObject.ERROR_CODE);
	        map.put("msg", ConstsObject.SAVE_ERROR_MSG);
			renderJson(map);
			return;
		}
       map.put("code", ConstsObject.SUCCESS_CODE);
       map.put("msg", ConstsObject.SAVE_SUCCESS_MSG);
		renderJson(map);*/
   	
   } 
    
    
    // 获取项目详情
    public void getProject(){
    	Map<String,Object> map = new HashedMap();
    	String  pro_id = getPara("pro_id");
    	System.out.println(pro_id);
    	if(pro_id!=null&&!"".equals(pro_id)){
    		int project_id = Integer.valueOf(pro_id); 
    		//项目
    		Record project = ProjectModel.dao.getProjectById(project_id);
    		
    		if(project != null){
        		String [] province  = new String[4];
        		province[0] = project.getStr("pro_province");
        		province[1] = project.getStr("pro_city");
        		province[2] = project.getStr("pro_county");
        		province[3] = project.getStr("pro_town");
        		project.set("pro_province",province);
        		
        		String [] industry = new String[2];
        		industry[0] = project.getStr("pro_industry");
        		industry[1] = project.getStr("pro_subsectors");
        		project.set("pro_industry",industry);
        		
    			
    			
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
    			map.put("data", r2);
    		    map.put("code", ConstsObject.SUCCESS_CODE);
       	        map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
       		    renderJson(map);
    		}else{
    			
    		   map.put("code", ConstsObject.ERROR_CODE);
     	       map.put("msg",  ConstsObject.SEARCH_NULL_MSG);
     		   renderJson(map);
    			
    		}
    		

    		
    		
    	}else{
    		 map.put("code", ConstsObject.ERROR_CODE);
   	         map.put("msg",  ConstsObject.SEARCH_ERROR_MSG);
    		 renderJson(map);
    		
    	}
    	
    	
    	
    	
    }
    
    //编辑项目 前台需传state(如果是 草稿 和管理 里面的编辑可以不传  如果是 保存并上报 则是 更改草稿箱未上报的 状态  需要传状态）
    public void updateProject(){

    	Map<String,Object> map = new HashedMap();
		try {

	       String formStr = HttpKit.readData(getRequest());
	       System.out.println("输出更新字符串:"+formStr);
	       JSONObject jo = JSONObject.fromObject(formStr);
	       JSONObject project =(JSONObject) jo.get("project");
	       
	       JSONArray province = project.getJSONArray("pro_province"); //省市县镇前端过来 的是一个数组
	       JSONArray industry = project.getJSONArray("pro_industry"); //大行业，子行业是一个数组
	       
	       Record project_record = new Record();
	       project_record.set("pro_id", project.get("pro_id")).set("pro_name", project.get("pro_name")).
	       set("pro_year", project.get("pro_year")).set("pro_state", project.get("pro_state")).
	       set("pro_investment", project.get("pro_investment")).set("pro_content", project.get("pro_content")).
	       set("pro_province", province.get(0)).set("pro_city", province.get(1)).
	       set("pro_county", province.get(2)).set("pro_town",province.get(3)).
	       set("pro_address", project.get("pro_address")).set("pro_location", project.get("pro_location")).
	       set("pro_industry", industry.get(0)).set("pro_subsectors", industry.get(1)).
	       set("pro_way", project.get("pro_way")).set("pro_start_year", project.get("pro_start_year")).
	       set("pro_end_year", project.get("pro_end_year")).set("pro_owner", project.get("pro_owner")).
	       set("pro_responsibility", project.get("pro_responsibility")).set("pro_type", project.get("pro_type"));
	       
	       // start  根据页面编辑的 是否保存草稿还是保存并上报
	       String auditState = ConstsObject.AUDIT_STATE_UNAUDIT;
	       //页面保存标识 区分草稿 还是 上报保存
	       String state = jo.getString("state");
	       if("sb".equals(state)){
	    	   auditState = "待审核";
	    	   project_record.set("pro_audit_state", auditState);
	       }
	       
	      // end 

	       
           
           boolean flag =  ProjectModel.dao.updateProject(project_record);
	    
	       if(flag){
	    	   // 开始更新项目自定义信息
	    
	    	  JSONArray ja =(JSONArray) jo.get("project_define");
	    	  List<Record>list = new ArrayList<Record>();
	    	   for(int i =0;i<ja.size();i++){
	    	    	  JSONObject js =(JSONObject) ja.get(i);
	    	    	  Record r = new Record();	    	    	 
	    	    	  r.set("field_content", js.get("field_content"));
	    	    	  r.set("project_define_id",js.get("project_define_id"));
	    	    	  list.add(r);
	  
	    	    }
	    	   if(list.size()>0){
	    		   ProjectDefineModel.dao.updateProjectDefine(list);

	    	   }
	    	   
	    	  // 结束 保存项目自定义信息
	    	  // 开始保存 项目年度计划
	    	   JSONObject plan =(JSONObject) jo.get("plan");
	    	   Record plan_record = new Record();

	    	   //plan_record.set("plan_year", plan.get("plan_year")); //是否需要年度？
	    	   plan_record.set("plan_id", plan.get("plan_id"));
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
	    	   boolean save_flag = ProjectPlanModel.dao.updateProjectPlan(plan_record);
	    	   //保存年度计划结束
	    	   if(save_flag){
	    		   JSONArray plan_arr =(JSONArray) jo.get("plan_define");
	    	    	  List<Record>plan_def_list = new ArrayList<Record>();
	    	    	   for(int i =0;i<plan_arr.size();i++){
	    	    	    	  JSONObject pd =(JSONObject) plan_arr.get(i);
	    	    	    	  Record r = new Record();
	    	    	    	  r.set("field_content", pd.get("field_content"));
	    	    	    	  r.set("plan_define_id", pd.get("plan_define_id"));
	    	    	    	  plan_def_list.add(r);
	    	  
	    	    	    }
	    	    	   if(plan_def_list.size()>0){
	    	    		  ProjectPlanDefModel.dao.updateProjectPlanDef(plan_def_list);

	    	    	   }
	    		   
	    	   }
	    	   
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
        map.put("code", ConstsObject.SUCCESS_CODE);
        map.put("msg", ConstsObject.SAVE_SUCCESS_MSG);
		renderJson(map);
    	
    	
    	
    }
    
    //查询项目列表(草稿，管理等列表） 审核列表由国服平台提供
   public void getProjectList(){
	   Map<String,Object> map = new HashedMap();
	   String org_id = getPara("org_id");
	   System.out.println("查询列表组织ID:"+org_id);
	   if(org_id!=null){
		   
		   Calendar cal = Calendar.getInstance();
		   int year = cal.get(Calendar.YEAR);
		   String year_str = getPara("pro_year");
		  
		   if(year_str != null&&!"".equals(year_str)){
			   year = Integer.valueOf(year_str);
		   }
		   
		   int pageNo = ConstsObject.PAGE_NO;
		   String pageNo_str = getPara("page");
		   if(pageNo_str != null){
			   pageNo = Integer.valueOf(pageNo_str);
		   }
		   
		   int pageSize = ConstsObject.PAGE_SIZE;
		   String pageSize_str = getPara("limit");
		   if(pageSize_str != null){
			   pageSize = Integer.valueOf(pageSize_str);
		   }
		   String keyword = getPara("pro_name");
		   if(keyword == null){
			   keyword = "";
		   }  
		   String city = getPara("pro_city");
		   if(city == null){
			   city = "";
		   }
		   
		   String county = getPara("pro_county");
		   if(county == null){
			   county = "";
		   }
		   
		   //项目类型
		   String pro_type = getPara("pro_type");
		   if(pro_type == null){
			   pro_type = "";
		   }

		   //大类
		   String pro_industry = getPara("pro_industry");
		   if(pro_industry == null){
			   pro_industry = "";
		   }
		   //子类
		   String pro_subsectors = getPara("pro_subsectors");
		   if(pro_subsectors == null){
			   pro_subsectors = "";
		   }
		   
		   String username = getPara("pro_username");
		   if(username == null){
			   username = "";
		   }

		   String flag = getPara("flag");
		   
		   Page<Record>page = ProjectModel.dao.getProjectList(pageNo, pageSize, username, keyword, year, city, county, pro_type, pro_industry, pro_subsectors, flag, org_id);
			map.put("data", page);
		    map.put("code", ConstsObject.SUCCESS_CODE);
   	        map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
   		    renderJson(map);
	   }else{
		   map.put("code", ConstsObject.ERROR_CODE);
 	       map.put("msg",  ConstsObject.SEARCH_ERROR_MSG);
  		   renderJson(map);
	   }
	   
	   
	   
   }
   
   
   public void delProject(){
	   
	   Map<String,Object> map = new HashedMap();
	   String pro_id = getPara("pro_id");
	   String del_way = ConstsObject.DEL_WAY;
	   if(pro_id!=null){
		  String d_way =  getPara("del_way");
		  if(d_way!=null){
			  del_way =d_way;
		  }
		  int pid = Integer.valueOf(pro_id);
		   //删除项目主表
		  boolean flag =  ProjectModel.dao.delProject(pid, del_way);
		  if(flag){
			 
			  
			  if("delete".equals(del_way)){
				  //删除项目自定义
				 ProjectDefineModel.dao.batchDelProjectDefine(pid);
			  
				  //删除年度计划
				  Record plan = ProjectPlanModel.dao.getProjectPlanId(pid);
				  
				  if(plan!=null){
					  
					  int plan_id = plan.getInt("plan_id");
					  boolean f = ProjectPlanModel.dao.delProjectPlan(plan_id);
					  
					  if(f){  
						  //删除年度计划自定义
						 ProjectPlanDefModel.dao.batchDelProjectDefine(plan_id);
  
					  }
					  
				  }
				  
			  }
			
			  
			   map.put("code", ConstsObject.SUCCESS_CODE);
		       map.put("msg",  ConstsObject.DEL_SUCCESS_MSG);
	 		   renderJson(map); 
			  
		  }else{
			  //删除失败
			   map.put("code", ConstsObject.ERROR_CODE);
		       map.put("msg",  ConstsObject.DEL_ERROR_MSG);
	 		   renderJson(map);
			  
		  }
		   
		  
	   }else{
		   map.put("code", ConstsObject.ERROR_CODE);
 	       map.put("msg",  ConstsObject.DEL_ERROR_MSG);
  		   renderJson(map);
		   
	   }
	   
   }
   
   
   //用于平台审核流程  更新 项目的 状态 审核通过 或者不通过
   public void updateStatus(){
	   String id = getPara("id");
	   Map<String,Object> map = new HashedMap();
	   if(id!=null){
		   String status = getPara("status");
		  boolean flag = ProjectModel.dao.updateStatus(Integer.valueOf(id), status);
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
    


}
