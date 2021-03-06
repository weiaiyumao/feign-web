package cn.feignclient.credit_feign_web.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.ApiLogPageDomain;
import main.java.cn.domain.MobileInfoDomain;
import main.java.cn.domain.MobileTestLogDomain;
import main.java.cn.domain.page.PageDomain;

@FeignClient(value = "credit-provider-service",fallback = ApiMobileTestServiceHiHystric.class)
public interface ApiMobileTestService {
	
	@RequestMapping(value = "/apiMobileTest/findByMobileNumbers", method = RequestMethod.POST)
	public BackResult<List<MobileInfoDomain>> findByMobileNumbers(@RequestParam(value = "mobileNumbers")String mobileNumbers,@RequestParam(value = "userId")String userId);

	@RequestMapping(value = "/apiMobileTest/getPageByUserId",  method = RequestMethod.POST)
	public BackResult<PageDomain<MobileTestLogDomain>> getPageByUserId(@RequestParam(value = "pageNo")int pageNo, @RequestParam(value = "pageSize")int pageSize, @RequestParam(value = "userId")String userId, @RequestParam(value = "type")String type);
	
	@RequestMapping(value = "/apiMobileTest/getPageByCustomerId",  method = RequestMethod.POST)
	public BackResult<PageDomain<ApiLogPageDomain>> getPageByCustomerId(@RequestParam(value = "pageNo")int pageNo, @RequestParam(value = "pageSize")int pageSize, @RequestParam(value = "customerId")String customerId, @RequestParam(value = "method")String method);
	
	@RequestMapping(value = "/apiMobileTest/findByMobile", method = RequestMethod.POST)
	public BackResult<MobileInfoDomain> findByMobile(@RequestParam(value = "mobile")String mobile,@RequestParam(value = "userId")String userId);
	
	@RequestMapping(value = "/apiMobileTest/findByMobileToAmi", method = RequestMethod.POST)
	public BackResult<MobileInfoDomain> findByMobileToAmi(@RequestParam(value = "mobile")String mobile,@RequestParam(value = "userId")String userId,@RequestParam(value = "method")String method);
}
