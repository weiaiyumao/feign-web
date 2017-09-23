package cn.feignclient.credit_feign_web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.UserAccountFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.TrdOrderDomain;
import main.java.cn.domain.UserAccountDomain;

@RestController
@RequestMapping("/userAccount")
public class UserProviderController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserAccountFeignService userAccountFeignService;

	@RequestMapping(value = "/findbyMobile", method = RequestMethod.GET)
	public BackResult<UserAccountDomain> findbyMobile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String mobile, String token) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<UserAccountDomain> result = new BackResult<UserAccountDomain>();

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
			result.setResultMsg("注销校验失败无法注销");
			return result;
		}

		result = userAccountFeignService.findbyMobile(mobile);

		return result;
	}

	@RequestMapping(value = "/rechargeOrRefunds", method = RequestMethod.GET)
	public BackResult<Boolean> rechargeOrRefunds(TrdOrderDomain trdOrderDomain) {

		Assert.notNull(trdOrderDomain, "The param trdOrderDomain not be null!");
		Assert.notNull(trdOrderDomain.getCreUserId(), "The param creUserId not be null!");
		Assert.notNull(trdOrderDomain.getClOrderNo(), "The param clOrderNo not be null!");
		Assert.notNull(trdOrderDomain.getNumber(), "The param number not be null!");
		Assert.notNull(trdOrderDomain.getMoney(), "The param money not be null!");
		Assert.notNull(trdOrderDomain.getPayType(), "The param payType not be null!");
		Assert.notNull(trdOrderDomain.getPayTime(), "The param payTime not be null!");
		Assert.notNull(trdOrderDomain.getType(), "The param type not be null!");

		return userAccountFeignService.rechargeOrRefunds(trdOrderDomain);
	}

	@RequestMapping(value = "/findTrdOrderByCreUserId", method = RequestMethod.GET)
	public BackResult<List<TrdOrderDomain>> findTrdOrderByMobile(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("mobile") String mobile, String token,Integer creUserId) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<List<TrdOrderDomain>> result = new BackResult<List<TrdOrderDomain>>();

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
			result.setResultMsg("注销校验失败无法注销");
			return result;
		}

		try {
			result = userAccountFeignService.findTrdOrderByCreUserId(creUserId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户手机号：" + mobile + "执行查询订单信息出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}

}
