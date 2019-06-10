package com.trunko.web.controller.month;

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
import com.trunko.web.dao.month.MonthDefineModel;
import com.trunko.web.dao.month.MonthModel;
import com.trunko.web.dao.project.ProjectDefineModel;
import com.trunko.web.dao.project.ProjectModel;
import com.trunko.web.dao.project.ProjectPlanDefModel;
import com.trunko.web.dao.project.ProjectPlanModel;

/**
 * 月报控制器
 * @author Administrator
 *
 */
@CrossOrigin
public class MonthController extends Controller {
	private static final  Logger log = Logger.getLogger(MonthController.class);
	
	//保存月报
	public void saveReport(){
		Map<String,Object> map = new HashedMap();
		try {
	       
	       Date createDate = new Date();
	       String formStr = HttpKit.readData(getRequest());
	       JSONObject jo = JSONObject.fromObject(formStr);
	       
	       JSONObject month =(JSONObject) jo.get("month");
	       
	       // start  根据页面保存的 是否保存草稿还是保存并上报
	       String auditState = ConstsObject.AUDIT_STATE_UNAUDIT;
	       //页面保存标识 区分草稿 还是 上报保存
	       String state = jo.getString("state");
	       if("sb".equals(state)){
	    	   auditState = "待审核";
	       }
	      // end 
	       
	       Record month_record = new Record();
	       month_record.set("project_id", month.get("project_id")).
	       set("report_year", month.get("report_year")).set("report_month", month.get("report_month")).
	       set("report_month_complete", month.get("report_month_complete")).set("report_year_complete", month.get("report_year_complete")).
	       set("report_total_complete", month.get("report_total_complete")).set("report_year_percent", month.get("report_year_percent")).
	       set("report_all_percent", month.get("report_all_percent")).set("report_progress", month.get("report_progress")).
	       set("report_question", month.get("report_question")).set("report_plan", month.get("report_plan")).
	     //前台传用户名 组织ID过来
	       set("report_username", jo.get("username")).set("report_createtime", createDate).set("report_state", auditState);

	       boolean flag =  MonthModel.dao.saveMonth(month_record);
	       if(flag){
	    	   // 开始保存月报自定义信息
	    	  int mon_id = month_record.getInt("report_id");
	    	  JSONArray ja =(JSONArray) jo.get("month_define");
	    	  List<Record>list = new ArrayList<Record>();
	    	   for(int i =0;i<ja.size();i++){
	    	    	  JSONObject js =(JSONObject) ja.get(i);
	    	    	  Record r = new Record();
	    	    	  r.set("field_name", js.get("field_name"));
	    	    	  r.set("field_content", js.get("field_content"));
	    	    	  r.set("month_report_id", mon_id);
	    	    	  list.add(r);
	  
	    	    }
	    	   if(list.size()>0){
	    		  MonthDefineModel.dao.saveMonthDef(list);

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
	
	//更新月报
	public void updateReport(){
		
		Map<String,Object> map = new HashedMap();
		try {
	       
	       String formStr = HttpKit.readData(getRequest());
	       JSONObject jo = JSONObject.fromObject(formStr);
	       
	       JSONObject month =(JSONObject) jo.get("month");
	       

	       
	       Record month_record = new Record();
	       month_record.set("report_id", month.get("report_id")).
	       set("report_year", month.get("report_year")).set("report_month", month.get("report_month")).
	       set("report_month_complete", month.get("report_month_complete")).set("report_year_complete", month.get("report_year_complete")).
	       set("report_total_complete", month.get("report_total_complete")).set("report_year_percent", month.get("report_year_percent")).
	       set("report_all_percent", month.get("report_all_percent")).set("report_progress", month.get("report_progress")).
	       set("report_question", month.get("report_question")).set("report_plan", month.get("report_plan"));
	       // start  根据页面保存的 是否保存草稿还是保存并上报
	       //页面保存标识 区分草稿 还是 上报保存
	       String state = jo.getString("state");
	       String auditState = ConstsObject.AUDIT_STATE_UNAUDIT;
	       if("sb".equals(state)){
	    	   auditState = "待审核";
	    	   month_record.set("report_state", auditState);
	       }
	      // end 
	       boolean flag =  MonthModel.dao.updateMonth(month_record);
	       if(flag){
	    	   // 开始保存月报自定义信息
	    	
	    	  JSONArray ja =(JSONArray) jo.get("month_define");
	    	  List<Record>list = new ArrayList<Record>();
	    	   for(int i =0;i<ja.size();i++){
	    	    	  JSONObject js =(JSONObject) ja.get(i);
	    	    	  Record r = new Record();
	    	    	  r.set("field_content", js.get("field_content"));
	    	    	  r.set("month_define_id", js.get("month_define_id"));
	    	    	  list.add(r);
	  
	    	    }
	    	   if(list.size()>0){
	    		  MonthDefineModel.dao.updateMonthDefine(list);

	    	   }
	    	   
	    	   if("mg".equals(state)){
	    		   //如果是管理列表中的编辑  需要更新当前月份后面所有月报的数据
	    		   
	    		   
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
	
	
	//获取月报详情
	public void getReport(){
		
		Map<String,Object> map = new HashedMap();
		String report_id = getPara("report_id");
		if(report_id!=null&&!"".equals(report_id)){
			//获取月报详情
			Record m = MonthModel.dao.getMonthDetail(Integer.valueOf(report_id));
			if(m!=null){
				
				List<Record>deflist = MonthDefineModel.dao.getMonthDefList(Integer.valueOf(report_id));
				Record def = new Record();
				for (int i = 0; i < deflist.size(); i++) {
					
					def.set(deflist.get(i).getStr("field_name"), deflist.get(i).getStr("field_content"));
					
				}
				
				m.setColumns(def);
			}
			map.put("data", m);
		    map.put("code", ConstsObject.SUCCESS_CODE);
   	        map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
   		    renderJson(map);
			
		}else{
			 map.put("code", ConstsObject.ERROR_CODE);
	 	     map.put("msg",  ConstsObject.SEARCH_NULL_MSG);
	 		 renderJson(map);
			
		}

		
	}
	
	//获取月报列表 单个项目对应月报列表
	public void getReportList(){
		
		Map<String,Object> map = new HashedMap();
		String pro_id = getPara("project_id");
		if(pro_id != null){
			
			   List<Record>month_list = MonthModel.dao.getMonthList(Integer.valueOf(pro_id));
			   map.put("data", month_list);
			   map.put("code", ConstsObject.SUCCESS_CODE);
	 	       map.put("msg",  ConstsObject.SEARCH_SUCCESS_MSG);
	 		   renderJson(map);
			
		}else{
		   map.put("code", ConstsObject.ERROR_CODE);
 	       map.put("msg",  ConstsObject.SEARCH_NULL_MSG);
 		   renderJson(map);
				
		}
		
	}
	
	//获取要填报月报的项目列表 或者已经填报
	public void getNeedReportMonthList(){
		
	    Map<String,Object> map = new HashedMap();
		String org_id = getPara("org_id");
		if(org_id == null||"".equals(org_id)){
			  map.put("code", ConstsObject.ERROR_CODE);
	 	      map.put("msg",  ConstsObject.SEARCH_ERROR_MSG);
	  		  renderJson(map);
		}else{
			String username = getPara("username");
			int page = ConstsObject.PAGE_NO;
			int limit =ConstsObject.PAGE_SIZE;
			if(username == null){
				username = "";
			}
			
			Calendar cl = Calendar.getInstance();
			int month = cl.get(Calendar.MONTH)+1;
			int year = cl.get(Calendar.YEAR);
			String monthStr =getPara("month");
			String yearStr = getPara("year");
			if(monthStr != null && !"".equals(yearStr)){
				month = Integer.valueOf(monthStr);
			}
			if(yearStr != null && !"".equals(yearStr)){
				year = Integer.valueOf(yearStr);
			}
			
			String pageStr = getPara("page");
			String limitStr= getPara("limit");
			if(pageStr!=null){
				page = Integer.valueOf(pageStr);
			}
			
			if(limitStr!=null){
				limit = Integer.valueOf(limitStr);
			}
			String keyword = getPara("keyword");
			if(keyword == null){
				keyword = "";
			}
			    Page<Record>page_list = MonthModel.dao.getReportProjectList(page, limit, username, keyword, year, month, org_id);
				map.put("data", page_list);
			    map.put("code", ConstsObject.SUCCESS_CODE);
	   	        map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
	   		    renderJson(map);
		}

		
		
		
		
	}
	
	
	//获取月报管理列表
	public void getManageReportMonthList(){
		
	    Map<String,Object> map = new HashedMap();
		String org_id = getPara("org_id");
		if(org_id == null||"".equals(org_id)){
			  map.put("code", ConstsObject.ERROR_CODE);
	 	      map.put("msg",  ConstsObject.SEARCH_ERROR_MSG);
	  		  renderJson(map);
		}else{
		
			int page = ConstsObject.PAGE_NO;
			int limit =ConstsObject.PAGE_SIZE;

			
			Calendar cl = Calendar.getInstance();
			int month = cl.get(Calendar.MONTH)+1;
			int year = cl.get(Calendar.YEAR);
			String monthStr =getPara("month");
			String yearStr = getPara("year");
			if(monthStr != null && !"".equals(yearStr)){
				month = Integer.valueOf(monthStr);
			}
			if(yearStr != null && !"".equals(yearStr)){
				year = Integer.valueOf(yearStr);
			}
			
			String pageStr = getPara("page");
			String limitStr= getPara("limit");
			if(pageStr!=null){
				page = Integer.valueOf(pageStr);
			}
			
			if(limitStr!=null){
				limit = Integer.valueOf(limitStr);
			}
			String keyword = getPara("keyword");
			if(keyword == null){
				keyword = "";
			}
			    Page<Record>page_list = MonthModel.dao.getManageReportList(page, limit, keyword, year, month, org_id);
				map.put("data", page_list);
			    map.put("code", ConstsObject.SUCCESS_CODE);
	   	        map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
	   		    renderJson(map);
		}

		
		
		
		
	}
	
	//删除月报
	public void delReport(){
		 Map<String,Object> map = new HashedMap();
		String month_id = getPara("month_id");
		if(month_id!=null && !"".equals(month_id)){
			int mon_id =Integer.valueOf(month_id);
			boolean flag = MonthModel.dao.delMonth(mon_id);
			if(flag){
				//删除自定义月报
				 MonthDefineModel.dao.delMonthDef(mon_id);
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
			  //删除失败
			   map.put("code", ConstsObject.ERROR_CODE);
		       map.put("msg",  ConstsObject.DEL_ERROR_MSG);
	 		   renderJson(map);
			
		}
		
	}
	
	
	   //用于平台审核流程  更新 项目的 状态 审核通过 或者不通过
	   public void updateStatus(){
		   String id = getPara("month_id");
		   Map<String,Object> map = new HashedMap();
		   if(id!=null){
			   String status = getPara("status");
			  boolean flag = MonthModel.dao.updateStatus(Integer.valueOf(id), status);
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
