package cn.feignclient.credit_feign_web.service;

import org.springframework.stereotype.Component;

import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;

@Component
public class UserFeignServiceHiHystric implements UserFeignService {

	@Override
	public BackResult<CreUserDomain> findbyMobile(String mobile) {
		return new BackResult<CreUserDomain>(ResultCode.RESULT_FAILED, "user-provider-service服务findbyMobile出现异常");
	}

	@Override
	public BackResult<CreUserDomain> findOrsaveUser(CreUserDomain creUserDomain) {
		return new BackResult<CreUserDomain>(ResultCode.RESULT_FAILED, "user-provider-service服务findOrsaveUser出现异常");
	}

	@Override
	public BackResult<CreUserDomain> updateCreUser(CreUserDomain creUserDomain) {
		return new BackResult<CreUserDomain>(ResultCode.RESULT_FAILED, "user-provider-service服务updateCreUser出现异常");
	}

	@Override
	public BackResult<CreUserDomain> updateCreUser(String userPhone, String email) {
		return new BackResult<CreUserDomain>(ResultCode.RESULT_FAILED, "user-provider-service服务updateCreUser出现异常");
	}

	@Override
	public BackResult<CreUserDomain> activateUser(CreUserDomain creUserDomain) {
		return new BackResult<CreUserDomain>(ResultCode.RESULT_FAILED, "user-provider-service服务activateUser出现异常");
	}

	@Override
	public BackResult<CreUserDomain> activateUserZzt(CreUserDomain creUserDomain) {
		return new BackResult<CreUserDomain>(ResultCode.RESULT_FAILED, "user-provider-service服务activateUserZzt出现异常");
	}

}
