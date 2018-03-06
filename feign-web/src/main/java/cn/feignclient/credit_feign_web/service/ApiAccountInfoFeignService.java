package cn.feignclient.credit_feign_web.service;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.AccountInfoDomain;
import main.java.cn.domain.ApiAccountInfoDomain;

@FeignClient(value = "user-provider-service",fallback = ApiAccountInfoFeignServiceHiHystric.class)
public interface ApiAccountInfoFeignService {

	@RequestMapping(value = "/apiAccountInfo/findByCreUserId", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<ApiAccountInfoDomain> findByCreUserId(@RequestParam("creUserId")String creUserId);
	
	@RequestMapping(value = "/apiAccountInfo/updateApiAccountInfo", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<ApiAccountInfoDomain> updateApiAccountInfo(@RequestBody ApiAccountInfoDomain domain);
	
	@RequestMapping(value = "/apiAccountInfo/checkApiAccount", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> checkApiAccount(@RequestParam("apiName")String apiName, @RequestParam("password")String password, @RequestParam("ip")String ip, @RequestParam("checkCount")int checkCount);
	
	@RequestMapping(value = "/apiAccountInfo/checkMsAccount", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> checkMsAccount(@RequestParam("apiName")String apiName, @RequestParam("password")String password, @RequestParam("ip")String ip, @RequestParam("checkCount")int checkCount);
	
	@RequestMapping(value = "/apiAccountInfo/checkTcAccount", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<AccountInfoDomain> checkTcAccount(@RequestParam("apiName")String apiName, @RequestParam("password")String password,@RequestParam("method")String method, @RequestParam("ip")String ip);
	
	@RequestMapping(value = "/apiAccountInfo/updateTcAccount", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> updateTcAccount(@RequestBody Map<String,Object> params);
	
	@RequestMapping(value = "/apiAccountInfo/checkRqApiAccount", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> checkRqApiAccount(@RequestParam("apiName")String apiName, @RequestParam("password")String password, @RequestParam("ip")String ip, @RequestParam("checkCount")int checkCount);
}
