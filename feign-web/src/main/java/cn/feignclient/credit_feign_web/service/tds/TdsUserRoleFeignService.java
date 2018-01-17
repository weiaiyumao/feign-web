package cn.feignclient.credit_feign_web.service.tds;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageAuto;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsUserRoleDomain;

@FeignClient(value = "user-provider-service")
public interface TdsUserRoleFeignService {
	
	@RequestMapping(value = "/userRole/loadById", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsUserRoleDomain> loadById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/userRole/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsUserRoleDomain> save(TdsUserRoleDomain tdsUserRoleDomain);
	
	@RequestMapping(value="/userRole/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BackResult<TdsUserRoleDomain>  update(TdsUserRoleDomain tdsUserRoleDomain);
	
	@RequestMapping(value="/userRole/deleteById",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id);
	
	@RequestMapping(value="/userRole/selectAll",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsUserRoleDomain>> selectAll(TdsUserRoleDomain tdsUserRoleDomain);
	
	@RequestMapping(value="/userRole/upStatusById",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> upStatusById(@RequestParam("id")Integer id,@RequestParam("status")String status);


	@RequestMapping(value="/userRole/queryRoleIsStatus",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<PageAuto>> queryRoleIsStatus(PageAuto auto);
	
	
	@RequestMapping(value="/userRole/queryUserByRoleName",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<Map<String,String>>> queryUserByRoleName(@RequestParam("contact") String contact);
}
