package com.trunko.web.dao.info;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 信息发布模型
 */

public class InfoModel extends Model<InfoModel>{

	private static final long serialVersionUID = 1L;
	
	public static InfoModel dao = new InfoModel();
	
	public boolean saveInfo(Record info){
		return Db.save("tb_info", "info_id", info);
	}
	
	public Record getInfo(int info_id){
		return Db.findById("tb_info", "info_id", info_id);
	}
	
	public boolean updateInfo(Record info){
		return Db.update("tb_info", "info_id", info);
	}
	
	public boolean delInfo(int id){
		return Db.deleteById("tb_info", "info_id", id);
	}
	

}
