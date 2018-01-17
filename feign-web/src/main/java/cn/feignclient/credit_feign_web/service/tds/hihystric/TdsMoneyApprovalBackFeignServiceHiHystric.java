package cn.feignclient.credit_feign_web.service.tds.hihystric;


import org.springframework.stereotype.Component;

import cn.feignclient.credit_feign_web.service.tds.TdsMoneyApprovalBackFeignService;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsMoneyApprovalBackDomain;

@Component
public class TdsMoneyApprovalBackFeignServiceHiHystric implements TdsMoneyApprovalBackFeignService{

	@Override
	public BackResult<PageDomain<TdsMoneyApprovalBackDomain>> pageApprovalBack(TdsMoneyApprovalBackDomain domain) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务pageApprovalBack出现异常");
	}

	@Override
	public BackResult<Integer> backOrderMoney(TdsMoneyApprovalBackDomain domain) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务backOrderMoney出现异常");
	}

	@Override
	public BackResult<Integer> approvalByUpStatusBack(TdsMoneyApprovalBackDomain domain, String appRemarks) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务approvalByUpStatusBack出现异常");
	}
	

    
}
