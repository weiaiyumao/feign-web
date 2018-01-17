package cn.feignclient.credit_feign_web.controller.tds;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import cn.feignclient.credit_feign_web.redis.RedisClient;
import cn.feignclient.credit_feign_web.service.UserFeignService;
import cn.feignclient.credit_feign_web.service.tds.TdsUserFeignService;
import main.java.cn.common.BackResult;
import main.java.cn.common.RedisKeys;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;
import main.java.cn.domain.tds.TdsUserDomain;

public class TdsBaseController {
	
	@Autowired
	protected RedisClient redisClinet;

	@Autowired
	protected TdsUserFeignService tdsUserFeignService;
	
	@Autowired
	protected UserFeignService userFeignService;
	
	@Autowired
	private RedisTemplate<String,TdsUserDomain> redisTemplateTds;
	
	@Autowired
	private RedisTemplate<String, CreUserDomain> redisTemplate;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
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
	 * 根据手机号码获取用户对象 (缓存30分钟)
	 * @param mobile
	 * @return tds
	 */
	protected TdsUserDomain getUserInfo(String mobile) {

		TdsUserDomain tdsUserDomain = new TdsUserDomain();
		String skey = RedisKeys.getInstance().getUserInfokey(mobile);
		tdsUserDomain = redisTemplateTds.opsForValue().get(skey);

		if (null == tdsUserDomain) {
			BackResult<TdsUserDomain> result = tdsUserFeignService.loadByPhone(mobile);

			if (result.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				tdsUserDomain = result.getResultObj();
				redisTemplateTds.opsForValue().set(skey, tdsUserDomain, 30 * 60, TimeUnit.SECONDS);
			}
		} 
		return tdsUserDomain;
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
	
	
	
	/**
	 * 根据手机号码重新覆盖对象信息
	 */
	protected void replaceAndSaveObj(String mobile){
		BackResult<TdsUserDomain> result = tdsUserFeignService.loadByPhone(mobile);
		String skey = RedisKeys.getInstance().getUserInfokey(mobile);
		redisTemplateTds.opsForValue().set(skey,result.getResultObj(),30 * 60, TimeUnit.SECONDS);
	 
	}
	
}
