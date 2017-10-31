package cn.feignclient.credit_feign_web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.ApiAccountInfoFeignService;
import cn.feignclient.credit_feign_web.service.ApiMobileTestService;
import cn.feignclient.credit_feign_web.service.UserAccountFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.MobileInfoDomain;

@RestController
@RequestMapping("/feign/apiMobileTest")
public class ApiMobileTestController extends BaseController{

	private final static Logger logger = LoggerFactory.getLogger(ApiAccountInfoController.class);
	
	@Autowired
	private ApiAccountInfoFeignService apiAccountInfoFeignService;
	
	@Autowired
	private ApiMobileTestService apiMobileTestService;
	
	@Autowired
	private UserAccountFeignService userAccountFeignService;
	
	/**
	 * 对外API账户2次清洗接口
	 * @param apiName
	 * @param password
	 * @param ip
	 * @param mobileNumbers
	 * @return
	 */
	@RequestMapping(value = "/findByMobileNumbers", method = RequestMethod.POST)
	public BackResult<List<MobileInfoDomain>> findByMobileNumbers(HttpServletRequest request,
			HttpServletResponse response,String apiName,String password,String mobileNumbers){
		
		BackResult<List<MobileInfoDomain>> result = new BackResult<List<MobileInfoDomain>>();
		
		try {
			
			if (CommonUtils.isNotString(apiName)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户名不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(password)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户密码不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(mobileNumbers)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的手机号码不能为空");
				return result;
			}
			
			String[] phones = mobileNumbers.split(",");
			
			String ip = super.getIpAddr(request);
			
			// 1、账户信息检测
			BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkApiAccount(apiName, password, ip, phones.length);
			
			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}
			
			// 2、执行检测返回检测结果
			result = apiMobileTestService.findByMobileNumbers(mobileNumbers,resultCreUser.getResultObj().toString());
			
			if (!result.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				return result;
			}
			
			int changeCount = 0;
			
			if (!CommonUtils.isNotEmpty(result.getResultObj())) {
				for (MobileInfoDomain domain : result.getResultObj()) {
					if (domain.getChargesStatus().equals("1")) {
						changeCount = changeCount + 1;
					}
				}
			}
			
			if (changeCount > 0) {
				// 3、结算
				BackResult<Boolean> resultConsume = userAccountFeignService.consumeApiAccount(resultCreUser.getResultObj().toString(), String.valueOf(result.getResultObj().size()));
				
				if (!resultConsume.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
					result.setResultCode(resultConsume.getResultCode());
					result.setResultMsg(resultConsume.getResultMsg());
					return result;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商户号：" + apiName + "执行账户2次清洗出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
			result.setResultObj(null);
		}
		
		return result;
	}
}
