package cn.feignclient.credit_feign_web.service.tds.hihystric;



import java.util.Map;

import org.springframework.stereotype.Component;

import cn.feignclient.credit_feign_web.service.tds.TdsUserBankApyFeignServer;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsUserBankApyDomain;

@Component
public class TdsUserBankApyFeignServerHihystric implements  TdsUserBankApyFeignServer{

	@Override
	public BackResult<Map<String, Object>> countByUserId(Integer userId) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务countByUserId出现异常");
	}

	@Override
	public BackResult<Integer> getAccByNumber(Integer userId, Integer pnameId) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务getAccByNumber出现异常");
	}

	@Override
	public BackResult<Integer> updateByUserId(TdsUserBankApyDomain tdsUserBankApyDomain) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务updateBankApy出现异常");
	}

	@Override
	public BackResult<Integer> addBankApy(TdsUserBankApyDomain domain) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务addBankApy出现异常");
	}

	@Override
	public BackResult<TdsUserBankApyDomain> loadBankApyByUserId(Integer userId) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "user-provider-service服务loadBankApyByUserId出现异常");
	}


	
   
	
}
