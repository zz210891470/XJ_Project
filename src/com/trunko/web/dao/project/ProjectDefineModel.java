package com.trunko.web.dao.project;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
/**
 * 项目自定义模型
 */
public class ProjectDefineModel extends Model<ProjectDefineModel> {

	private static final long serialVersionUID = 1L;
	public static ProjectDefineModel dao = new ProjectDefineModel();
    public boolean saveProjectDefine(List<Record>list){
    	
		return Db.batchSave("tb_project_define", list, list.size()).length == list.size();
    	
    }
    
    public List<Record>getProjectDefine(int pro_id){
    	
		return Db.find("select * from tb_project_define where project_id=?", pro_id);
    	
    }
    
    public boolean updateProjectDefine(List<Record>list){
    	
		return Db.batchUpdate("tb_project_define","project_define_id", list, list.size()).length == list.size();

    	
    }
}
