package cn.feignclient.credit_feign_web.service.tds;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.feignclient.credit_feign_web.service.tds.hihystric.TdsMoneyApprovalBackFeignServiceHiHystric;
import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsMoneyApprovalBackDomain;

@FeignClient(value = "user-provider-service", fallback = TdsMoneyApprovalBackFeignServiceHiHystric.class)
public interface TdsMoneyApprovalBackFeignService {
	
	 
	  /**
	   * 退款审核分页列表
	   * @param id
	   * @return  obj
	   */
	@RequestMapping(value="/moneyApprovalBack/pageApprovalBack",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsMoneyApprovalBackDomain>> pageApprovalBack(TdsMoneyApprovalBackDomain domain);
	
	
}
