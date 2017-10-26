package cn.feignclient.credit_feign_web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import cn.feignclient.credit_feign_web.redis.RedisClient;
import cn.feignclient.credit_feign_web.service.UserFeignService;

public class BaseController {
	
	@Autowired
    protected UserFeignService userFeignService;
	
	@Autowired  
    protected RedisClient redisClinet;  

	/**
	 * 检查是否登录
	 * @param mobile
	 * @param token
	 * @return
	 */
	public Boolean isLogin(String mobile,String token){
		String redisToken = redisClinet.get("user_token_" + mobile);
		
		if (null == redisToken || "".equals(redisToken)) {
			return false;
		}
		
		redisClinet.set("user_token_" + mobile, redisToken);
		
		return redisToken.equals(token) ? true : false;
	}

	/**
	 * 获取请求的真实ＩＰ地址
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {     
	      String ip = request.getHeader("x-forwarded-for");     
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("Proxy-Client-IP");     
	     }     
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("WL-Proxy-Client-IP");     
	      }     
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	          ip = request.getRemoteAddr();     
	     }     
	     return ip;     
	}    
}
