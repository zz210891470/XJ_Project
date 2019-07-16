package com.trunko.web.controller.info;

import java.io.File;
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
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.trunko.anoation.CrossOrigin;
import com.trunko.common.ConstsObject;
import com.trunko.web.dao.img.ImageModel;
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
			.set("info_org_id",jo.get("org_id"))
			.set("info_time",jo.get("info_time"));
			
			 boolean flag = InfoModel.dao.saveInfo(info);
			
			if(flag){
				 JSONArray img_ja =(JSONArray) jo.get("img_arr");
			      //保存图片
	    	      if(img_ja.size()>0){
	    	    	  List<Record>imglist = new ArrayList<Record>();
	    	    	  for (int i = 0; i < img_ja.size(); i++) {
	    	    		  JSONObject j =  img_ja.getJSONObject(i);
	    	    		  Record im = new Record();
	    	    		  im.set("imgPid", info.get("info_id"));
	    	    		  im.set("imgUrl", j.getString("url"));
	    	    		  im.set("imgName", j.getString("filename"));
	    	    		  im.set("imgType", "消息类型");
	    	    	
	    	    		  imglist.add(im);
					}
	    	    	  ImageModel.dao.batchSaveImgs(imglist);
	    	    	 
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
	
	//获取信息列表
	public void getInfoList(){
		  Map<String,Object> map = new HashedMap();
		  String org_id = getPara("org_id");
		  if(org_id!=null){
				int page = ConstsObject.PAGE_NO;
				int pageSize = ConstsObject.PAGE_SIZE;
				String keyword = getPara("keyword");
				String info_type = getPara("info_type");
				String start_date = getPara("start_date");
				String end_date = getPara("end_date");
				String pageStr = getPara("page");
				String pageSizeStr = getPara("limit");
				 
				if(keyword == null||"".endsWith(keyword)){
					keyword = "";
				}
				if(info_type == null){
					info_type = "";
				}
				if(start_date == null){
					start_date = "";
				}
				if(end_date == null){
					end_date = "";
				}
				if(pageStr!= null&&!"".equals(pageStr)){
					page = Integer.valueOf(pageStr);
				}
				if(pageSizeStr!= null&&!"".equals(pageSizeStr)){
					
					pageSize = Integer.valueOf(pageSizeStr);
				}
				if(info_type == null){
					info_type = "";
				}
				
				Page<Record>pagelist = InfoModel.dao.getInfoList(page, pageSize, keyword, start_date, end_date,org_id,info_type);
				map.put("data", pagelist);
			    map.put("code", ConstsObject.SUCCESS_CODE);
			    map.put("msg", ConstsObject.SEARCH_SUCCESS_MSG);
				renderJson(map);
		  }else{
			   map.put("code", ConstsObject.ERROR_CODE);
	 	       map.put("msg",  ConstsObject.SEARCH_ERROR_MSG);
	  		   renderJson(map);
		  }
	
	}
	
	
	public void getInfo(){
		 Map<String,Object> map = new HashedMap();
		String info_id = getPara("info_id");
		if(info_id != null && !"".equals(info_id)){
			int id = Integer.valueOf(info_id); 
			Record info = InfoModel.dao.getInfo(id);
			List<Record>imgs = ImageModel.dao.getImglist(id,"消息类型");
			info.set("img_arr", imgs);
/*			List<Record>rec_user = InfoUserModel.dao.getUsers(id);
			List<String>users = new ArrayList<String>();
			for(int i =0 ;i <rec_user.size();i++){
				users.add(rec_user.get(i).getStr("info_username"));
			}*/
			//info.set("org_user",users);
			map.put("data",info);
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
				
	    	      JSONArray img_ja = jo.getJSONArray("img_arr");
	    	      //保存图片
	    	      if(img_ja.size()>0){
	    	    	  //删除旧图片
	    	    	  ImageModel.dao.deleteImgs(Integer.valueOf(info_id),"消息类型");
	    	    	  //删除硬盘图片
	    	    	  List<Record>oldImgs = ImageModel.dao.getImglist(Integer.valueOf(info_id),"消息类型");
	    	    	  for (int i = 0; i < oldImgs.size(); i++) {
	    	    		  String url = oldImgs.get(i).getStr("url");
	    	    		  int idx = url.indexOf("upload/");
	    	    		  String imgurl = url.substring(idx+7);
	    	    		  
	    	    			String path = getRequest().getSession().getServletContext().getRealPath("/upload/"+imgurl);
	    	    			System.out.println("删除路径:"+path);
	    	    			File file = new File(path);
	    	    			System.out.println(file);
	    	    			if (file.exists() && file.isFile()){
	    	    				  file.delete();
	    	    			}
	    	    			
					   }
	    	    	  
	    	    	  List<Record>imglist = new ArrayList<Record>();
	    	    	  for (int i = 0; i < img_ja.size(); i++) {
	    	    		  JSONObject j =  img_ja.getJSONObject(i);
	    	    		  Record im = new Record();
	    	    		  im.set("imgPid", Integer.valueOf(info_id));
	    	    		  im.set("imgUrl", j.getString("url"));
	    	    		  im.set("imgName", j.getString("filename"));
	    	    		  im.set("imgType", "消息类型");
	    	    		  imglist.add(im);
					}
	    	    	  ImageModel.dao.batchSaveImgs(imglist);
	    	    	 
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
	
	//获取首页信息列表
	public void getIndexInfoList(){	
		String org_id = getPara("org_id");
		Map<String,Object> map = new HashedMap();
		if(org_id!=null&&!"".equals(org_id)){
			List<Record>list = InfoModel.dao.getIndexInfoList(org_id);
			renderJson(list);
		}else{
		  	 map.put("code", ConstsObject.ERROR_CODE);
		     map.put("msg","无权限访问");
		     renderJson(map);
		}
		
	}
	
	

}
