package cn.feignclient.credit_feign_web.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.MobileInfoDomain;

@FeignClient(value = "credit-provider-service")
public interface ApiMobileTestService {
	
	@RequestMapping(value = "/apiMobileTest/findByMobileNumbers", method = RequestMethod.POST)
	public BackResult<List<MobileInfoDomain>> findByMobileNumbers(@RequestParam(value = "mobileNumbers")String mobileNumbers);
	
}
