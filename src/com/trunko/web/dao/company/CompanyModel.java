package com.trunko.web.dao.company;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 援疆企业模型
 */
public class CompanyModel extends Model<CompanyModel> {

	private static final long serialVersionUID = 1L;

	public static CompanyModel dao = new CompanyModel();

	public boolean checkIfexist(String companyName, String id) {
		if (!"".equals(id)) {
			return Db.findFirst(
							"select comp_name from tb_company where comp_name=? and comp_id!=?",
							companyName, id) != null;
		} else {
			return Db.findFirst(
					"select comp_name from tb_company where comp_name=?",
					companyName) != null;
		}
	}

	public boolean saveCompany(Record com) {

		return Db.save("tb_company", "comp_id", com);

	}

	public boolean updateCompany(Record com) {

		return Db.update("tb_company", "comp_id", com);

	}

	public Page<Record> getCompanyList(int page, int pageSize, String keyword) {

		String from_sql = "from tb_company   where 1=1  ";

		if (!"".equals(keyword)) {
			from_sql += " and  p.pro_name like '%" + keyword + "%' ";
		}

		return Db.paginate(page, pageSize, "select * ", from_sql);

	}

	public Record getCompany(int comp_id) {

		return Db.findById("tb_company", "comp_id", comp_id);

	}
	
	public boolean delCompany(int comp_id){
		
		return Db.deleteById("tb_company", "comp_id", comp_id);
		
	}

}
