package cn.feignclient.credit_feign_web.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.CvsFilePathDomain;
import main.java.cn.domain.RunTestDomian;

@FeignClient(value = "credit-provider-service")
public interface CreditProviderService {
	
	@RequestMapping(value = "/credit/runTheTest", method = RequestMethod.POST)
	BackResult<RunTestDomian> runTheTest(@RequestParam(value = "fileUrl")String fileUrl,@RequestParam(value = "userId")String userId,@RequestParam(value = "timestamp")String timestamp);
	
	@RequestMapping(value = "/credit/findByUserId", method = RequestMethod.POST)
	BackResult<List<CvsFilePathDomain>> findByUserId(@RequestParam(value = "userId")String userId);
	
	@RequestMapping(value = "/credit/deleteCvsByIds", method = RequestMethod.POST)
	public BackResult<Boolean> deleteCvsByIds(@RequestParam(value = "ids")String ids,@RequestParam(value = "userId")String userId);
}
