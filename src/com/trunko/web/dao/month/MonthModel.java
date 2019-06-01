package com.trunko.web.dao.month;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
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
	
	public boolean updateMonth(Record record){
				
		return Db.update("tb_month_report", "report_id", record);
		
	}
	
	
    public List<Record>getMonthList(int pro_id,int month){
    
    	
		return Db.find("select * from tb_month_report m left join  tb_project p on p.pro_id = m.project_id where p.pro_id=? and m.report_month=? order by p.pro_year asc",pro_id,month);
    	
    }
    
    
    
    public Page<Record>getReportProjectList(int page,int pageSize,String username,String keyword,int year,int month,String org_id){
		List<Object> list = new ArrayList<Object>();

		String from_sql = "from tb_project p left join tb_month_report m on p.pro_id = m.project_id where  p.pro_year =?  and p.pro_org_id =? and m.report_month=? ";
		list.add(year);
		list.add(org_id);
		list.add(month);
		if(!"".equals(username)){
			from_sql += " and p.pro_username = ? ";
			list.add(username);
		}
		
		if(!"".equals(keyword)){
			from_sql += "  and p.pro_name like '%"+keyword+"%' ";
			list.add(keyword);
		}
			
		
		from_sql +=" and p.pro_audit_state ='审核通过' order by p.pro_id desc";
		
		return  Db.paginate(page, pageSize, "select p.pro_id,p.pro_name,p.pro_year,m.report_month_complete,m.report_year_complete,m.report_createtime,m.report_state ", from_sql, list.toArray());


		
	}
    
    public Record getMonthDetail(int project_id,int month){
    	String sql = "select  * from tb_project p left join  tb_month_report m  on p.pro_id = m.project_id  and  m.report_month=? where p.pro_id=? ";
		return Db.findFirst(sql,month,project_id);
    }
	
    
    public boolean delMonth(int month_id){
    
		return Db.deleteById("tb_month_report", "report_id", month_id);
    	
    }
    
    public boolean updateStatus(int month_id,String status){
		return Db.update("update tb_month_report set report_state =? where report_id=? ",status, month_id) == 1;
    	
    }
	

}
