package com.trunko.web.dao.project;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
/**
 * 项目年度计划模型
 */
public class ProjectPlanModel extends Model<ProjectPlanModel> {

	private static final long serialVersionUID = 1L;
	public static  ProjectPlanModel dao = new ProjectPlanModel();
	
	public boolean saveProjectPlan(Record plan){
		
		return Db.save("tb_project_plan", "plan_id", plan);
	}
	
	public Record getProjectPlan(int pro_id){
		
		return Db.findFirst("select * from tb_project_plan where project_id =? ", pro_id);
		
	}
	
	public boolean updateProjectPlan(Record plan){
		
		return Db.update("tb_project_plan", "plan_id", plan);
	}
	

	
	public boolean delProjectPlan(int plan_id){
		return Db.deleteById("tb_project_plan", "plan_id", plan_id);
	}
	
	
	public Record getProjectPlanId(int pro_id){
		
		return Db.findFirst("select plan_id from tb_project_plan where project_id =? ", pro_id);
		
	}
	
}
