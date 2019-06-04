package com.trunko.web.controller.company;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.trunko.anoation.CrossOrigin;
import com.trunko.common.ConstsObject;
import com.trunko.web.dao.company.CompanyModel;

/**
 * 援疆企业管理
 * @author Administrator
 *
 */
@CrossOrigin
public class CompanyController extends Controller {
	
	private static final  Logger log = Logger.getLogger(CompanyController.class);
	
	//保存公司
	public void saveCompany(){
	      Map<String,Object> map = new HashedMap();
	     
	       String formStr = HttpKit.readData(getRequest());
	       JSONObject jo = JSONObject.fromObject(formStr);
	       JSONObject company =(JSONObject) jo.get("company");
	       String company_name = company.getString("comp_name");
	       boolean if_exist = CompanyModel.dao.checkIfexist(company_name, "");
	        if(if_exist){
	        	//该名称已存在
		        map.put("code", ConstsObject.ERROR_CODE);
		        map.put("msg", "名称已存在");
				renderJson(map);
				return;
	        	
	        }else{
	        	
	        	 Record comp = new Record();
	        	 comp.set("comp_lawman",jo.get("comp_lawman")).set("comp_province", jo.get("comp_province")).
	        	 set("comp_city", jo.get("comp_city")).set("comp_reg_money", jo.get("comp_reg_money")).
	        	 set("comp_reg_time", jo.get("comp_reg_time")).set("comp_investment", jo.get("comp_investment")).
	        	 set("comp_type", jo.get("comp_type")).set("comp_address", jo.get("comp_address")).
	        	 set("comp_in_time", jo.get("comp_in_time")).set("comp_contact", jo.get("comp_contact")).
	        	 set("comp_phone", jo.get("comp_phone")).set("comp_numer", jo.get("comp_numer")).
	        	 set("comp_min_number", jo.get("comp_min_number")).set("comp_remark", jo.get("comp_remark"));
	        	 
	        	 boolean flag = CompanyModel.dao.saveCompany(comp);
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
	
	//查询企业列表
	 public void getCompanylist(){
		 
		 Map<String,Object> map = new HashedMap();
		 try {
			 String keyword = getPara("keyword");
			 int page = ConstsObject.PAGE_NO;
			 int pageSize =  ConstsObject.PAGE_SIZE;
			 String page_str = getPara("page");
			 String pageSize_str = getPara("limit");
			 if(keyword == null){
				 keyword = "";
			 }
			 
			 if(page_str != null && !"".equals(page_str)){
				 page = Integer.valueOf(page_str);
			 }
			 
			 if(pageSize_str != null && !"".equals(pageSize_str)){
				 pageSize = Integer.valueOf(pageSize_str);
			 }
			 
			 Page<Record>page_list = CompanyModel.dao.getCompanyList(page, pageSize, keyword);
		     map.put("data", page_list);
		     map.put("code", ConstsObject.SUCCESS_CODE);
   	         map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
   		     renderJson(map);
		} catch (Exception e) {
			//如果不写这段代码 由于 捕获了异常   异常拦截器不会将 错误记录到错误日志中  所以 在这里需要手动 记录到日志
	        StringBuilder sb =new StringBuilder("\n---Exception Log Begin---\n");
	        sb.append("Exception Type:").append(e.getClass().getName()).append("\n");
	        sb.append("Exception Details:");
	        log.error(sb.toString(),e);
			 map.put("code", ConstsObject.ERROR_CODE);
	 	     map.put("msg",  ConstsObject.SEARCH_ERROR_MSG);
	  		 renderJson(map);
		}

		
	 }
	 
	 

}
