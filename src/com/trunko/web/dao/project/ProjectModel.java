package com.trunko.web.dao.project;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;


/**
 * 项目基本模型
 */
public class ProjectModel extends Model<Model> {

	private static final long serialVersionUID = 1L;
	public static ProjectModel dao = new ProjectModel();
	
	public boolean saveProject(Record record){
		
		return Db.save("tb_project", "pro_id", record);
		
	}
	
	public Record getProjectById(int pro_id){
		return Db.findById("tb_project", "pro_id", pro_id);
	}
	

    
    
}
