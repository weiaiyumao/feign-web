package cn.feignclient.credit_feign_web.controller.selfHelpTong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.UserAccountFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.ErpTradeDomain;
import main.java.cn.domain.TrdOrderDomain;
import main.java.cn.domain.UserAccountDomain;

/**
 * 自助通调用 账户接口
 * @author ChuangLan
 *
 */
@RestController
@RequestMapping("/userAccountBus")
public class UserAccountBusController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(UserAccountBusController.class);

	@Autowired
	private UserAccountFeignService userAccountFeignService;

	/**
	 * 自助通根据手机号码查询账户信息
	 * @param request
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/findUserAccountbyMobile", method = RequestMethod.POST)
	public BackResult<UserAccountDomain> findbyMobile(HttpServletRequest request, String mobile) {

		logger.info("自助通手机号：" + mobile + "请求查询账户余额");

		BackResult<UserAccountDomain> result = new BackResult<UserAccountDomain>();

		if (!checkSign(request)) {
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("签名验证失败");
			return result;
		}
		
		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
		}

		try {
			result = userAccountFeignService.findbyMobile(mobile);
		} catch (Exception e) {
			logger.error("自助通手机号：" + mobile + "请求查询账户余额!出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}
	
	@RequestMapping(value = "/rechargeOrRefunds", method = RequestMethod.POST)
	public BackResult<ErpTradeDomain> rechargeOrRefunds(HttpServletRequest request, HttpServletResponse response,TrdOrderDomain trdOrderDomain) {
		
		BackResult<ErpTradeDomain> result = new BackResult<ErpTradeDomain>();
		
		if (!checkSign(request)) {
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("签名验证失败");
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
		
		try {
			logger.info("自助通手机号：" + trdOrderDomain.getMobile() + "请求 " + (trdOrderDomain.getType().equals("1") ? "充值" : "退款") +"操作");
			result = userAccountFeignService.rechargeOrRefunds(trdOrderDomain);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自助通手机号：" + trdOrderDomain.getMobile() + "请求 " + (trdOrderDomain.getType().equals("1") ? "充值" : "退款") +"操作，出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}
		
		return result;
	}

}
