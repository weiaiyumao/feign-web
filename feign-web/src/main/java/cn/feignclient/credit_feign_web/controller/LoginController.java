package cn.feignclient.credit_feign_web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.domain.TokenUserDomain;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import cn.feignclient.credit_feign_web.utils.MD5Util;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;
import main.java.cn.sms.util.ChuangLanSmsUtil;

@RestController
@RequestMapping("/login")
public class LoginController extends BaseController{
	
	/**
	 * 发送手机号码
	 * @param request
	 * @param response
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/sendSms")
	public BackResult<Boolean> sendSms(HttpServletRequest request, HttpServletResponse response, String mobile) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		Assert.notNull(mobile, "The param mobile not be null!");
		BackResult<Boolean> result = new BackResult<Boolean>();
		
		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
		}

		int code = (int)((Math.random()*9+1)*100000);
		
		String sessionCode = redisClinet.get("se_ken_" + mobile);
		
		if (null == sessionCode || sessionCode.equals("")) {
			sessionCode = String.valueOf(code);
		}
		
		Boolean fag = ChuangLanSmsUtil.getInstance().sendSmsByMobile(mobile, sessionCode);
		
		if (!fag) {
			result.setResultMsg("发送失败！");
			result.setResultCode(ResultCode.RESULT_FAILED);
			return result;
		}
		
		System.out.println("验证码：" + sessionCode);
		
		redisClinet.set("se_ken_" + mobile, sessionCode);
		
		return result;
	}
	
	
	
	/**
	 * 登录
	 * @param request
	 * @param response
	 * @param mobile
	 * @param code
	 * @return
	 */
	@RequestMapping("/userLogin")
	public BackResult<TokenUserDomain> userLogin(HttpServletRequest request, HttpServletResponse response, String mobile,String code) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		BackResult<TokenUserDomain> result = new BackResult<TokenUserDomain>();

		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
		}

		if (CommonUtils.isNotString(code)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("code不能为空");
			return result;
		}
		
		String sessionCode = redisClinet.get("se_ken_" + mobile);
		
		if (!sessionCode.equals(code)) {
			result.setResultMsg("验证码验证错误！");
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			return result;
		} 
		
		String userMd5str = "";
		
		CreUserDomain creUserDomain = new CreUserDomain();
		creUserDomain.setUserPhone(mobile);
		
		// 查询用户
		BackResult<CreUserDomain> creResult = userFeignService.findOrsaveUser(creUserDomain);
		
		if(!creResult.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
			result.setResultMsg("检查用户失败");
			result.setResultCode(ResultCode.RESULT_FAILED);
			return result;
		}
		
		userMd5str = MD5Util.getInstance().getMD5Code("user_token_"+creResult.getResultObj().getId());
		
		// 清空 se_ken_
		redisClinet.remove("se_ken_"+mobile);
		
		redisClinet.set("user_token_" + mobile, userMd5str);
		
		TokenUserDomain user = new TokenUserDomain();
		user.setToken(userMd5str);
		result.setResultObj(user);
		
		return result;
	}
	
	/**
	 * 登出
	 * @param request
	 * @param response
	 * @param mobile
	 * @param token
	 * @return
	 */
	@RequestMapping("/logout")
	public BackResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response, String mobile,String token) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<Boolean> result = new BackResult<Boolean>();
		
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
		
		// 清空 se_ken_
		redisClinet.remove("user_token_"+mobile);
		result.setResultObj(true);
		
		return result;
	}
	
	/**
	 * 检测是否已经登出
	 * @param request
	 * @param response
	 * @param mobile
	 * @param token
	 * @return
	 */
	@RequestMapping("/isLogout")
	public BackResult<Boolean> isLogout(HttpServletRequest request, HttpServletResponse response, String mobile,String token) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<Boolean> result = new BackResult<Boolean>();
		
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
		
		Boolean fag = isLogin(mobile, token);
		
		result.setResultObj(fag);
		result.setResultMsg(fag ? "处于登录状态" : "用户已经注销登录");
		
		return result;
	}
	
}
