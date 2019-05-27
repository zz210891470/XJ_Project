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
    	
		return Db.batchSave("tb_project", list, list.size()).length == list.size();
    	
    }
}
