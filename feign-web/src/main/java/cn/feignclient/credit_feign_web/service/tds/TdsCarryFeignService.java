package cn.feignclient.credit_feign_web.service.tds;



import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.feignclient.credit_feign_web.service.tds.hihystric.TdsMoneyApprovalCarryFeignServiceHiHystric;
import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsCarryDomain;

@FeignClient(value = "user-provider-service", fallback = TdsMoneyApprovalCarryFeignServiceHiHystric.class)
public interface TdsCarryFeignService {
	
	
	@RequestMapping(value = "/carry/pageTdsCarry", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public  BackResult<PageDomain<TdsCarryDomain>> pageTdsCarry(TdsCarryDomain domain);
	
	
	@RequestMapping(value = "/carry/getCarryByUserId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Map<String,Object>> getCarryByUserId(@RequestParam("userId") Integer userId);
	
	
	@RequestMapping(value = "/carry/getSubCarry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> getSubCarry(TdsCarryDomain domain,@RequestParam("type") String type);
	
	
	
	
	
}

