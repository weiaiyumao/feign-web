package cn.feignclient.credit_feign_web.service;

import java.math.BigDecimal;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.TrdOrderDomain;

@FeignClient(value = "user-provider-service",fallback = TrdOrderFeignServiceHiHystric.class)
public interface TrdOrderFeignService {

	@RequestMapping(value = "/trdorder/alipayrecharge", method = RequestMethod.GET)
	BackResult<String> alipayrecharge(@RequestParam("creUserId")Integer creUserId,@RequestParam("productsId")Integer productsId,@RequestParam("number")Integer number,@RequestParam("money")BigDecimal money,@RequestParam("payType")String payType,@RequestParam("type")String type);

	@RequestMapping(value = "/trdorder/findOrderInfoByOrderNo", method = RequestMethod.GET)
	BackResult<TrdOrderDomain> findOrderInfoByOrderNo(@RequestParam("orderNo")String orderNo);
}
