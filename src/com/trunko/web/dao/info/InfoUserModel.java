package com.trunko.web.dao.info;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 消息接收用户模型
 * @author Administrator
 *
 */
public class InfoUserModel extends Model<InfoUserModel>{

	private static final long serialVersionUID = 1L;
	public static InfoUserModel dao = new InfoUserModel();
	
	public boolean saveUsers(List<Record>list) {
		
		return Db.batchSave("tb_info_user", list, list.size()).length == list.size();
	}
	
	public int[] updateUsers(List<Record>list) {
		
		return Db.batchUpdate("tb_info_user", "info_user_id", list, list.size());
	}
	
	public List<Record> getUsers(int id){
		return Db.find("select info_username from tb_info_user where info_pid = ?", id);
	}
	
	public int delUsers(int info_id){
		
		return Db.delete("delete from tb_info_user where info_pid =?",info_id);
	}

}
