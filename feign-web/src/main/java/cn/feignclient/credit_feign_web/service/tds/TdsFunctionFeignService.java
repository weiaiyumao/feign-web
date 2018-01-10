package cn.feignclient.credit_feign_web.service.tds;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.tds.TdsFunctionDomain;

@FeignClient(value = "user-provider-service")
public interface TdsFunctionFeignService {
	
	@RequestMapping(value = "/function/loadById", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsFunctionDomain> loadById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/function/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsFunctionDomain> save(TdsFunctionDomain tdsFunctionDomain);
	
	@RequestMapping(value="/function/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsFunctionDomain>  update(TdsFunctionDomain TdsFunctionDomain);
	
	@RequestMapping(value="/function/deleteById",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/function/selectAll",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsFunctionDomain>> selectAll(TdsFunctionDomain tdsFunctionDomain);
}
