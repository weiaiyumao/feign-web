package cn.feignclient.credit_feign_web.service.tds;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.tds.TdsCompanyDomain;
import main.java.cn.domain.tds.TdsPositionDomain;

@FeignClient(value = "user-provider-service")
public interface TdsPositionFeignService {
	
	
	
	@RequestMapping(value = "/position/selectByDeparId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsPositionDomain>> selectByDeparId(@RequestParam("departmentId")Integer departmentId);
	
	@RequestMapping(value = "/position/selectCompanyAll", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsCompanyDomain>> selectCompanyAll();
	
	
	
}
