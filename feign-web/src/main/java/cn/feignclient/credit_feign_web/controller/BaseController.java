package cn.feignclient.credit_feign_web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import cn.feignclient.credit_feign_web.redis.RedisClient;
import cn.feignclient.credit_feign_web.service.UserFeignService;

public class BaseController {
	
	@Autowired
    protected UserFeignService userFeignService;
	
	@Autowired  
    protected RedisClient redisClinet;  
	
	@Value("${api_key}")
	protected String apiKey;
	
	@InitBinder  
    protected  void initBinder(WebDataBinder binder) {  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
    }

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
