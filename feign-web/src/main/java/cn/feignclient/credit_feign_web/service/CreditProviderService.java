package cn.feignclient.credit_feign_web.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.CvsFilePathDomain;
import main.java.cn.domain.RunTestDomian;
import main.java.cn.domain.page.PageDomain;
					 
@FeignClient(value = "credit-provider-service")
public interface CreditProviderService {
	
	@RequestMapping(value = "/credit/runTheTest", method = RequestMethod.GET)
	BackResult<RunTestDomian> runTheTest(@RequestParam(value = "fileUrl")String fileUrl,@RequestParam(value = "userId")String userId,@RequestParam(value = "timestamp")String timestamp,@RequestParam(value = "mobile")String mobile);
	
	@RequestMapping(value = "/credit/findByUserId", method = RequestMethod.GET)
	BackResult<List<CvsFilePathDomain>> findByUserId(@RequestParam(value = "userId")String userId);
	
	@RequestMapping(value = "/credit/deleteCvsByIds", method = RequestMethod.GET)
	public BackResult<Boolean> deleteCvsByIds(@RequestParam(value = "ids")String ids,@RequestParam(value = "userId")String userId);
	
	
	@RequestMapping(value = "/credit/getCVSPageByUserId", method = RequestMethod.POST)
	public BackResult<PageDomain<CvsFilePathDomain>> getPageByUserId(@RequestParam(value = "pageNo")int pageNo,@RequestParam(value = "pageSize")int pageSize,@RequestParam(value = "userId")String userId);
	
	@RequestMapping(value = "/credit/theTest", method = RequestMethod.POST)
	public BackResult<RunTestDomian> theTest(@RequestParam(value = "fileUrl")String fileUrl,@RequestParam(value = "userId")String userId, @RequestParam(value = "source")String source,@RequestParam(value = "mobile")String mobile,@RequestParam(value = "startLine")String startLine,@RequestParam(value = "type")String type);
}
