package cn.feignclient.credit_feign_web.service.tds;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsCompanyDomain;
import main.java.cn.domain.tds.TdsUserDomain;

@FeignClient(value = "user-provider-service")
public interface TdsUserFeignService {

	@RequestMapping(value = "/tdsUser/loadById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsUserDomain> loadById(@RequestParam("id") Integer id);

	@RequestMapping(value = "/tdsUser/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> save(TdsUserDomain tdsUserDomain);

	@RequestMapping(value = "/tdsUser/deleteById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> deleteById(@RequestParam("id") Integer id);

	@RequestMapping(value = "/tdsUser/pageSelectAll", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsUserDomain>> pageSelectAll(TdsUserDomain tdsUserDomain,
			@RequestParam("pageSize") Integer pageSize, @RequestParam("curPage") Integer curPage);

	@RequestMapping(value = "/tdsUser/loadByPhone", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsUserDomain> loadByPhone(@RequestParam("phone") String phone);

	@RequestMapping(value = "/tdsUser/upPassWord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> upPassWord(@RequestParam("usedPass") String usedPass,
			@RequestParam("newPass") String newPass, @RequestParam("userId") Integer userId);

	@RequestMapping(value = "/tdsUser/editUserInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> editUserInfo(TdsUserDomain tdsUserDomain);

	@RequestMapping(value = "/tdsUser/editComInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> editComInfo(TdsCompanyDomain domain, @RequestParam("userId") Integer userId,
			@RequestParam("userName") String userName, @RequestParam("phone") String phone,
			@RequestParam("contact") String contact);

}
