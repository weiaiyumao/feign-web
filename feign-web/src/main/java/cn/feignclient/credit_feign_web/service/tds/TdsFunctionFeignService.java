package cn.feignclient.credit_feign_web.service.tds;


import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.page.BasePageParam;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsFunMoViewDomain;
import main.java.cn.domain.tds.TdsFunctionDomain;

@FeignClient(value = "user-provider-service")
public interface TdsFunctionFeignService {
	
	@RequestMapping(value = "/function/loadByIdView", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsFunMoViewDomain> loadByIdView(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/function/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> save(TdsFunctionDomain tdsFunctionDomain);
	
	@RequestMapping(value="/function/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<Integer>  update(TdsFunctionDomain TdsFunctionDomain);
	
	@RequestMapping(value="/function/deleteById",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/function/pageTdsFunction",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsFunMoViewDomain>> pageTdsFunction(TdsFunMoViewDomain domain);
	
	
    @RequestMapping(value="/function/queryFunction",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<List<TdsFunctionDomain>> queryFunction();
	

	@RequestMapping(value="/function/pageByFunction",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<Map<String,Object>>> pageByFunction(@RequestParam("name")String name,BasePageParam basePageParam);
	

	
}
