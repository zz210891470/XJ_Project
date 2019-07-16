package com.trunko.web.controller.img;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.trunko.anoation.CrossOrigin;
import com.trunko.common.ConstsObject;

@CrossOrigin
public class ImgController extends Controller{
	
	private static final  Logger log = Logger.getLogger(ImgController.class);
	
	public void upload(){
		    String method = getRequest().getMethod();
/*		    if("OPTIONS".equals(method)){
		    	renderJson("204");
		    	return;
		    }*/
		   Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			UploadFile file = getFile();
			
			String fileName = file.getFileName();
			//重命名文件名
			String extentionName = fileName.substring(fileName.lastIndexOf("."));
			String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()))+extentionName;
			Calendar cal = Calendar.getInstance();
			//根据年月生成目录名
			String newDirName = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1);
			String name = file.getUploadPath()+"/"+newDirName;
			File f = new File(name);
			if(!f.exists()){
				f.mkdir();
			}
		
			File m = new File(file.getUploadPath()+"/"+file.getFileName());   
			File fl=new File(name+"/"+newFileName);   
			m.renameTo(fl);
			
		
	        String url ="http://"+getRequest().getServerName()+":"+getRequest().getServerPort()+getRequest().getContextPath()+"/upload/"+newDirName+"/"+newFileName;
	     
	        map.put("code", ConstsObject.SUCCESS_CODE);
	        map.put("url",url);
	        map.put("fileName",newFileName);
	        map.put("dirName",newDirName);
	        map.put("msg","上传成功");
			renderJson(map);
	        
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
	
	
	public void delImg() throws UnsupportedEncodingException{
	 String fileName =getPara("fileName");
	 String dirName =getPara("dirName");
	 //图片如果存在/字符可能会存在问题
	 dirName = dirName.substring(dirName.indexOf("/")+1,dirName.lastIndexOf("/"));
   	 String path = getRequest().getSession().getServletContext().getRealPath("/upload/"+dirName);
   
   	 fileName = URLDecoder.decode(URLDecoder.decode(fileName,"utf-8"),"utf-8");
   	 String pro_id = getPara("pro_id");
   	 System.out.println("删除文件:"+fileName+"id"+pro_id);
		File file = new File(path +File.separator+fileName);
		System.out.println(file);
		if (file.exists() && file.isFile()){
			  file.delete();
		//	 ProjectImg.dao.deleteImg(pro_id, fileName);
			 renderJson("{\"rtnCode\":\"200\"}");
		}else{
			 renderJson("{\"rtnCode\":\"401\"}");
		}
		
		
	}

}
