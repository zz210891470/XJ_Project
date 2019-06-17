package com.trunko.web.dao.sms;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
/**
 * 短信模型
 */
public class SmsModel extends Model<SmsModel>{

	private static final long serialVersionUID = 1L;
	public static SmsModel dao = new SmsModel();
	
	public boolean saveMsg(Record msg){
		
		return Db.save("tb_msg", "msg_id", msg);
		
	}
	public Page<Record>getMsgList(String org_id,int pageNumber,int pageSize,String keyword){
		
		String sql = "select *  ";
		String fromSql = " from tb_msg where msg_org_id = ? ";
		if(!"".equals(keyword)){
			fromSql+=" and send_content like '%"+keyword+"%' ";
		}
		
		return Db.paginate(pageNumber, pageSize, sql, fromSql,org_id);
	}
	
	public Record getMsg(int msg_id){
		
		return Db.findById("tb_msg", "msg_id", msg_id);
		
	}
	
	public boolean delMsg(int msg_id){
		
		return Db.deleteById("tb_msg", "msg_id", msg_id);
	}
	

}
