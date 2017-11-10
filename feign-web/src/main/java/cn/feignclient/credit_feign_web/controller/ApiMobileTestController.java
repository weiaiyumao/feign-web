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
import main.java.cn.common.RedisKeys;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.MobileInfoDomain;
import main.java.cn.domain.MobileTestLogDomain;
import main.java.cn.domain.page.PageDomain;

@RestController
@RequestMapping("/feign/apiMobileTest")
public class ApiMobileTestController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(ApiAccountInfoController.class);

	@Autowired
	private ApiAccountInfoFeignService apiAccountInfoFeignService;

	@Autowired
	private ApiMobileTestService apiMobileTestService;

	@Autowired
	private UserAccountFeignService userAccountFeignService;

	/**
	 * 对外API账户2次清洗接口
	 * 
	 * @param apiName
	 * @param password
	 * @param ip
	 * @param mobileNumbers
	 * @return
	 */
	@RequestMapping(value = "/findByMobileNumbers", method = RequestMethod.POST)
	public synchronized BackResult<List<MobileInfoDomain>> findByMobileNumbers(HttpServletRequest request,
			HttpServletResponse response, String apiName, String password, String mobile) {

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

			if (CommonUtils.isNotString(mobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的手机号码不能为空");
				return result;
			}

			String[] phones = mobile.split(",");

			String ip = super.getIpAddr(request);

			logger.info("账户号：" + apiName + "的IP地址是：" + ip);

			// 1、账户信息检测
			BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkApiAccount(apiName, password, ip,
					phones.length);

			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}

			// 2、执行检测返回检测结果
			result = apiMobileTestService.findByMobileNumbers(mobile, resultCreUser.getResultObj().toString());

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
				BackResult<Boolean> resultConsume = userAccountFeignService.consumeApiAccount(
						resultCreUser.getResultObj().toString(), String.valueOf(result.getResultObj().size()));

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

	/**
	 * 对外API账户2次清洗接口
	 * 
	 * @param apiName
	 * @param password
	 * @param ip
	 * @param mobileNumbers
	 * @return
	 */
	@RequestMapping("/getByMobileNumbers")
	public BackResult<Boolean> getByMobileNumbers(HttpServletRequest request, HttpServletResponse response,
			String apiName, String password, String mobileNumbers) {

		BackResult<Boolean> result = new BackResult<Boolean>();

		try {

			// 商户合法性
			if (CommonUtils.isNotString(apiName)) {
				result.setResultCode(ResultCode.RESULT_API_NOTACCOUNT);
				result.setResultMsg("商户API账户名不能为空");
				return result;
			}

			// 商户密码合法性
			if (CommonUtils.isNotString(password)) {
				result.setResultCode(ResultCode.RESULT_RCAPI_MOTAPIPWD);
				result.setResultMsg("商户API账户密码不能为空");
				return result;
			}

			// 手机号码集合合法性
			if (CommonUtils.isNotString(mobileNumbers)) {
				result.setResultCode(ResultCode.RESULT_RCAPI_MOBILESFORMATEX);
				result.setResultMsg("检测的手机号码不能为空");
				return result;
			}

			String[] phones = mobileNumbers.split(",");

			// 单次检测的最大条数
			if (phones.length > 20) {
				result.setResultCode(ResultCode.RESULT_RCAPI_SINGLIMIT);
				result.setResultMsg("单次检测超过最大限制20条");
				return result;
			}

			// 单个手机号码格式校验
			for (String mobile : phones) {
				if (!CommonUtils.isNumeric(mobile)) {
					result.setResultCode(ResultCode.RESULT_RCAPI_MOBILESFORMATEX);
					result.setResultMsg("检测的手机号码格式不正确");
					return result;
				}
			}
			
			String ip = super.getIpAddr(request);

			// 1、账户信息检测
			BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkApiAccount(apiName, password, ip,phones.length);

			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}

			// 正在检测的总条数
			String progresstestCount = redisClinet.get(RedisKeys.getUserTestCountKey(resultCreUser.getResultObj()));

			if (!CommonUtils.isNotString(progresstestCount)) {
				// 如果正在检测的条数+正准备检测的条数超过100条不能进行检测
				if (Integer.parseInt(progresstestCount) + phones.length >= 100) {
					result.setResultCode(ResultCode.RESULT_RCAPI_THREADUPPERLIMIT);
					result.setResultMsg("商户最大检测条数超过限制");
					return result;
				}

			} else {
				redisClinet.set(RedisKeys.getUserTestCountKey(resultCreUser.getResultObj()), 0,
						RedisKeys.TEST_COUNT_EXPIRESECONDS);
			}
			
			// 开始检测

			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商户号：" + apiName + "执行账户2次清洗出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
			result.setResultObj(null);
		}

		return result;
	}

	@RequestMapping(value = "/getPageByUserId", method = RequestMethod.POST)
	public BackResult<PageDomain<MobileTestLogDomain>> getPageByUserId(HttpServletRequest request,
			HttpServletResponse response, int pageNo, int pageSize, String creUserId, String mobile, String token) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<PageDomain<MobileTestLogDomain>> result = new BackResult<PageDomain<MobileTestLogDomain>>();

		try {

			if (CommonUtils.isNotIngeter(pageNo)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("分页PageNo不能为空");
				return result;
			}

			if (CommonUtils.isNotIngeter(pageSize)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("分页pageSize不能为空");
				return result;
			}

			if (CommonUtils.isNotString(creUserId)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("用户creUserId不能为空");
				return result;
			}

			if (CommonUtils.isNotString(mobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("手机号码不能为空");
				return result;
			}

			if (CommonUtils.isNotString(token)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("token不能为空");
				return result;
			}

			if (!isLogin(mobile, token)) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败");
				return result;
			}

			result = apiMobileTestService.getPageByUserId(pageNo, pageSize, creUserId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户ID：" + creUserId + "获取检测列表系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
			result.setResultObj(null);
		}

		return result;

	}

}
