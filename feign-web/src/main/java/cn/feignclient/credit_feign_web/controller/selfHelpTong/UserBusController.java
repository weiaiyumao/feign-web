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
import cn.feignclient.credit_feign_web.service.ApiAccountInfoFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import cn.feignclient.credit_feign_web.utils.MD5Util;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.ApiAccountInfoDomain;
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
	
	@Autowired
	private ApiAccountInfoFeignService apiAccountInfoFeignService;

	/**
	 * 自助通调用激活账户
	 * 
	 * @param request
	 * @param response
	 * @param mobile
	 * @param code
	 * @return
	 */
	@RequestMapping("/activateUser")
	public BackResult<String> activateUser(HttpServletRequest request, HttpServletResponse response, String mobile,
			String timestamp, String token) {

		logger.info("自助通请求用户【" + mobile +"】，请求激活账户！token:"+token + "timestamp:" + timestamp);
		
		BackResult<String> result = new BackResult<String>();

		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			result.setResultObj(null);
			return result;
		}

		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
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

		CreUserDomain creUserDomain = new CreUserDomain();
		creUserDomain.setUserPhone(mobile);

		// 查询用户
		BackResult<CreUserDomain> creResult = userFeignService.activateUserZzt(creUserDomain);

		if (!creResult.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
			result.setResultMsg(creResult.getResultMsg());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultObj(null);
			return result;
		}

		result.setResultObj(creResult.getResultObj().getId().toString());

		return result;
	}
	
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
	
	@RequestMapping(value = "/findAPIInfo", method = RequestMethod.POST)
	public BackResult<ApiAccountInfoDomain> findTrdOrderByMobile(HttpServletRequest request,String mobile) {

		logger.info("自助通手机号：" + mobile + "请求查看API账户信息");

		BackResult<ApiAccountInfoDomain> result = new BackResult<ApiAccountInfoDomain>();

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
			
			CreUserDomain user = findByMobile(mobile);

			if (null == user) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败，系统不存在该手机号码的用户");
				return result;
			}
			
			result = apiAccountInfoFeignService.findByCreUserId(user.getId().toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户手机号：" + mobile + "执行查询API账户信息出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}
}
