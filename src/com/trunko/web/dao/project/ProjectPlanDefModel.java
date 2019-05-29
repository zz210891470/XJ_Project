package com.trunko.web.dao.project;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 项目年度计划 自定义模型
 */
public class ProjectPlanDefModel extends Model<ProjectPlanDefModel>{

	private static final long serialVersionUID = 1L;
	public static ProjectPlanDefModel dao = new ProjectPlanDefModel();
	public boolean saveProjectPlanDef(List<Record>planlist){
		
		return Db.batchSave("tb_project_plan_define", planlist, planlist.size()).length  == planlist.size();
		
	}
	
    public List<Record>getProjectPlanDefine(int plan_id){
    	
		return Db.find("select * from tb_project_plan_define where plan_id=?", plan_id);
    	
    }
    
	public boolean updateProjectPlanDef(List<Record>planlist){
		
		return Db.batchUpdate("tb_project_plan_define","plan_define_id", planlist, planlist.size()).length  == planlist.size();
		
	}

}
