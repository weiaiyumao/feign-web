package cn.feignclient.credit_feign_web.service.tds;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	
	/**
	 * 退单
	 * @param domain
	 * @return
	 */
	@RequestMapping(value="/moneyApprovalBack/backOrderMoney",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> backOrderMoney(TdsMoneyApprovalBackDomain domain);
	
	
	
	
	/**
	 * 退单审核状态改变
	 * @param domain
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/moneyApprovalBack/approvalByUpStatusBack", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	BackResult<Integer> approvalByUpStatusBack(TdsMoneyApprovalBackDomain domain,@RequestParam("appRemarks")  String appRemarks);
	
	
}
