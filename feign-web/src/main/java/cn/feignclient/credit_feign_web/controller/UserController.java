package cn.feignclient.credit_feign_web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	
	private final static Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping("/findbyMobile")
	public BackResult<CreUserDomain> findbyMobile(HttpServletRequest request, HttpServletResponse response,
			String mobile, String token) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();

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

		if (!isLogin(mobile, token)) {
			result.setResultCode(ResultCode.RESULT_SESSION_STALED);
			result.setResultMsg("注销校验失败无法注销");
			return result;
		} 
		
		try {
			result = userFeignService.findbyMobile(mobile);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户手机号：" + mobile + "执行查询用户信息出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}
		
		return result;
	}
	
	@RequestMapping("/updateCreUser")
	public BackResult<CreUserDomain> updateCreUser(HttpServletRequest request, HttpServletResponse response, String token ,CreUserDomain creUserDomain) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();

		if (CommonUtils.isNotString(creUserDomain.getUserPhone())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
			
		}

		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}

		if (!isLogin(creUserDomain.getUserPhone(), token)) {
			result.setResultCode(ResultCode.RESULT_SESSION_STALED);
			result.setResultMsg("注销校验失败无法注销");
			return result;
		} 
		
		try {
			result = userFeignService.updateCreUser(creUserDomain);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户手机号：" + creUserDomain.getUserPhone() + "执行修改信息出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}
		
		return result;
	}
}
