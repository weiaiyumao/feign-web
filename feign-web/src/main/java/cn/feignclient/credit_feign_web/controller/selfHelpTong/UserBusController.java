package cn.feignclient.credit_feign_web.controller.selfHelpTong;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;

/**
 * 自助通调用 账户信息接口
 * 
 * @author ChuangLan
 *
 */
@RestController
@RequestMapping("/userBus")
public class UserBusController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(UserBusController.class);

	/**
	 * 查询账户信息
	 * 
	 * @param request
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/findUserInfobyMobile", method = RequestMethod.POST)
	public BackResult<CreUserDomain> findbyMobile(HttpServletRequest request, String mobile) {

		logger.info("自助通手机号：" + mobile + "请求查询账户信息");

		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();

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
			result = userFeignService.findbyMobile(mobile);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自助通手机号：" + mobile + "请求查询账户信息，出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}

	/**
	 * 修改账户信息
	 * 
	 * @param request
	 * @param creUserDomain
	 * @return
	 */
	@RequestMapping(value = "/updateUserEmail", method = RequestMethod.POST)
	public BackResult<CreUserDomain> updateCreUser(HttpServletRequest request, String mobile, String email) {

		logger.info("自助通手机号：" + mobile + "请求修改账户信息");

		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();

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

		if (CommonUtils.isNotString(email)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("邮箱地址不能为空");
			return result;
		}

		try {
			result = userFeignService.updateCreUser(mobile,email);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自助通手机号：" + mobile + "请求修改账户信息，出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}
}
