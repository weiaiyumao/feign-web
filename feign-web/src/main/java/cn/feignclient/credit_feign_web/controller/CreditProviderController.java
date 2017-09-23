package cn.feignclient.credit_feign_web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.CreditProviderService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.RunTestDomian;

@RestController
@RequestMapping("/credit")
public class CreditProviderController extends BaseController{
	
	@Autowired
    CreditProviderService creditProviderService;
	
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    @ResponseBody
    public String sayHi(@RequestParam String name){
        return creditProviderService.sayHiFromClientOne(name);
    }
    
    @RequestMapping(value = "/runTheTest", method = RequestMethod.GET)
	public BackResult<RunTestDomian> runTheTest(HttpServletRequest request, HttpServletResponse response,String fileUrl,String userId,String mobile,String token) {
    	
    	response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		BackResult<RunTestDomian> result = new BackResult<RunTestDomian>();

		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
		}

		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		
		if (CommonUtils.isNotString(fileUrl)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("文件地址不能为空");
			return result;
		}

		if (CommonUtils.isNotString(userId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户ID 不能为空");
			return result;
		}
		
		if (!isLogin(mobile, token)) {
			result.setResultCode(ResultCode.RESULT_SESSION_STALED);
			result.setResultMsg("用户已经注销登录无法进行操作");
			return result;
		}
		
		return creditProviderService.runTheTest(fileUrl, userId);
	} 
    
    
    
}
