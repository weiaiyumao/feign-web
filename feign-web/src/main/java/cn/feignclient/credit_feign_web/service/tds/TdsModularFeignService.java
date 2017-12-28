package cn.feignclient.credit_feign_web.service.tds;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.tds.TdsModularDomain;

@FeignClient(value = "user-provider-service")
public interface TdsModularFeignService {
	
	@RequestMapping(value = "/modular/loadById", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsModularDomain> loadById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/modular/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> save(TdsModularDomain tdsModularDomain);
	
	@RequestMapping(value="/modular/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsModularDomain>  update(TdsModularDomain tdsModularDomain);
	
	@RequestMapping(value="/modular/deleteById",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/modular/selectAll",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsModularDomain>> selectAll(TdsModularDomain tdsModularDomain);
}
