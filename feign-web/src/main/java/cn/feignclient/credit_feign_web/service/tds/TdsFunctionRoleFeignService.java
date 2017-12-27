package cn.feignclient.credit_feign_web.service.tds;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.tds.TdsFunctionRoleDomain;

@FeignClient(value = "user-provider-service")
public interface TdsFunctionRoleFeignService {
	
	@RequestMapping(value = "/functionRole/loadById", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsFunctionRoleDomain> loadById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/functionRole/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsFunctionRoleDomain> save(TdsFunctionRoleDomain tdsFunctionRoleDomain);
	
	@RequestMapping(value="/functionRole/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsFunctionRoleDomain>  update(TdsFunctionRoleDomain tdsFunctionRoleDomain);
	
	@RequestMapping(value="/functionRole/deleteById",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/functionRole/selectAll",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsFunctionRoleDomain>> selectAll(TdsFunctionRoleDomain tdsFunctionRoleDomain);
	
}
