package cn.feignclient.credit_feign_web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.cn.common.BackResult;
import main.java.cn.domain.CreUserDomain;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{
	
	@RequestMapping("/findbyMobile")
	public BackResult<CreUserDomain> findbyMobile(HttpServletRequest request, HttpServletResponse response,String mobile) {
		
		 response.setHeader("Access-Control-Allow-Origin", "*");   // 有效，前端可以访问
         response.setContentType("text/json;charset=UTF-8");
		
		Assert.notNull(mobile, "The param mobile not be null!");
		
		BackResult<CreUserDomain> result = userFeignService.findbyMobile(mobile);
		
		return result;
	}
}
