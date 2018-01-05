package cn.feignclient.credit_feign_web.service.tds;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.feignclient.credit_feign_web.service.tds.hihystric.TdsMoneyApprovalFeignServiceHiHystric;
import main.java.cn.common.BackResult;
import main.java.cn.domain.tds.TdsMoneyApprovalDomain;

@FeignClient(value = "user-provider-service",fallback =TdsMoneyApprovalFeignServiceHiHystric.class)
public interface TdsMoneyApprovalFeignService {
	
	
	@RequestMapping(value = "/moneyApproval/downAddOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> downAddOrder(TdsMoneyApprovalDomain domain);

	
}
