package cn.feignclient.credit_feign_web.service;

import java.util.List;

import org.springframework.stereotype.Component;

import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.MobileInfoDomain;
import main.java.cn.domain.MobileTestLogDomain;
import main.java.cn.domain.page.PageDomain;

@Component
public class ApiMobileTestServiceHiHystric implements ApiMobileTestService {

	@Override
	public BackResult<List<MobileInfoDomain>> findByMobileNumbers(String mobileNumbers, String userId) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "credit-provider-service服务findByMobileNumbers出现异常");
	}

	@Override
	public BackResult<PageDomain<MobileTestLogDomain>> getPageByUserId(int pageNo, int pageSize, String userId) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "credit-provider-service服务getPageByUserId出现异常");
	}

	@Override
	public BackResult<MobileInfoDomain> findByMobile(String mobile, String userId) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "credit-provider-service服务findByMobile出现异常");
	}

}
