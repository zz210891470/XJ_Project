package com.trunko.web.dao.info;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
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
	
	
	public Page<Record>getInfoList(int pageNumber,int pageSize,String keyword,String startDate,String endDate){
		List<Object> list = new ArrayList<Object>();
		String sql = "select * ";
		String fromSql = " from tb_info ";
		if(!"".equals(keyword)){
			fromSql+= " info_title like '%"+keyword+"%' ";
		}
		
		if(!"".equals(startDate)&&"".equals(endDate)){
			fromSql+= " info_time >= ? ";
			list.add(startDate);
		}else if("".equals(startDate)&&!"".equals(endDate)){
			fromSql+= " info_time <= ? ";
			list.add(startDate);
		}else if(!"".equals(startDate)&&!"".equals(endDate)){
			fromSql+= " info_time >= ?  and info_time <= ? ";
			list.add(startDate);
			list.add(endDate);
		}
		
		return Db.paginate(pageNumber, pageSize, sql, fromSql, list.toArray());
	}

}
