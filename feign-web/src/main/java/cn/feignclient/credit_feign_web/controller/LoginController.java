package cn.feignclient.credit_feign_web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.feignclient.credit_feign_web.domain.JyDomain;
import cn.feignclient.credit_feign_web.domain.TokenUserDomain;
import cn.feignclient.credit_feign_web.jysdk.GeetestConfig;
import cn.feignclient.credit_feign_web.jysdk.GeetestLib;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import cn.feignclient.credit_feign_web.utils.MD5Util;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;
import main.java.cn.sms.util.ChuangLanSmsUtil;

@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(ApiAccountInfoController.class);
	
	/**
	 * 加载极验 验证码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/initjyCode")
	public void initjyCode(HttpServletRequest request, HttpServletResponse response, String mobile) throws IOException {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<JyDomain> result = new BackResult<JyDomain>();

		GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
				GeetestConfig.isnewfailback());

		// 自定义参数,可选择添加
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("user_id", mobile); // 网站用户id
		param.put("client_type", "web"); // web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
		param.put("ip_address", super.getIpAddr(request)); // 传输用户请求验证时所携带的IP

		// 进行验证预处理
		int gtServerStatus = gtSdk.preProcess(param);

		// 将服务器状态设置到session中
		redisClinet.set(gtSdk.gtServerStatusSessionKey + "_" + mobile, String.valueOf(gtServerStatus));
		// 将userid设置到session中
		redisClinet.set(gtSdk.gtServerStatusSessionKey + "_user_id_" + mobile, mobile);
		String str = gtSdk.getResponseStr();

		JSONObject json = JSONObject.parseObject(str);
		String resStr = gtSdk.getResponseStr();
		JyDomain jydomain = new JyDomain();
		jydomain.setChallenge(json.getString("challenge"));
		jydomain.setSuccess(json.getString("success"));
		jydomain.setGt(json.getString("gt"));
		result.setResultObj(jydomain);
		PrintWriter out = response.getWriter();
		out.println(resStr);
	}

	/**
	 * 加载极验 验证码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/verifyTyCode")
	public void verifyTyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(), 
				GeetestConfig.isnewfailback());
			
		String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
		String validate = request.getParameter(GeetestLib.fn_geetest_validate);
		String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);
		String mobile = request.getParameter("mobile");
		//从session中获取gt-server状态
		int gt_server_status_code = Integer.valueOf(redisClinet.get(gtSdk.gtServerStatusSessionKey + "_" + mobile));
		
		//从session中获取userid
		String userid = redisClinet.get(gtSdk.gtServerStatusSessionKey + "_user_id_" + mobile);
		
		//自定义参数,可选择添加
		HashMap<String, String> param = new HashMap<String, String>(); 
		param.put("user_id", userid); //网站用户id
		param.put("client_type", "web"); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
		param.put("ip_address", super.getIpAddr(request)); //传输用户请求验证时所携带的IP
		
		int gtResult = 0;

		if (gt_server_status_code == 1) {
			//gt-server正常，向gt-server进行二次验证
			gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
		} else {
			// gt-server非正常情况下，进行failback模式验证
			gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
		}


		if (gtResult == 1) {
			
			int code = (int) ((Math.random() * 9 + 1) * 100000);

			ChuangLanSmsUtil.getInstance().sendSmsByMobile(mobile, String.valueOf(code));

//			System.out.println(mobile+"验证码：" + String.valueOf(code));
			logger.info(mobile+"验证码：" + String.valueOf(code));

			redisClinet.set("se_ken_" + mobile, String.valueOf(code));
			
			// 验证成功
			PrintWriter out = response.getWriter();
			JSONObject data = new JSONObject();
			data.put("status", "success");
			data.put("version", gtSdk.getVersionInfo());
			out.println(data.toString());
		}
		else {
			// 验证失败
			JSONObject data = new JSONObject();
			data.put("status", "fail");
			data.put("version", gtSdk.getVersionInfo());
			PrintWriter out = response.getWriter();
			out.println(data.toString());
		}
	}

	/**
	 * 发送手机号码
	 * 
	 * @param request
	 * @param response
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/sendSms")
	public BackResult<Boolean> sendSms(HttpServletRequest request, HttpServletResponse response, String mobile) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<Boolean> result = new BackResult<Boolean>();

		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
		}

		return result;
	}

	/**
	 * 登录
	 * 
	 * @param request
	 * @param response
	 * @param mobile
	 * @param code
	 * @return
	 */
	@RequestMapping("/userLogin")
	public BackResult<TokenUserDomain> userLogin(HttpServletRequest request, HttpServletResponse response,
			String mobile, String code) {

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
		creUserDomain.setLastLoginIp(super.getIpAddr(request));
		// 查询用户
		BackResult<CreUserDomain> creResult = userFeignService.findOrsaveUser(creUserDomain);

		if (!creResult.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
			result.setResultMsg(creResult.getResultMsg());
			result.setResultCode(creResult.getResultCode());
			return result;
		}

		userMd5str = MD5Util.getInstance().getMD5Code("user_token_" + creResult.getResultObj().getId());

		// 清空 se_ken_
		redisClinet.remove("se_ken_" + mobile);

		redisClinet.set("user_token_" + mobile, userMd5str);

		TokenUserDomain user = new TokenUserDomain();
		user.setToken(userMd5str);
		result.setResultObj(user);

		return result;
	}

	/**
	 * erp调用激活账户
	 * 
	 * @param request
	 * @param response
	 * @param mobile
	 * @param code
	 * @return
	 */
	@RequestMapping("/api/activateUser")
	public BackResult<String> activateUser(HttpServletRequest request, HttpServletResponse response, String mobile,
			String timestamp, String token) {

		logger.info("自助通请求用户【" + mobile +"】，请求激活账户！token:"+token + "timestamp:" + timestamp);
		
		BackResult<String> result = new BackResult<String>();

		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			result.setResultObj(null);
			return result;
		}

		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			result.setResultObj(null);
			return result;
		}

		String md5Token = MD5Util.getInstance().getMD5Code(timestamp + apiKey);

		if (!md5Token.equals(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("签名验证失败");
			result.setResultObj(null);
			return result;
		}

		CreUserDomain creUserDomain = new CreUserDomain();
		creUserDomain.setUserPhone(mobile);

		// 查询用户
		BackResult<CreUserDomain> creResult = userFeignService.activateUser(creUserDomain);

		if (!creResult.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
			result.setResultMsg(creResult.getResultMsg());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultObj(null);
			return result;
		}

		result.setResultObj(creResult.getResultObj().getId().toString());

		return result;
	}

	/**
	 * 登出
	 * 
	 * @param request
	 * @param response
	 * @param mobile
	 * @param token
	 * @return
	 */
	@RequestMapping("/logout")
	public BackResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response, String mobile,
			String token) {

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
		redisClinet.remove("user_token_" + mobile);
		result.setResultObj(true);

		return result;
	}

	/**
	 * 检测是否已经登出
	 * 
	 * @param request
	 * @param response
	 * @param mobile
	 * @param token
	 * @return
	 */
	@RequestMapping("/isLogout")
	public BackResult<Boolean> isLogout(HttpServletRequest request, HttpServletResponse response, String mobile,
			String token) {

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
