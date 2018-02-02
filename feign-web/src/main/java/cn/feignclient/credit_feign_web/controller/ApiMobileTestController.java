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
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
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

//	@Autowired
//	private UserAccountFeignService userAccountFeignService;

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
			BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkRqApiAccount(apiName, password, ip,
					phones.length);

			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}

			// 2、执行检测返回检测结果
			result = apiMobileTestService.findByMobileNumbers(mobile, resultCreUser.getResultObj().toString());

//			if (!result.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
//				return result;
//			}
//
//			int changeCount = 0;
//
//			if (!CommonUtils.isNotEmpty(result.getResultObj())) {
//				for (MobileInfoDomain domain : result.getResultObj()) {
//					if (domain.getChargesStatus().equals("1")) {
//						changeCount = changeCount + 1;
//					}
//				}
//			}
//
//			if (changeCount > 0) {
//
//				// 3、结算
//				BackResult<Boolean> resultConsume = userAccountFeignService
//						.consumeRqApiAccount(resultCreUser.getResultObj().toString(), String.valueOf("1"));
//
//				if (!resultConsume.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
//					logger.error("------------------------------------商户号：" + apiName + "执行账户2次清洗出现记账系统异常："
//							+ resultConsume.getResultMsg());
//				}
//
//			}

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
			HttpServletResponse response, int pageNo, int pageSize, String creUserId, String mobile, String token,String type) {

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

			if (CommonUtils.isNotString(type)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("类型不能为空");
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

			result = apiMobileTestService.getPageByUserId(pageNo, pageSize, creUserId,type);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户ID：" + creUserId + "获取检测列表系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
			result.setResultObj(null);
		}

		return result;

	}

	/**
	 * 空号API接口
	 * 
	 * @param apiName
	 * @param password
	 * @param ip
	 * @param mobileNumbers
	 * @return
	 */
	@RequestMapping(value = "/findByMobile", method = RequestMethod.POST)
	public synchronized BackResult<MobileInfoDomain> findByMobile(HttpServletRequest request,
			HttpServletResponse response, String apiName, String password, String mobile) {

		BackResult<MobileInfoDomain> result = new BackResult<MobileInfoDomain>();

		Long s = System.currentTimeMillis();
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

			// 验证是否为正常的１１位有效数字
			if (!CommonUtils.isNumeric(mobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("请输入正确的11位手机号码");
				return result;
			}

			String ip = super.getIpAddr(request);
			
			logger.info("手机号：" + mobile + "请求基础验证消耗时间：" + (s - System.currentTimeMillis()));

//			logger.info("---------不发短息----------账户号：" + apiName + "的IP地址是：" + ip);

			
			Long m = System.currentTimeMillis();
			// 1、账户信息检测
			BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkApiAccount(apiName, password, ip, 1);
			logger.info("手机号：" + mobile + "请求userService账户检测消耗时间：" + (m - System.currentTimeMillis()));
			
			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}

			Long l = System.currentTimeMillis();
			// 2、执行检测返回检测结果
			result = apiMobileTestService.findByMobile(mobile, resultCreUser.getResultObj().toString());
			logger.info("手机号：" + mobile + "请求credit检测消耗时间：" + (l - System.currentTimeMillis()));
			
//			if (result.getResultCode().equals(ResultCode.RESULT_SUCCEED) && result.getResultObj().getChargesStatus().equals("1")) {
//				// 3、结算
//				BackResult<Boolean> resultConsume = userAccountFeignService
//						.consumeApiAccount(resultCreUser.getResultObj().toString(), String.valueOf(1));
//
//				if (!resultConsume.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
//					logger.error("------------------------------------商户号：" + apiName + "执行空号API出现记账系统异常："
//							+ resultConsume.getResultMsg());
//				}
//
//			}
			logger.info("手机号：" + mobile + "请求总共消耗时间：" + (s - System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商户号：" + apiName + "执行空号API出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
		}

		return result;
	}

}
