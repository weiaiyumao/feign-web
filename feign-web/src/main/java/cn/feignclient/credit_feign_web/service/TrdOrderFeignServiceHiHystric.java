package cn.feignclient.credit_feign_web.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.TrdOrderDomain;

@Component
public class TrdOrderFeignServiceHiHystric implements TrdOrderFeignService {

	@Override
	public BackResult<String> alipayrecharge(Integer creUserId, Integer productsId, Integer number, BigDecimal money,
			String payType, String type) {
		return new BackResult<String>(ResultCode.RESULT_FAILED, "user-provider-service服务alipayrecharge出现异常");
	}

	@Override
	public BackResult<TrdOrderDomain> findOrderInfoByOrderNo(String orderNo) {
		return new BackResult<TrdOrderDomain>(ResultCode.RESULT_FAILED, "user-provider-service服务findOrderInfoByOrderNo出现异常");
	}

}
