package cn.feignclient.credit_feign_web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import cn.feignclient.credit_feign_web.redis.RedisClient;
import cn.feignclient.credit_feign_web.service.UserFeignService;
import cn.feignclient.credit_feign_web.thread.ThreadExecutorService;
import main.java.cn.common.BackResult;
import main.java.cn.common.RedisKeys;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;
import main.java.cn.domain.tds.TdsFunctionDomain;
import main.java.cn.domain.tds.TdsUserDomain;
import main.java.cn.hhtp.util.MD5Util;

public class BaseController {
	
	@Autowired
	protected UserFeignService userFeignService;

	@Autowired
	protected RedisClient redisClinet;

	@Autowired
	protected ThreadExecutorService threadExecutorService;
	
	@Value("${api_key}")
	protected String apiKey;

	@Autowired
	private RedisTemplate<String, CreUserDomain> redisTemplate;
	
	
	@Autowired
	private RedisTemplate<String,TdsUserDomain> redisTemplateTds;
	
	
	@Autowired
	private RedisTemplate<String,List<TdsFunctionDomain>> redisTemplateTdList;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 检查是否登录
	 * 
	 * @param mobile
	 * @param token
	 * @return
	 */
	public Boolean isLogin(String mobile, String token) {
		String redisToken = redisClinet.get("user_token_" + mobile);

		if (null == redisToken || "".equals(redisToken)) {
			return false;
		}

		redisClinet.set("user_token_" + mobile, redisToken);

		return redisToken.equals(token) ? true : false;
	}

	/**
	 * 获取请求的真实ＩＰ地址
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 自助通的请求验证签名
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Boolean checkSign(HttpServletRequest request) {

		Enumeration paramNames = request.getParameterNames();
		Map map = new HashMap();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					map.put(paramName, paramValue);
				}
			}
		}

		if (map == null || map.size() <= 0) {
			return Boolean.FALSE;
		}

		if (map.get("timestamp") == null || map.get("timestamp").equals("null") || map.get("timestamp").equals("")) {
			return Boolean.FALSE;
		}

		if (map.get("token") == null || map.get("token").equals("null") || map.get("token").equals("")) {
			return Boolean.FALSE;
		}

		String timestamp = map.get("timestamp").toString();
		String token = map.get("token").toString();

		String md5Token = MD5Util.getInstance().getMD5Code(timestamp + apiKey);

		if (!md5Token.equals(token)) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	
	/**
	 * 根据手机号码获取用户对象 (缓存30分钟)
	 * 
	 * @param mobile
	 * @return
	 */
	protected CreUserDomain findByMobile(String mobile) {

		CreUserDomain creuserdomain = new CreUserDomain();
		String skey = RedisKeys.getInstance().getSessUserInfo(mobile);
		creuserdomain = redisTemplate.opsForValue().get(skey);

		if (null == creuserdomain) {
			BackResult<CreUserDomain> result = userFeignService.findbyMobile(mobile);

			if (result.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				creuserdomain = result.getResultObj();
				redisTemplate.opsForValue().set(skey, creuserdomain, 30 * 60, TimeUnit.SECONDS);
			}
		} 
		return creuserdomain;
	}
	
	
	
//	/**
//	 * 根据手机号码获取用户对象 (缓存30分钟)
//	 * @param mobile
//	 * @return tds
//	 */
//	protected TdsUserDomain getUserModuRole(Integer userId) {
//
//		List<TdsFunctionDomain> tdsFunctionDomain = new ArrayList<TdsFunctionDomain>();
//		String skey = RedisKeys.getInstance().getUserInfokey(userId);
//		tdsFunctionDomain= redisTemplateTdList.opsForValue().get(skey);
//
//		if (null == tdsFunctionDomain) {
//			BackResult<List<TdsFunctionDomain>> result = tdsUserLoginFeignService.moduleLoadingByUsreId(userId);
//
//			if (result.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
//				tdsFunctionDomain = result.getResultObj();
//				redisTemplateTds.opsForValue().set(skey, tdsFunctionDomain, 30 * 60, TimeUnit.SECONDS);
//			}
//		} 
//		return tdsFunctionDomain;
//	}
	
	
	
	

}
