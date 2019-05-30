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
	

}
