package com.trunko.web.dao.form;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.trunko.web.dao.project.ProjectDefineModel;
import com.trunko.web.dao.project.ProjectModel;

/**
 * 表单
 */
public class FormModel extends Model<FormModel>{
	
	private static final long serialVersionUID = 1L;
	public static FormModel dao = new FormModel();
    
    public boolean insertForms(List<Record>forms){
    	
		return Db.batchSave("tb_form_config", forms,forms.size()).length ==  forms.size();
    	
    }
    
    public List<Record>getForms(String org_id,String flag){
    	
    	String sql = "select form_data,form_type,org_id,form_title,form_desc from tb_form_config where org_id = ?";
    	if("all".equals(flag)){
    		sql = "select * from tb_form_config where org_id = ?";
    	}
		return Db.find(sql, org_id);
    	
    }
    
    public boolean updateForm(Record form){
    	
		return Db.update("tb_form_config", "form_id", form);
    	
    }
    
    
    public boolean updateForms(List<Record>form_list){
    	
		return Db.batchUpdate("tb_form_config", "form_id", form_list, form_list.size()).length == form_list.size();
    	
    }


}
