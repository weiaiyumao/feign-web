package cn.feignclient.credit_feign_web.service;

import org.springframework.stereotype.Component;

import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.ApiAccountInfoDomain;

@Component
public class ApiAccountInfoFeignServiceHiHystric implements ApiAccountInfoFeignService {

	@Override
	public BackResult<ApiAccountInfoDomain> findByCreUserId(String creUserId) {
		return new BackResult<ApiAccountInfoDomain>(ResultCode.RESULT_FAILED, "user-provider-service服务findByCreUserId出现异常");
	}

	@Override
	public BackResult<ApiAccountInfoDomain> updateApiAccountInfo(ApiAccountInfoDomain domain) {
		return new BackResult<ApiAccountInfoDomain>(ResultCode.RESULT_FAILED, "user-provider-service服务updateApiAccountInfo出现异常");
	}

	@Override
	public BackResult<Integer> checkApiAccount(String apiName, String password, String ip, int checkCount) {
		return new BackResult<Integer>(ResultCode.RESULT_FAILED, "user-provider-service服务checkApiAccount出现异常");
	}

	@Override
	public BackResult<Integer> checkRqApiAccount(String apiName, String password, String ip, int checkCount) {
		return new BackResult<Integer>(ResultCode.RESULT_FAILED, "user-provider-service服务checkRqApiAccount出现异常");
	}

}
