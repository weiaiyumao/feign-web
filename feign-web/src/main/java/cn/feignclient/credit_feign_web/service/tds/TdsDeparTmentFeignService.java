package cn.feignclient.credit_feign_web.service.tds;


import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsDepartmentDomain;
import main.java.cn.domain.tds.TdsFunctionDomain;
import main.java.cn.domain.tds.UserRoleDepartmentViewDomain;

@FeignClient(value = "user-provider-service")
public interface TdsDeparTmentFeignService {

	@RequestMapping(value = "/super/pageUserRoleDepartmentView", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<UserRoleDepartmentViewDomain>> pageUserRoleDepartmentView(
			@RequestParam("name") String departName, @RequestParam("roleName") String role_name,
			@RequestParam("createTime") String create_time, @RequestParam("contact") String contact,
			@RequestParam("currentPage") Integer currentPage, @RequestParam("numPerPage") Integer numPerPage);
	
	
 	 @RequestMapping(value="/super/funByUserId",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
     public BackResult<List<TdsFunctionDomain>> funByuserId(@RequestParam("usreId")Integer usreId);
	
     
 	 
 	  @RequestMapping(value="/super/selectAll",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	  public BackResult<List<TdsDepartmentDomain>> selectAll(TdsDepartmentDomain domain);

}
