package com.trunko.web.controller.form;

import com.jfinal.core.Controller;
import com.trunko.anoation.CrossOrigin;

/**
 * 表单控制器
 */
@CrossOrigin
public class FormController extends Controller{
     //获取所有表单
     public  void getForm(){

      String rog_id = "1111";
      renderJson(rog_id);
     }


}
