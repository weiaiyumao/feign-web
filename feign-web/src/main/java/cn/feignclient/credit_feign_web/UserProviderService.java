//package cn.feignclient.credit_feign_web;
//
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import main.java.cn.common.BackResult;
//import main.java.cn.domain.TrdOrderDomain;
//import main.java.cn.domain.UserAccountDomain;
//
//@FeignClient(value = "user-provider-service")
//public interface UserProviderService {
//	
//	@RequestMapping(value = "/userAccount/findbyMobile", method = RequestMethod.GET)
//	BackResult<UserAccountDomain> findbyMobile(String mobile);
//	
//	@RequestMapping(value = "/userAccount/rechargeOrRefunds", method = RequestMethod.GET)
//	BackResult<Boolean> rechargeOrRefunds(TrdOrderDomain trdOrderDomain);
//	
//}
