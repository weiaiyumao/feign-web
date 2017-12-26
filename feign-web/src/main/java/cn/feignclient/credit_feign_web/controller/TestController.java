package cn.feignclient.credit_feign_web.controller;

import com.netflix.governator.annotations.binding.Main;

import main.java.cn.hhtp.util.HttpUtil;
import net.sf.json.JSONObject;

public class TestController {
	
	
	public static void main0(String[] args) {
		String url  = "http://api.xingjk.cn/api/do.php";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("action", "loginIn");
		jsonObject.put("name", "api-zyy0apgt");
		jsonObject.put("password", "wzq199623");
		String response = HttpUtil.createHttpPost(url, jsonObject);
		System.out.println(response);
		// 305153ec-342b-4adc-b476-21f2779a30a1
		
	}
	
	public static void main2(String[] args) {
		String url  = "http://api.xingjk.cn/api/do.php";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("action", "getPhone");
		jsonObject.put("sid", "57034");
		jsonObject.put("token", "305153ec-342b-4adc-b476-21f2779a30a1");
		String response = HttpUtil.createHttpPost(url, jsonObject);
		System.out.println(response);
		// 305153ec-342b-4adc-b476-21f2779a30a1
	}
	
	
	// http://api.xingjk.cn/api/do.php?action=getMessage&sid=项目id&phone=取出来的手机号&token=登录时返回的令牌
	
	public static void main4(String[] args) {
		String url  = "http://api.xingjk.cn/api/do.php";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("action", "getMessage");
		jsonObject.put("sid", "57034");
		jsonObject.put("phone", "17184310746");
		jsonObject.put("token", "305153ec-342b-4adc-b476-21f2779a30a1");
		String response = HttpUtil.createHttpPost(url, jsonObject);
		System.out.println(response);
		// 305153ec-342b-4adc-b476-21f2779a30a1
	}
	
	public static void main3(String[] args) {
		String url  = "http://api.xingjk.cn/api/do.php";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("action", "getMessage");
		jsonObject.put("sid", "57034");
		jsonObject.put("phone", "17184310746");
		jsonObject.put("token", "305153ec-342b-4adc-b476-21f2779a30a1");
		String response = HttpUtil.createHttpPost(url, jsonObject);
		System.out.println(response);
		// 305153ec-342b-4adc-b476-21f2779a30a1
	}
	
	public static void main(String[] args) {
		String url  = "http://api.xingjk.cn/api/do.php";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("action", "getPhone");
		jsonObject.put("sid", "57034");
		jsonObject.put("token", "305153ec-342b-4adc-b476-21f2779a30a1");
		String response = HttpUtil.createHttpPost(url, jsonObject);
		
		String[] mobile = response.split("|");
		System.out.println(response);
		// 获取手机号
		
		// 网站发送短信
		String urlcode  = "http://manager.yunke66.com/api!sms.action";
		JSONObject jsonObjectcode = new JSONObject();
		jsonObjectcode.put("mobile", mobile[1]);
		jsonObjectcode.put("type", "1");
		String responsecode = HttpUtil.createHttpPost(urlcode, jsonObjectcode);
		System.out.println(responsecode);
		
//		document.getElementById("get_code").onclick=function(){time(this);}
//	    $("#get_code").click(function () {
//	        var url = "api!sms.action";
//	        var mobile = $("input[name='mobile']").val();
//	        ajax("post",url,{"key":"{mobile:"+mobile+",type:1}"},function(data){
//	            if(data.success){
//	                successmsg("发送成功！");
//	            } else {
//	                errormsg($.parseJSON(data.message).message);
//	            }
//	        });
//	    })
		// 获取验证码
		
		String urlgetCode  = "http://api.xingjk.cn/api/do.php";
		JSONObject jsonObjectgetCode = new JSONObject();
		jsonObjectgetCode.put("action", "getMessage");
		jsonObjectgetCode.put("sid", "57034");
		jsonObjectgetCode.put("phone", "17184310746");
		jsonObjectgetCode.put("token", "305153ec-342b-4adc-b476-21f2779a30a1");
		String responsegetCode = HttpUtil.createHttpPost(urlgetCode, jsonObjectgetCode);
		System.out.println(responsegetCode);
		
		
		// 自动注册
		
//		  var formUrl  = "api!register.action";
//	        var formData = JSON.stringify($('#regform').serializeObject());
//	        formData = formData.replace(/area/, "regionId");
//	        var formJson = {"key":formData};
//	        ajax("post",formUrl,formJson,function(data){
//	            if(data.success){
//	                successmsg("注册成功");
//	                window.location.href="downloadpage.html";
//	            } else {
//	                errormsg($.parseJSON(data.message).message);
//	            }
//	        });
		
	}
}
