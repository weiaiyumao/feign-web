package cn.feignclient.credit_feign_web.controller.selfHelpTong;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.CreditProviderService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;
import main.java.cn.domain.CvsFilePathDomain;
import main.java.cn.domain.RunTestDomian;
import main.java.cn.domain.page.PageDomain;

/**
 * 自助通调用 实号检测接口
 * 
 * @author ChuangLan
 *
 */
@RestController
@RequestMapping("/creditBus")
public class CreditBusController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(CreditBusController.class);

	@Autowired
	CreditProviderService creditProviderService;

	@RequestMapping(value = "/theTest", method = RequestMethod.POST)
	public BackResult<RunTestDomian> runTheTest(HttpServletRequest request, String fileUrl, String mobile,
			String source,String startLine,String type) {

		logger.info("自助通手机号：" + mobile + "请求进行实号检测");
		BackResult<RunTestDomian> result = new BackResult<RunTestDomian>();

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

		if (CommonUtils.isNotString(fileUrl)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("文件地址不能为空");
			return result;
		}
		
		if (CommonUtils.isNotString(source)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("来源不能为空");
			return result;
		}
		
		if (CommonUtils.isNotString(startLine)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("开始条数不能为空");
			return result;
		}
		
		if (CommonUtils.isNotString(type)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("类型不能为空：1检测 2查询检测结果");
			return result;
		}

		try {

			CreUserDomain user = findByMobile(mobile);

			if (null == user) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败，系统不存在该手机号码的用户");
				return result;
			}

			result = creditProviderService.theTest(fileUrl, String.valueOf(user.getId()), source, mobile, startLine,type);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自助通手机号：" + mobile + "请求进行实号检测，出现系统异常" + e.getMessage());
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

	/**
	 * 根据id删除检测记录
	 * 
	 * @param request
	 * @param ids
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/deleteCvsByIds", method = RequestMethod.POST)
	public BackResult<Boolean> deleteCvsByIds(HttpServletRequest request, String ids, String mobile) {

		logger.info("自助通手机号：" + mobile + "请求删除历史检测记录");

		BackResult<Boolean> result = new BackResult<Boolean>();

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
		
		if (CommonUtils.isNotString(ids)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("删除记录的ids不能为空");
			return result;
		}

		try {

			CreUserDomain user = findByMobile(mobile);

			if (null == user) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败，系统不存在该手机号码的用户");
				return result;
			}

			result = creditProviderService.deleteCvsByIds(ids, String.valueOf(user.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自助通手机号：" + mobile + "请求删除历史检测记录，出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}

}
