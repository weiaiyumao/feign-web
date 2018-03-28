package cn.feignclient.credit_feign_web.service.tds.hihystric;


import org.springframework.stereotype.Component;

import cn.feignclient.credit_feign_web.service.tds.TdsMoneyApprovalFeignService;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsApprovalOutDomain;
import main.java.cn.domain.tds.TdsApprovalOutQueryDomain;
import main.java.cn.domain.tds.TdsCommissionDomain;
import main.java.cn.domain.tds.TdsMoneyApprovalDomain;
import main.java.cn.domain.tds.TdsSerualInfoDomain;

@Component
public class TdsMoneyApprovalFeignServiceHiHystric implements TdsMoneyApprovalFeignService{
	
	
	@Override
	public BackResult<Integer> downAddOrder(TdsMoneyApprovalDomain domain){
		return new BackResult<Integer>(ResultCode.RESULT_FAILED, "user-provider-service服务downAddOrder出现异常");
	}

	@Override
	public BackResult<PageDomain<TdsMoneyApprovalDomain>> pageApprovalByUpStatusGo(TdsMoneyApprovalDomain domain) {
	 	return new BackResult<PageDomain<TdsMoneyApprovalDomain>>(ResultCode.RESULT_FAILED, "user-provider-service服务pageApprovalByUpStatusGo出现异常");
	}

	@Override
	public BackResult<PageDomain<TdsMoneyApprovalDomain>> pageApprovalByUpStatusBack(TdsMoneyApprovalDomain domain) {
		return new BackResult<PageDomain<TdsMoneyApprovalDomain>>(ResultCode.RESULT_FAILED, "user-provider-service服务pageApprovalByUpStatusBack出现异常");
	}

	@Override
	public BackResult<Integer> approvalByUpStatusGo(TdsMoneyApprovalDomain domain, String appRemarks) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务approvalByUpStatusGo出现异常");
	}

	@Override
	public BackResult<Integer> approvalByUpStatusOut(TdsMoneyApprovalDomain domain, String appRemarks) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务approvalByUpStatusOut出现异常");
	}

	@Override
	public BackResult<Integer> approvalByUpStatusBack(TdsMoneyApprovalDomain domain, String appRemarks) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务approvalByUpStatusBack出现异常");
	}

	@Override
	public BackResult<PageDomain<TdsSerualInfoDomain>> pageTdsSerualInfo(TdsSerualInfoDomain domain) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务pageTdsSerualInfo出现异常");
	}

	@Override
	public BackResult<PageDomain<TdsCommissionDomain>> pageCommission(TdsCommissionDomain domain) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务pageCommission出现异常");
	}

	@Override 
	public BackResult<PageDomain<TdsApprovalOutDomain>> pageApprovalByUpStatusOut(TdsApprovalOutQueryDomain domain) {
		return new BackResult<PageDomain<TdsApprovalOutDomain>>(ResultCode.RESULT_FAILED, "user-provider-service服务pageApprovalByUpStatusOut出现异常");
	}

	@Override
	public BackResult<Integer> updatePageApprovalByUpStatus(String userId, String tdsCarryId, String status,String remarks,String order) {
		return new BackResult<Integer>(ResultCode.RESULT_FAILED, "user-provider-service服务updatePageApprovalByUpStatus出现异常");
	}

	
	

}
