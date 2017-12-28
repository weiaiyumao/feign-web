package cn.feignclient.credit_feign_web.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.CreUserDomain;

@FeignClient(value = "user-provider-service",fallback = UserFeignServiceHiHystric.class)
public interface UserFeignService {
	
	@RequestMapping(value = "/user/findbyMobile", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	BackResult<CreUserDomain> findbyMobile(@RequestParam("mobile")String mobile);
	
	@RequestMapping(value = "/user/findOrsaveUser", method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> findOrsaveUser(@RequestBody CreUserDomain creUserDomain);
	
	@RequestMapping(value = "/user/updateCreUser", method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> updateCreUser(@RequestBody CreUserDomain creUserDomain);
	
	@RequestMapping(value = "/user/updateCreUserEmail", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> updateCreUser(@RequestParam("userPhone")String userPhone, @RequestParam("email")String email);
	
	@RequestMapping(value = "/user/activateUser", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> activateUser(@RequestBody CreUserDomain creUserDomain);
	
	@RequestMapping(value = "/user/activateUserZzt", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> activateUserZzt(@RequestBody CreUserDomain creUserDomain);
}
