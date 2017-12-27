package cn.feignclient.credit_feign_web.controller.tds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsCustomerFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageAuto;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsCustomerViewDomain;

/**
 * 客服列表
 * 
 * @author ChuangLan
 *
 */
@RestController
@RequestMapping("/customer")
public class TdsCustomerController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(TdsCustomerController.class);

	@Autowired
	private TdsCustomerFeignService tdsCustomerFeignService;

	/**
	 * 修改编辑客户信息
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public BackResult<Integer> update(HttpServletRequest request, HttpServletResponse response, String token,
			Integer loginUserId, PageAuto auto, Integer upUserId,Integer[] arrRoles ) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		if (CommonUtils.isNotIngeter(upUserId)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "更新用户id不能为空-参数：upUserId");
		}
		if (CommonUtils.isNotIngeter(loginUserId)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "登录者用户id不能为空-参数：loginUserId");
		}
		if (CommonUtils.isNotString(auto.getPassWord())) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "密码不能为空-参数：passWord");
		}
		if (CommonUtils.isNotString(auto.getComUrl())) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "公司网址不能空-参数：comUrl");
		}

		// 获取登录用户信息，增加修改人
		logger.info("用户ID:" + loginUserId + "===修改客户列表操作,修改信息用户id:" + upUserId + "===");
		result = tdsCustomerFeignService.update(loginUserId, auto, upUserId, arrRoles);

		return result;
	}

	/**
	 * 分页显示
	 * 
	 * @param auto
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/pageTdsCustomer", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsCustomerViewDomain>> pageTdsCustomer(PageAuto auto, HttpServletRequest request,
			HttpServletResponse response) {
		BackResult<PageDomain<TdsCustomerViewDomain>> result = new BackResult<PageDomain<TdsCustomerViewDomain>>();

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		logger.info("===客户列表显示===");
		result = tdsCustomerFeignService.pageTdsCustomer(auto);
		return result;

	}

	/**
	 * 转让
	 * 
	 * @param auto
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/attorn", method = RequestMethod.POST)
	public BackResult<PageAuto> attorn(PageAuto auto, HttpServletRequest request, HttpServletResponse response) {
		BackResult<PageAuto> result = new BackResult<PageAuto>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		logger.info("===客户列表显示===");
		result = tdsCustomerFeignService.attorn(auto);
		return result;

	}

	/**
	 * 客服列表新增
	 * 
	 * @param auto
	 * @param loginUserId
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/addTdsCustomer", method = RequestMethod.POST)
	public BackResult<Integer> addTdsCustomer(PageAuto auto,Integer loginUserId,Integer[] arrRoles,HttpServletRequest request,
			HttpServletResponse response, String token) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		logger.info("====用户id:======" + loginUserId + " 新增客户");
		result = tdsCustomerFeignService.addTdsCustomer(auto, loginUserId,arrRoles);
		return result;

	}

}
