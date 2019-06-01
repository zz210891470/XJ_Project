package com.trunko.web.dao.month;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 月报自定义模型
 */
public class MonthDefineModel extends Model<MonthDefineModel>{
	

	private static final long serialVersionUID = 1L;
	public static MonthDefineModel dao = new MonthDefineModel();
	
	public boolean saveMonthDef(List<Record>reportlist){
		
		return Db.batchSave("tb_month_define", reportlist, reportlist.size()).length  == reportlist.size();
		
	}
	
    public boolean updateMonthDefine(List<Record>list){
    	
		return Db.batchUpdate("tb_month_define","month_define_id", list, list.size()).length == list.size();

    }
    
    
    public List<Record>getMonthDefList(int month_id){
    	
		return Db.find("select * from tb_month_define where month_report_id = ? ", month_id);
    	
    }
    
    public int delMonthDef(int month_id){
    	
		return Db.delete("delete from tb_month_define where month_report_id = ?", month_id) ;
    	
    }
    


}
