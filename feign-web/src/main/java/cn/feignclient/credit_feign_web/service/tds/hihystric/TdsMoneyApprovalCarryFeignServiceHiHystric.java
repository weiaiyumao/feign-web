package cn.feignclient.credit_feign_web.service.tds.hihystric;


import java.util.Map;

import org.springframework.stereotype.Component;

import cn.feignclient.credit_feign_web.service.tds.TdsCarryFeignService;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsCarryDomain;

@Component
public class TdsMoneyApprovalCarryFeignServiceHiHystric implements TdsCarryFeignService{

	@Override
	public BackResult<PageDomain<TdsCarryDomain>> pageTdsCarry(TdsCarryDomain domain) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务pageTdsCarry出现异常");
	}

	@Override
	public BackResult<Map<String, Object>> getCarryByUserId(Integer userId) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务getCarryByUserId出现异常");
	}


	

    
}
