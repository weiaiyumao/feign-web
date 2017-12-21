package cn.feignclient.credit_feign_web.service.tds;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.tds.TdsFunctionDomain;
import main.java.cn.domain.tds.TdsRoleDomain;

@FeignClient(value = "user-provider-service")
public interface TdsRoleFeignService {
	
	@RequestMapping(value = "/role/loadById", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsRoleDomain> loadById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/role/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsRoleDomain> save(TdsRoleDomain tdsRoleDomain);
	
	@RequestMapping(value="/role/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsRoleDomain>  update(TdsRoleDomain tdsRoleDomain);
	
	@RequestMapping(value="/role/deleteById",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/role/selectAll",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsRoleDomain>> selectAll(TdsRoleDomain tdsRoleDomain);
	
	@RequestMapping(value ="/role/queryfunByRoleId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsFunctionDomain>> queryfunByRoleId(@RequestParam("roleId")Integer roleId); 
}
