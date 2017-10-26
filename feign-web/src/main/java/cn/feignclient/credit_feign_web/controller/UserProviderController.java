package cn.feignclient.credit_feign_web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.UserAccountFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.ErpTradeDomain;
import main.java.cn.domain.TrdOrderDomain;
import main.java.cn.domain.UserAccountDomain;
import main.java.cn.hhtp.util.MD5Util;

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
	
	/**
	 * erp 调用查询账户余额接口
	 * @param request
	 * @param response
	 * @param mobile
	 * @param timestamp
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/api/findbyUserAccount", method = RequestMethod.POST)
	public BackResult<UserAccountDomain> findbyUserAccount(String mobile, String timestamp,String token) {

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

		String md5Token = MD5Util.getInstance().getMD5Code(timestamp + apiKey);
		
		if (!md5Token.equals(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("签名验证失败");
			return result;
		}

		try {
			result = userAccountFeignService.findbyMobile(mobile);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户ID：" + mobile + "执行查询账户余额出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}
	
	@RequestMapping(value = "/api/rechargeOrRefunds", method = RequestMethod.POST)
	public BackResult<ErpTradeDomain> rechargeOrRefunds(HttpServletRequest request, HttpServletResponse response,TrdOrderDomain trdOrderDomain,String timestamp,String token) {

		BackResult<ErpTradeDomain> result = new BackResult<ErpTradeDomain>();
		
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			result.setResultObj(null);
			return result;
		}
		
		if (null == trdOrderDomain) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("参数不能为空");
			result.setResultObj(null);
			return result;
		}
		
		if (CommonUtils.isNotString(trdOrderDomain.getClOrderNo())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("订单业务编号不能为空");
			result.setResultObj(null);
			return result;
		}
		
		if (CommonUtils.isNotIngeter(trdOrderDomain.getProductsId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("产品编号不能为空");
			result.setResultObj(null);
			return result;
		}
		
		if (CommonUtils.isNotBigDecimal(trdOrderDomain.getMoney())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("金额不能为空");
			result.setResultObj(null);
			return result;
		}
		
		if (null == trdOrderDomain.getPayTime()) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("成功支付时间不能为空");
			result.setResultObj(null);
			return result;
		} 
		
		if (CommonUtils.isNotString(trdOrderDomain.getType())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("类型不能为空 1：充值 2：退款");
			result.setResultObj(null);
			return result;
		} 
		
		if (CommonUtils.isNotString(trdOrderDomain.getMobile())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			result.setResultObj(null);
			return result;
		}
		
		String md5Token = MD5Util.getInstance().getMD5Code(timestamp + apiKey);
		
		if (!md5Token.equals(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("签名验证失败");
			result.setResultObj(null);
			return result;
		}
		try {
			result = userAccountFeignService.rechargeOrRefunds(trdOrderDomain);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户ID：" + trdOrderDomain.getCreUserId() + "执行查询订单信息出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
			result.setResultObj(null);
		}
		
		return result;
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
