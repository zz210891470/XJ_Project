package com.trunko.web.dao.img;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 图片模型
 * @author Administrator
 *
 */
public class ImageModel extends Model<ImageModel> {

	private static final long serialVersionUID = 1L;
	
	public static ImageModel dao = new ImageModel();
	
	public boolean batchSaveImgs(List<Record>imglist){
		
		return Db.batchSave("tb_imgs", imglist, imglist.size()).length == imglist.size();
		
	}
	
	public List<Record>getImglist(int monthid,String flag){
		String sql = "select imgName as filename, imgUrl as url from tb_imgs where imgPid = ? and imgType =? ";
		return Db.find(sql, monthid,flag);
		
	}
	
	public int deleteImgs(int monid,String flag){
		String sql = "delete from tb_imgs where imgPid = ? and imgType =? ";
		return Db.delete(sql, monid,flag);
		
		
	}

}
