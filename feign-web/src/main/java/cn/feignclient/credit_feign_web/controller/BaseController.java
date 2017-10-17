package cn.feignclient.credit_feign_web.controller;

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

}
