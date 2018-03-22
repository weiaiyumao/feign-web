package cn.feignclient.credit_feign_web.service.tds;



import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.feignclient.credit_feign_web.service.tds.hihystric.TdsUserBankApyFeignServerHihystric;
import main.java.cn.common.BackResult;
import main.java.cn.domain.tds.TdsUserBankApyDomain;

@FeignClient(value = "user-provider-service",fallback =TdsUserBankApyFeignServerHihystric.class)
public interface TdsUserBankApyFeignServer {
   
	
		@RequestMapping(value = "/home/countByUserId", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
		public BackResult<Map<String, Object>> countByUserId(@RequestParam("userId")Integer userId);
		
		
		@RequestMapping(value = "/home/getAccByNumber", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public BackResult<Integer> getAccByNumber(@RequestParam("userId")Integer userId,@RequestParam("pnameId")Integer pnameId);
		
		
		
		@RequestMapping(value = "/home/updateByUserId", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
		public BackResult<Integer> updateByUserId(TdsUserBankApyDomain tdsUserBankApyDomain);
		
		
		
		@RequestMapping(value = "/home/addBankApy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public BackResult<Integer> addBankApy(TdsUserBankApyDomain domain);
		
		
		@RequestMapping(value = "/home/loadBankApyByUserId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public BackResult<TdsUserBankApyDomain> loadBankApyByUserId(@RequestParam("userId")Integer userId);
}

