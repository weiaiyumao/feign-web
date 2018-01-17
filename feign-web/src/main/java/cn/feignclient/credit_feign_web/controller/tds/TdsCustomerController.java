package cn.feignclient.credit_feign_web.controller.tds;

import java.util.List;
import java.util.Map;

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
import cn.feignclient.credit_feign_web.service.tds.TdsUserFeignService;
import cn.feignclient.credit_feign_web.service.tds.TdsUserRoleFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsAttornLogDomain;
import main.java.cn.domain.tds.TdsCustomerViewDomain;
import main.java.cn.domain.tds.TdsUserDiscountDomain;
import main.java.cn.domain.tds.TdsUserDomain;

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

	@Autowired
	private TdsUserFeignService tdsUserFeignService;

	@Autowired
	private TdsUserRoleFeignService tdsUserRoleFeignService;

	/**
	 * 修改编辑客户信息
	 */
	@RequestMapping(value = "/updateCustomer", method = RequestMethod.POST)
	public BackResult<Integer> updateCustomer(HttpServletRequest request, HttpServletResponse response, String token,
			TdsCustomerViewDomain domain, Integer loginUserId, String passWord, String comName, String comUrl) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		// 获取登录用户信息，增加修改人
		domain.setComName(comName);
		domain.setCom_url(comUrl);
		result = tdsCustomerFeignService.updateCustomer(domain, loginUserId, passWord);
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
	public BackResult<Integer> addTdsCustomer(HttpServletRequest request, HttpServletResponse response, String token,
			TdsCustomerViewDomain domain, Integer loginUserId, String passWord, String comName, String comUrl) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		domain.setComName(comName);
		domain.setCom_url(comUrl);
		result = tdsCustomerFeignService.addTdsCustomer(domain, loginUserId, passWord);
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
	public BackResult<PageDomain<TdsCustomerViewDomain>> pageTdsCustomer(TdsCustomerViewDomain domain, HttpServletRequest request,
			HttpServletResponse response) {
		BackResult<PageDomain<TdsCustomerViewDomain>> result = new BackResult<PageDomain<TdsCustomerViewDomain>>();

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		if (CommonUtils.isNotString(domain.getParentUserId())) {
			return new BackResult<>(ResultCode.RESULT_PARAM_EXCEPTIONS, "登录者id不能为空参数<parentUserId>");
		}

		result = tdsCustomerFeignService.pageTdsCustomer(domain);
		return result;

	}

	/**
	 * 根据id查询客户列信息信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loadById", method = RequestMethod.POST)
	public BackResult<TdsCustomerViewDomain> loadById(Integer userId, HttpServletRequest request,
			HttpServletResponse response) {
		BackResult<TdsCustomerViewDomain> result = new BackResult<TdsCustomerViewDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(userId)) {
			return new BackResult<>(ResultCode.RESULT_PARAM_EXCEPTIONS, "userId不能为空");
		}
		result = tdsCustomerFeignService.loadByIdView(userId);
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
	public BackResult<Integer> attorn(TdsAttornLogDomain domain, HttpServletRequest request,
			HttpServletResponse response, Integer loginUserId, String token) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		if (CommonUtils.isNotIngeter(loginUserId)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "操作用户id不能为空");
		}
		domain.setCreater(loginUserId);// 操作人
		result = tdsCustomerFeignService.attorn(domain);
		logger.info("用户id：" + loginUserId + "===转让操作===");
		return result;

	}

	/**
	 * 获取转出的用户信息
	 * 
	 * @param auto
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/attornByUserId", method = RequestMethod.POST)
	public BackResult<TdsUserDomain> attornByUserId(HttpServletRequest request, HttpServletResponse response,
			Integer userId) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsUserFeignService.loadById(userId);
	}

	
	
	/**
	 * 搜索转让人
	 * @param request
	 * @param response
	 * @param contact
	 *            联系人
	 * @return
	 */
	@RequestMapping(value = "/queryUserByRoleName", method = RequestMethod.POST)
	public BackResult<List<Map<String,String>>> queryUserByRoleName(HttpServletRequest request, HttpServletResponse response,
			String contact) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsUserRoleFeignService.queryUserByRoleName(contact); //
	}

	/**
	 * 编辑改价
	 */
	@RequestMapping(value = "/updatePrice", method = RequestMethod.POST)
	public BackResult<Integer> updatePrice(HttpServletRequest request, HttpServletResponse response, String token,
			TdsUserDiscountDomain domain) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		logger.info("====用户id:" + domain.getUpdater() + "改价======");
		result = tdsCustomerFeignService.updatePrice(domain);
		return result;

	}

	/**
	 * 根据列表用户id查询改价信息
	 */
	@RequestMapping(value = "/selectAllByUserId", method = RequestMethod.POST)
	public BackResult<List<TdsUserDiscountDomain>> selectAllByUserId(HttpServletRequest request,
			HttpServletResponse response, Integer userId) {
		BackResult<List<TdsUserDiscountDomain>> result = new BackResult<List<TdsUserDiscountDomain>>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		result = tdsCustomerFeignService.selectAllByUserId(userId);
		return result;

	}

	/**
	 * 新增改价信息
	 * 
	 * @param domain
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/addTdsUserDiscount", method = RequestMethod.POST)
	public BackResult<Integer> addTdsUserDiscount(TdsUserDiscountDomain domain, HttpServletRequest request,
			HttpServletResponse response, String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		return tdsCustomerFeignService.addTdsUserDiscount(domain);
	}

	/**
	 * 删除改价信息
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/deleteById", method = RequestMethod.POST)
	public BackResult<Integer> deleteById(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		if (CommonUtils.isNotIngeter(id)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "id不能为空");
		}
		return tdsCustomerFeignService.deleteById(id);
	}

}
