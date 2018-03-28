package cn.feignclient.credit_feign_web.service.tds;



import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsAccountBankDomain;

@FeignClient(value = "user-provider-service")
public interface TdsAccounBankFeignService {

	@RequestMapping(value = "/accounBank/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> save(TdsAccountBankDomain tdsAccountBankDomain);

	@RequestMapping(value = "/accounBank/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> update(TdsAccountBankDomain tdsAccountBankDomain);

	@RequestMapping(value = "/accounBank/isDisableById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> isDisableById(@RequestParam("id") Integer id);

	@RequestMapping(value = "/accounBank/pageTdsAccountBank", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsAccountBankDomain>> pageTdsAccountBank(TdsAccountBankDomain tdsAccountBankDomain);
	
	
	@RequestMapping(value = "/accounBank/loadById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsAccountBankDomain> loadById(@RequestParam("id")Integer id);
	
	@RequestMapping(value = "/accounBank/selectAllBankName",consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<Map<String, Object>>> selectAllBankName();

}
