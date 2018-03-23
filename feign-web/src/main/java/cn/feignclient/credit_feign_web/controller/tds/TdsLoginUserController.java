package cn.feignclient.credit_feign_web.controller.tds;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.tds.TdsUserLoginFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsFunctionDomain;
import main.java.cn.domain.tds.TdsModularDomain;
import main.java.cn.domain.tds.TdsUserDomain;
import main.java.cn.hhtp.util.MD5Util;

@RestController
@RequestMapping("/userLogin")
public class TdsLoginUserController extends BaseTdsController {

	private final static Logger logger = LoggerFactory.getLogger(TdsLoginUserController.class);

	@Autowired
	private TdsUserLoginFeignService tdsUserLoginFeignService;

	/**
	 * 登录
	 * @param
	 */
	@RequestMapping("/login")
	public BackResult<TdsUserDomain> userLogin(HttpServletRequest request, HttpServletResponse response, String name,
			String passWord) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsUserDomain> result = new BackResult<TdsUserDomain>();
		if (CommonUtils.isNotString(name)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户账号不能为空");
			return result;
		}

		if (CommonUtils.isNotString(passWord)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("密码不能为空");
			return result;
		}

		TdsUserDomain tdsUserDomain = new TdsUserDomain();
		tdsUserDomain.setPhone(name); // 手机号码
		// 密码加密
		tdsUserDomain.setPassword(MD5Util.getInstance().getMD5Code(passWord)); // 先加密判断
		// ip获取
		tdsUserDomain.setLoginIp(getIpAddr(request));
		// 登录
		result = tdsUserLoginFeignService.login(tdsUserDomain);
		// 登录失败
		if (result.getResultCode().equals(ResultCode.RESULT_FAILED)) {
			return new BackResult<>(ResultCode.RESULT_FAILED, result.getResultMsg());
		}
		// 生成tonke
		String tokenUserPhone = MD5Util.getInstance().getMD5Code("tds_user_token_" + result.getResultObj().getPhone());
		// 根据电话保存对象redis
		this.getUserInfo(result.getResultObj().getPhone());

		// 清空 se_ken_
		// redisClinet.remove("tdsse_ken_" + mobile);//
		redisClinet.set("tds_user_token_" + result.getResultObj().getPhone(), tokenUserPhone);
		result.getResultObj().setToken(tokenUserPhone);// 保存tonke
		//logger.info("=========用户名：" + name + "用户登录============");
		return result;
	}

	/**
	 * 模块加载
	 * 
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/moduleLoadingByUsreId")
	public BackResult<List<TdsModularDomain>> moduleLoadingByUsreId(Integer userId, HttpServletRequest request,
			HttpServletResponse response, String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<List<TdsModularDomain>> result = new BackResult<List<TdsModularDomain>>();

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
		result = tdsUserLoginFeignService.moduleLoadingByUsreId(userId);

		// TODO moduleLoadingByUsreId==redis保存

		// end
		logger.info("用户id:" + userId + "模块加载成功");
		return result;

	}

	@RequestMapping(value = "/loadingByUsreIdRole", method = RequestMethod.POST)
	public BackResult<List<TdsFunctionDomain>> loadingByUsreIdRole(Integer userId, HttpServletRequest request,
			HttpServletResponse response, String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<List<TdsFunctionDomain>> result = new BackResult<List<TdsFunctionDomain>>();

		if (CommonUtils.isNotIngeter(userId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户userId不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsUserLoginFeignService.loadingByUsreIdRole(userId);
		return result;
	}

	@RequestMapping("/signOut")
	public BackResult<Boolean> signOut(String mobile, String token, HttpServletRequest request,
			HttpServletResponse response) {
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

		redisClinet.remove("tds_user_token_" + mobile); // 清空token
		result.setResultObj(true);
		return result;
	}

}
