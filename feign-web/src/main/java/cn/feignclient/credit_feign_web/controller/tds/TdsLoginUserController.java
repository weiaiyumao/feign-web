package cn.feignclient.credit_feign_web.controller.tds;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.tds.TdsUserLoginFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsFunctionDomain;
import main.java.cn.domain.tds.TdsUserDomain;
import main.java.cn.hhtp.util.MD5Util;

@RestController
@SuppressWarnings("unchecked")
@RequestMapping("/userLogin")
public class TdsLoginUserController extends BaseTdsController {

	//private final static Logger logger = LoggerFactory.getLogger(TdsLoginUserController.class);

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
			return BackResult.error("用户账号不能为空");
		}

		if (CommonUtils.isNotString(passWord)) {
			return BackResult.error("密码不能为空");
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
			return BackResult.error(result.getResultMsg());
		}
		// 生成tonke
		String tokenUserPhone = MD5Util.getInstance().getMD5Code("tds_user_token_" + result.getResultObj().getPhone());
		
		// 根据电话保存对象redis
		this.getUserInfo(result.getResultObj().getPhone());

		// 清空 se_ken_
		// redisClinet.remove("tdsse_ken_" + mobile);//
		redisClinet.set("tds_user_token_" + result.getResultObj().getPhone(), tokenUserPhone);
		result.getResultObj().setToken(tokenUserPhone);// 保存tonke
		return result;
	}

     	
	/**
	 * 菜单渲染
	 * @param userId
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/loadingByUsreIdEnums", method = RequestMethod.POST)
	public BackResult<List<TdsFunctionDomain>> loadingByUsreIdRole(Integer userId, HttpServletRequest request,
			HttpServletResponse response, String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		if (CommonUtils.isNotIngeter(userId)) {
			return BackResult.error("用户id不能为空");
		}
		
		if (CommonUtils.isNotString(token)) {
			return BackResult.error("token不能为空");
		}
		
		//TODO  reids 保存
		
		return tdsUserLoginFeignService.loadingByUsreIdRole(userId);
	}

	
	
	@RequestMapping("/signOut")
	public BackResult<Boolean> signOut(String mobile, String token, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		if (CommonUtils.isNotString(mobile)) {
			return BackResult.error("手机号码不能为空");
		}

		if (CommonUtils.isNotString(token)) {
			return BackResult.error("token不能为空");
		}

		redisClinet.remove("tds_user_token_" + mobile); // 清空token
		
		return BackResult.ok(true);
	}

}
