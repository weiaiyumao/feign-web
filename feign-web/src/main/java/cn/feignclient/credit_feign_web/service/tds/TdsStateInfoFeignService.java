package cn.feignclient.credit_feign_web.service.tds;


import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageAuto;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsEnumDomain;
import main.java.cn.domain.tds.TdsProductMoneyDomain;
import main.java.cn.domain.tds.TdsStateInfoDomain;


@FeignClient(value = "user-provider-service")
public interface TdsStateInfoFeignService {
   
	
	@RequestMapping(value="/state/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<Integer>  update(TdsStateInfoDomain tdsStateInfoDomain);
	
	@RequestMapping(value="/state/deleteById",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id);
	
	
	@RequestMapping(value = "/state/pageTdsStateInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsStateInfoDomain>> pageTdsStateInfo(PageAuto auto);
	
	
	
	@RequestMapping(value = "/state/save", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> save(TdsStateInfoDomain tdsStateInfoDomain);
	
	
	@RequestMapping(value = "/state/loadById", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsStateInfoDomain> loadById(@RequestParam("id")Integer id);
	
	
	@RequestMapping(value = "/state/addProductTable", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> addProductTable(TdsProductMoneyDomain domain);
	
	@RequestMapping(value = "/state/queryByTypeCode", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsEnumDomain>> queryByTypeCode(@RequestParam("codeName")String codeName);
}
