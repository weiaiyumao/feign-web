package cn.feignclient.credit_feign_web.service.tds;


import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.tds.TdsCompanyDomain;
import main.java.cn.domain.tds.TdsModularDomain;
import main.java.cn.domain.tds.TdsUserDomain;

@FeignClient(value = "user-provider-service")
public interface TdsUserLoginFeignService {

	
	@RequestMapping(value = "/userLogin/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsUserDomain> login(TdsUserDomain tdsUserDomain);
	
	
	@RequestMapping(value="/userLogin/moduleLoadingByUsreId",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsModularDomain>> moduleLoadingByUsreId(@RequestParam("userId")Integer userId);
	
	
	
	@RequestMapping(value = "/userLogin/loadComById", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsCompanyDomain> loadComById(@RequestParam("id")Integer id);
	
	
	
    //=============首页==========
	@RequestMapping(value = "/home/countByUserId", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Map<String, Object>> countByUserId(@RequestParam("userId")Integer userId);
	
	
	@RequestMapping(value = "/home/getAccByNumber", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> getAccByNumber(@RequestParam("userId")Integer userId,@RequestParam("pnameId")Integer pnameId);
	
}
