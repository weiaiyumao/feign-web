package cn.feignclient.credit_feign_web.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.ErpTradeDomain;
import main.java.cn.domain.TrdOrderDomain;
import main.java.cn.domain.UserAccountDomain;
import main.java.cn.domain.page.PageDomain;

@FeignClient(value = "user-provider-service")
public interface UserAccountFeignService {
	
	@RequestMapping(value = "/userAccount/findbyMobile", method = RequestMethod.GET)
	BackResult<UserAccountDomain> findbyMobile(@RequestParam("mobile")String mobile);
	
	@RequestMapping(value = "/userAccount/rechargeOrRefunds", method = RequestMethod.GET)
	BackResult<ErpTradeDomain> rechargeOrRefunds(@RequestBody TrdOrderDomain trdOrderDomain);
	
	@RequestMapping(value = "/userAccount/findTrdOrderByCreUserId", method = RequestMethod.GET)
	BackResult<List<TrdOrderDomain>> findTrdOrderByCreUserId(@RequestParam("creUserId")Integer creUserId);
	
	@RequestMapping(value = "/userAccount/consumeApiAccount" , method = RequestMethod.POST)
	public BackResult<Boolean> consumeApiAccount(@RequestParam("creUserId")String creUserId,@RequestParam("count")String count);
	
	@RequestMapping(value = "/userAccount/pageFindTrdOrderByCreUserId", method = RequestMethod.POST)
	public BackResult<PageDomain<TrdOrderDomain>> pageFindTrdOrderByCreUserId(@RequestParam("creUserId")Integer creUserId,@RequestParam("pageSize")Integer pageSize,@RequestParam("pageNum")Integer pageNum);
}
