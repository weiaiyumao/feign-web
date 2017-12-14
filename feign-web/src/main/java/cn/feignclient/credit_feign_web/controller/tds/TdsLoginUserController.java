package cn.feignclient.credit_feign_web.controller.tds;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsUserLoginFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsFunctionDomain;
import main.java.cn.domain.tds.TdsUserDomain;
import main.java.cn.hhtp.util.MD5Util;

@RestController
@RequestMapping("/userLogin")
public class TdsLoginUserController extends BaseController {
	
	
	private final static Logger logger = LoggerFactory.getLogger(TdsLoginUserController.class);

	@Autowired
	private TdsUserLoginFeignService tdsUserLoginFeignService;


	/**
	 * 登录
	 * 
	 * @param code
	 *            验证码
	 * @return
	 */
	@RequestMapping("/login")
	public BackResult<TdsUserDomain> userLogin(HttpServletRequest request, HttpServletResponse response, String name,
			String passWord,String code) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsUserDomain> result = new BackResult<TdsUserDomain>();
		if (CommonUtils.isNotString(name)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户名不能为空");
			return result;
		}

		if (CommonUtils.isNotString(passWord)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("密码不能为空");
			return result;
		}

		if (CommonUtils.isNotString(code)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("验证码不能为空");
			return result;
		}
		
		TdsUserDomain tdsUserDomain = new TdsUserDomain();
		tdsUserDomain.setName(name);
		//密码加密
		tdsUserDomain.setPassword(MD5Util.getInstance().getMD5Code(passWord));
		tdsUserDomain.setLastLoginTime(new Date());
		//ip获取
		tdsUserDomain.setLoginIp(getIpAddr(request));
		//登录
		result = tdsUserLoginFeignService.login(tdsUserDomain);
		// 登录失败
		if (!result.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
			return result;
		}
		// 生成tonke
		String tokenUserId = MD5Util.getInstance().getMD5Code("tds_user_token_" + result.getResultObj().getId());
		// 清空 se_ken_
		// redisClinet.remove("tdsse_ken_" + mobile);//
		redisClinet.set("tds_user_token_" + result.getResultObj().getPhone(), tokenUserId);
		result.getResultObj().setToken(tokenUserId);
		logger.info("=========用户名：" +name+ "用户登录成功============");
		return result;
	}
	
	

	
	
	/**
	 * 模块加载
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/moduleLoadingByUsreId")
	public BackResult<List<TdsFunctionDomain>> moduleLoadingByUsreId(Integer userId, HttpServletRequest request,
			HttpServletResponse response,String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<List<TdsFunctionDomain>> result = new BackResult<List<TdsFunctionDomain>>();
		
		if (CommonUtils.isNotIngeter(userId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		 result=tdsUserLoginFeignService.moduleLoadingByUsreId(userId);
		 
		 // TODO redis保存
		  
		 //end
		 logger.info("用户id"+userId+"==========模块加载成功============！");
		 return result;
		
 	 }

}
