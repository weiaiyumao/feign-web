package cn.feignclient.credit_feign_web.controller.tds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.CreditProviderService;
import cn.feignclient.credit_feign_web.service.UserAccountFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;
import main.java.cn.domain.CvsFilePathDomain;
import main.java.cn.domain.TrdOrderDomain;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsCreUserAccountLogDomain;
import main.java.cn.domain.tds.TdsUserAccountInfoDomain;

@RestController
@RequestMapping("/tdsUserAccount")
public class TdsUserAccountController extends TdsBaseController {

	private final static Logger logger = LoggerFactory.getLogger(TdsUserAccountController.class);

	@Autowired
	private UserAccountFeignService userAccountFeignService;
	
	@Autowired
	private CreditProviderService creditProviderService;

	/**
	 * 分销商-查询账户信息
	 * 
	 * @param request
	 * @param response
	 * @param mobile
	 * @param timestamp
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/findTdsUserAccountInfoDomainByMobile", method = RequestMethod.POST)
	public BackResult<TdsUserAccountInfoDomain> findTdsUserAccountInfoDomainByMobile(String mobile) {

		BackResult<TdsUserAccountInfoDomain> result = new BackResult<TdsUserAccountInfoDomain>();

		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
		}

		try {
			result = userAccountFeignService.findTdsUserAccountInfoDomainByMobile(mobile);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户ID：" + mobile + "执行查询账户余额出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}

	/**
	 * 分销商-查询账户信息
	 * 
	 * @param request
	 * @param response
	 * @param mobile
	 * @param timestamp
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/updateUserAccountByTds", method = RequestMethod.POST)
	public BackResult<Boolean> updateUserAccountByTds(TdsCreUserAccountLogDomain domain) {

		BackResult<Boolean> result = new BackResult<Boolean>();

		if (CommonUtils.isNotIngeter(domain.getCreUserId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户id不能为空");
			return result;
		}

		if (CommonUtils.isNotIngeter(domain.getNumber())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("条数不能为空");
			return result;
		}

		if (CommonUtils.isNotString(domain.getType())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("类型不能为空");
			return result;
		}

		if (CommonUtils.isNotString(domain.getCreater())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("条数不能为空");
			return result;
		}

		try {
			result = userAccountFeignService.updateUserAccountByTds(domain);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户ID：" + domain.getCreUserId() + "执行查询账户余额出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}

	@RequestMapping(value = "/pageFindTrdOrderByCreUserId", method = RequestMethod.POST)
	public BackResult<PageDomain<TrdOrderDomain>> pageFindTrdOrderByCreUserId(HttpServletRequest request,
			HttpServletResponse response, String mobile, String token, Integer creUserId, Integer pageSize,
			Integer pageNum) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<PageDomain<TrdOrderDomain>> result = new BackResult<PageDomain<TrdOrderDomain>>();

		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
		}

		if (CommonUtils.isNotIngeter(pageSize)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("pageSize不能为空");
			return result;
		}

		if (CommonUtils.isNotIngeter(pageNum)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("pageNum不能为空");
			return result;
		}

		try {

			CreUserDomain user = findByMobile(mobile);

			if (null == user) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败，系统不存在该手机号码的用户");
				return result;
			}

			result = userAccountFeignService.pageFindTrdOrderByCreUserId(creUserId, pageSize, pageNum);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户手机号：" + mobile + "执行查询订单信息出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}
	
	/**
	 * 分页获取实号检测下载列表
	 * 
	 * @param request
	 * @param pageNo
	 * @param pageSize
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/getPageByMobile", method = RequestMethod.POST)
	public BackResult<PageDomain<CvsFilePathDomain>> getPageByUserId(HttpServletRequest request, int pageNo,
			int pageSize, String mobile) {
		logger.info("自助通手机号：" + mobile + "请求分页获取历史检测记录");

		BackResult<PageDomain<CvsFilePathDomain>> result = new BackResult<PageDomain<CvsFilePathDomain>>();


		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
		}

		if (CommonUtils.isNotIngeter(pageNo)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("页数不能为空");
			return result;
		}

		if (CommonUtils.isNotIngeter(pageSize)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("每页条数不能为空");
			return result;
		}

		try {
			CreUserDomain user = findByMobile(mobile);

			if (null == user) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败，系统不存在该手机号码的用户");
				return result;
			}

			result = creditProviderService.getPageByUserId(pageNo, pageSize, String.valueOf(user.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自助通手机号：" + mobile + "请求分页获取历史检测记录，出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}
		return result;
	}

}
