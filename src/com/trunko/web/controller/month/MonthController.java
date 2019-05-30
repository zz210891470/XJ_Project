package com.trunko.web.controller.month;

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
	       String state = month.getString("state");
	       if("sb".equals(state)){
	    	   auditState = "待审核";
	       }
	      // end 
	       
	       Record month_record = new Record();
	       month_record.set("project_id", month.get("project_id")).
	       set("report_year", month.get("report_year")).set("report_month", month.get("report_month")).
	       set("report_month_complete", month.get("report_month_complete")).set("report_year_complete", month.get("report_year_complete")).
	       set("report_total_complete", month.get("report_total_complete")).set("report_year_percent", month.get("report_year_percent")).
	       set("report_percent_all", month.get("report_percent_all")).set("report_progress", month.get("report_progress")).
	       set("report_question", month.get("report_question")).set("report_plan", month.get("report_plan")).
	     //前台传用户名 组织ID过来
	       set("report_username", month.get("report_username")).set("report_createtime", createDate).set("report_state", auditState);

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
		
	}
	
	
	//获取月报详情
	public void getReport(){
		
	}
	
	//获取月报列表 单个项目对应月报列表
	public void getReportList(){
		
	}
	
	//获取要填报月报的项目列表 或者已经填报
	public void getNeedReportList(){
		
		
	}
	
	//删除月报
	public void delReport(){
		
	}
	
	

}
