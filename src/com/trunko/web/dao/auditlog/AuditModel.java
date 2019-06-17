package com.trunko.web.dao.auditlog;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
/**
 * 审核日志
 */
public class AuditModel extends Model<AuditModel>{

	private static final long serialVersionUID = 1L;
	public static AuditModel dao = new AuditModel();
	public boolean saveAudit(Record r){
		return Db.save("tb_audit", "auditId", r);
	}

}
