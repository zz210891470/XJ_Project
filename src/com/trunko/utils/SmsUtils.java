package com.trunko.utils;

import com.jfinal.kit.PropKit;



public class SmsUtils {

	public static String url = PropKit.get("smsurl");
	public static String userid = PropKit.get("smsuserid");
	public static String account = PropKit.get("smsaccount");
	public static String password = PropKit.get("smspassword");

   
	public static String keyword(String checkWork) {

		String keyword = SmsClientKeyword.queryKeyWord(url, userid, account,
				password, checkWork);
		System.out.println(keyword);
		return keyword;
	}

	public static String overage() {

		String overage = SmsClientOverage.queryOverage(url, userid, account,
				password);
		System.out.println(overage);
		return overage;
	}
	
	public static String sendMsg(String mobiles,String content) {
		String result = SmsClientSend.sendSms(url, userid, account, password, mobiles, content);
		System.out.println(result);
		return result;
	}

	public static void test() {
		String send = SmsClientAccessTool.getInstance().doAccessHTTPPost(
				"http://118.145.30.35/sms.aspx", "", "utf-8");
		System.out.println(send);
	}
}
