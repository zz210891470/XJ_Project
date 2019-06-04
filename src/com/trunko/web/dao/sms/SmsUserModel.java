package com.trunko.web.dao.sms;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 短信接收用户
 */
public class SmsUserModel extends Model<SmsUserModel>{
	
	private static final long serialVersionUID = 1L;
	public static SmsUserModel dao = new SmsUserModel();
	public  boolean saveReceiveUsers(List<Record>list){
		
		return Db.batchSave("tb_msg_user", list, list.size()).length ==list.size();
	}
	

	public List<Record>getRecUserlist(int msg_id){
		return Db.find("select user_name from tb_msg_user where msg_pid =?", msg_id);
	}
	
	
	public int delMsgList(int msg_id){
		
		return Db.delete("delete from tb_msg_user where msg_pid = ?", msg_id);
		
	}

}
