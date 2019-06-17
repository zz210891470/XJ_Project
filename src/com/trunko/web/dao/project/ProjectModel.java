package com.trunko.web.dao.project;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


/**
 * 项目基本模型
 */
public class ProjectModel extends Model<Model> {

	private static final long serialVersionUID = 1L;
	public static ProjectModel dao = new ProjectModel();
	
	public boolean saveProject(Record record){
		
		return Db.save("tb_project", "pro_id", record);
		
	}
	
	public Record getProjectById(int pro_id){
		return Db.findById("tb_project", "pro_id", pro_id);
	}
	
	public boolean updateProject(Record r){
		
		return Db.update("tb_project", "pro_id", r);
		
	}
	
	
	
	//获取上报用户 项目列表
	public Page<Record>getProjectList(int page,int pageSize,String username,String keyword,int year,String city,String country,String pro_type,String industry,String subscotors,String flag,String org_id){
		List<Object> list = new ArrayList<Object>();

		String from_sql = "from tb_project where 1=1 and pro_year =? and pro_org_id =? ";
		list.add(year);
		list.add(org_id);

		
		if(!"".equals(keyword)){
			from_sql += "  and pro_name like '%"+keyword+"%' ";
		}
		
		
		if(!"".equals(city)){
			from_sql += " and pro_city = ? ";
			list.add(city);
		}
		
		if(!"".equals(country)){
			from_sql += " and pro_county = ? ";
			list.add(country);
		}
		
		if(!"".equals(pro_type)){
			from_sql += " and pro_type = ? ";
			list.add(pro_type);
		}
		
		
		if(!"".equals(industry)){
			from_sql += " and pro_industry = ? ";
			list.add(industry);
		}
		if(!"".equals(subscotors)){
			from_sql += " and pro_subsectors = ? ";
			list.add(subscotors);
		}
		
		//草稿箱
		if("draft".equals(flag)){
				from_sql += " and pro_username = ? and pro_audit_state ='未上报'  ";
				list.add(username);
		
		}else if("reported".equals(flag)){
			
			   if(!"".equals(username)){
					from_sql += " and pro_username = ? ";
					list.add(username);
				}
			
			//当前用户已上报项目
			from_sql += "  and pro_audit_state !='未上报' and pro_audit_state !='已删除' ";
			
		}else if("audit".equals(flag)){
			from_sql += " and pro_audit_user = ? and pro_audit_state ='待审核' ";
			list.add(username);
			
		}else{
			// 管理项目列表(功能不明确待定)
			from_sql += " and pro_audit_state ='审核通过' ";
			
		}
		
		from_sql +=" order by pro_id desc";
		return  Db.paginate(page, pageSize, "select pro_id,pro_proc_id,pro_proc_inst_id,pro_audit_user,pro_name,pro_year,pro_audit_state,pro_investment,pro_industry,pro_subsectors ", from_sql, list.toArray());


		
	}
	
	
	public boolean delProject(int pro_id,String way){
		String sql = "update tb_project set pro_audit_state = '已删除' where pro_id = ?";
		if("delete".equals(way)){
			sql = "delete from tb_project where pro_id = ?";
		}
		return Db.update(sql, pro_id) == 1;
		
	}
	
	public boolean updateStatus(int id,String status){
		String sql = "update tb_project set pro_audit_state = ? where pro_id = ?";
		return Db.update(sql, status,id) == 1;
	}

    
    
}
