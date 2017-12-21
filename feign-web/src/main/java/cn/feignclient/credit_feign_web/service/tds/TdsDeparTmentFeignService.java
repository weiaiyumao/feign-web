package cn.feignclient.credit_feign_web.service.tds;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageAuto;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsDepartmentDomain;
import main.java.cn.domain.tds.TdsFunctionDomain;
import main.java.cn.domain.tds.UserRoleDepartmentViewDomain;

@FeignClient(value = "user-provider-service")
public interface TdsDeparTmentFeignService {

	@RequestMapping(value = "/super/pageUserRoleDepartmentView", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<UserRoleDepartmentViewDomain>> pageUserRoleDepartmentView(PageAuto auto);

	@RequestMapping(value = "/super/funByUserId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsFunctionDomain>> funByuserId(@RequestParam("userId") Integer userId);

	@RequestMapping(value = "/super/selectAll", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsDepartmentDomain>> selectAll(TdsDepartmentDomain domain);

	@RequestMapping(value = "/super/addUserConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> addUserConfig(@RequestParam("name") String name, @RequestParam("passWord") String passWord,
			@RequestParam("phone") String phone, @RequestParam("departmentId") Integer departmentId,
			@RequestParam("positionId") Integer positionId, @RequestParam("comId") Integer comId,
			@RequestParam("arrRoles") Integer[] arrRoles, @RequestParam("loginUserId") Integer loginUserId);

	@RequestMapping(value = "/super/addCustomPermissions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> addCustomPermissions(@RequestParam("soleName") String soleName,
			@RequestParam("loginUserId") Integer loginUserId, @RequestParam("arrfuns") Integer[] arrfuns);
	
	@RequestMapping(value = "/super/addModularFun", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> addModularFun(@RequestBody TdsFunctionDomain domain,@RequestParam("parentId")Integer parentId);
	
	
	
}
