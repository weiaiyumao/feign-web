package cn.feignclient.credit_feign_web.service;

import java.util.List;

import org.springframework.stereotype.Component;

import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CvsFilePathDomain;
import main.java.cn.domain.RunTestDomian;
import main.java.cn.domain.page.PageDomain;

@Component
public class CreditProviderServiceHiHystric implements CreditProviderService {

	@Override
	public BackResult<RunTestDomian> runTheTest(String fileUrl, String userId, String timestamp, String mobile) {
		return new BackResult<RunTestDomian>(ResultCode.RESULT_FAILED, "credit-provider-service服务runTheTest出现异常");
	}

	@Override
	public BackResult<List<CvsFilePathDomain>> findByUserId(String userId) {
		return new BackResult<List<CvsFilePathDomain>>(ResultCode.RESULT_FAILED, "credit-provider-service服务findByUserId出现异常");
	}

	@Override
	public BackResult<Boolean> deleteCvsByIds(String ids, String userId) {
		return new BackResult<Boolean>(ResultCode.RESULT_FAILED, "credit-provider-service服务deleteCvsByIds出现异常");
	}

	@Override
	public BackResult<PageDomain<CvsFilePathDomain>> getPageByUserId(int pageNo, int pageSize, String userId) {
		return new BackResult<PageDomain<CvsFilePathDomain>>(ResultCode.RESULT_FAILED, "credit-provider-service服务getPageByUserId出现异常");
	}

	@Override
	public BackResult<RunTestDomian> theTest(String fileUrl, String userId, String source, String mobile,
			String startLine, String type) {
		return new BackResult<RunTestDomian>(ResultCode.RESULT_FAILED, "credit-provider-service服务theTest出现异常");
	}

}
