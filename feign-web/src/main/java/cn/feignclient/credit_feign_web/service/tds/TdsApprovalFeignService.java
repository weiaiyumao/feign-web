package cn.feignclient.credit_feign_web.service.tds;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageAuto;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsCustomerViewDomain;

@FeignClient(value = "user-provider-service")
public interface TdsApprovalFeignService {
	
	

    
	@RequestMapping(value = "/approval/pageTdsApproval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsCustomerViewDomain>> pageTdsApproval(PageAuto auto);
	
	
	
	
	@RequestMapping(value = "/approval/isAgree", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> isAgree(@RequestParam("isAgree")Integer isAgree,@RequestParam("userId")Integer userId,@RequestParam("reas")String reas);

}
