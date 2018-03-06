package cn.feignclient.credit_feign_web.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.AccountInfoDomain;
import main.java.cn.domain.ApiAccountInfoDomain;

@Component
public class ApiAccountInfoFeignServiceHiHystric implements ApiAccountInfoFeignService {

	@Override
	public BackResult<ApiAccountInfoDomain> findByCreUserId(String creUserId) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务findByCreUserId出现异常");
	}

	@Override
	public BackResult<ApiAccountInfoDomain> updateApiAccountInfo(ApiAccountInfoDomain domain) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务updateApiAccountInfo出现异常");
	}

	@Override
	public BackResult<Integer> checkApiAccount(String apiName, String password, String ip, int checkCount) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务checkApiAccount出现异常");
	}

	@Override
	public BackResult<Integer> checkRqApiAccount(String apiName, String password, String ip, int checkCount) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务checkRqApiAccount出现异常");
	}

	@Override
	public BackResult<AccountInfoDomain> checkTcAccount(String apiName, String password, String method, String ip) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务checkTcAccount出现异常");
	}

	@Override
	public BackResult<Integer> updateTcAccount(Map<String, Object> params) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务updateTcAccount出现异常");
	}

	@Override
	public BackResult<Integer> checkMsAccount(String apiName, String password, String ip, int checkCount) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务checkMsAccount出现异常");
	}

}
