package cn.feignclient.credit_feign_web.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.CreUserDomain;

@FeignClient(value = "user-provider-service")
public interface UserFeignService {
	
	@RequestMapping(value = "/user/findbyMobile", method = RequestMethod.GET)
	BackResult<CreUserDomain> findbyMobile(@RequestParam("mobile")String mobile);
	
	@RequestMapping(value = "/user/findOrsaveUser", method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> findOrsaveUser(@RequestBody CreUserDomain creUserDomain);
	
	@RequestMapping(value = "/user/updateCreUser", method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> updateCreUser(@RequestBody CreUserDomain creUserDomain);
	
}
