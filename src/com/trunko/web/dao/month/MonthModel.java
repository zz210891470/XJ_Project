package com.trunko.web.dao.month;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 月报基本模型
 */
public class MonthModel extends Model<MonthModel> {
	
	private static final long serialVersionUID = 1L;
	public static MonthModel dao = new MonthModel();
	public boolean saveMonth(Record record){
		
		return Db.save("tb_month_report", "report_id", record);
		
	}
	

}
