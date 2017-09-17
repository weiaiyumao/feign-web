package cn.feignclient.credit_feign_web;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.domain.BackResult;
import main.java.cn.domain.CvsFilePathDomain;

@FeignClient(value = "credit-provider-service")
public interface CreditProviderService {
	
	@RequestMapping(value = "/hi", method = RequestMethod.GET)
	String sayHiFromClientOne(@RequestParam(value = "name") String name);
	
	@RequestMapping(value = "/runTheTest", method = RequestMethod.GET)
	BackResult<CvsFilePathDomain> runTheTest(@RequestParam(value = "fileUrl")String fileUrl,@RequestParam(value = "userId")String userId);
}
