package cn.feignclient.credit_feign_web.service.tds.hihystric;


import org.springframework.stereotype.Component;

import cn.feignclient.credit_feign_web.service.tds.TdsMoneyApprovalFeignService;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsMoneyApprovalDomain;

@Component
public class TdsMoneyApprovalFeignServiceHiHystric implements TdsMoneyApprovalFeignService{
	
	
	@Override
	public BackResult<Integer> downAddOrder(TdsMoneyApprovalDomain domain){
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务downAddOrder出现异常");
	}

	
	

}
