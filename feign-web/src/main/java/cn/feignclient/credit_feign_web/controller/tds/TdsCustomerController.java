package cn.feignclient.credit_feign_web.controller.tds;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import main.java.cn.enums.TdsEnum.CUSTOMERSTYPE;
import main.java.cn.enums.TdsEnum.ISBUND_PHONE;
import main.java.cn.enums.TdsEnum.USERSTATUS;
import main.java.cn.sms.util.AdminSmsUtil;

/**
 * 客服列表
 * 
 * @author ChuangLan
 *
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/customer")
public class TdsCustomerController extends BaseTdsController{

	private final static Logger logger = LoggerFactory.getLogger(TdsCustomerController.class);

	@Autowired
	private TdsCustomerFeignService tdsCustomerFeignService;

	@Autowired
	private TdsUserFeignService tdsUserFeignService;

	@Autowired
	private TdsUserRoleFeignService tdsUserRoleFeignService;

	@Value("${isSmsAdmin}")
	private boolean isSmsAdmin;
	
	/**
	 * 修改编辑客户信息
	 */
	@RequestMapping(value = "/updateCustomer", method = RequestMethod.POST)
	public BackResult<Integer> updateCustomer(HttpServletRequest request, HttpServletResponse response, String token,
			TdsCustomerViewDomain domain, Integer loginUserId, String passWord) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return BackResult.error("token不能为空");
		}
		// 获取登录用户信息，增加修改人
		return tdsCustomerFeignService.updateCustomer(domain, loginUserId, passWord);
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
			TdsCustomerViewDomain domain, Integer loginUserId, String passWord,String moblie) {
		
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		if (CommonUtils.isNotString(token)) {
			return BackResult.error("token不能为空");
		}
		
		if (CommonUtils.isNotString(domain.getComName())) {
			return BackResult.error("公司名不能为空");
		}
		
		if (CommonUtils.isNotString(domain.getUserName())) {
			return BackResult.error("客户名称不能为空");
		}
			
//		if(!this.isAdminLogin(moblie, token)){
//			return BackResult.error("用户为登录");
//		}
		return tdsCustomerFeignService.addTdsCustomer(domain, loginUserId, "a123456");

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
			HttpServletResponse response,String customerType) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		if (CommonUtils.isNotString(domain.getParentUserId())) {
			return BackResult.error("参数<parentUserId>不能为空");
		}
		
		
		if (CommonUtils.isNotString(customerType)) {
			return BackResult.error("类型值不能为空");
		}
		
		if(customerType.equals(USERSTATUS.PASST.getCode())){
			//正常
			domain.setStatus(USERSTATUS.PASST.getCode());
		}else{
			//申请中
			domain.setStatus(USERSTATUS.PLEASE.getCode());
		}

		return tdsCustomerFeignService.pageTdsCustomer(domain);

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
		
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(userId)) {
			return BackResult.error("userId不能为空");
		}
		return tdsCustomerFeignService.loadByIdView(userId);

	}

	
	
	/**
	 * 转让
	 * @param auto
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/attorn", method = RequestMethod.POST)
	public BackResult<Integer> attorn(TdsAttornLogDomain domain, HttpServletRequest request,
			HttpServletResponse response, Integer loginUserId, String token) {
		
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		if (CommonUtils.isNotString(token)) {
			return BackResult.error("token不能为空");
		}
		if (CommonUtils.isNotIngeter(loginUserId)) {
			return BackResult.error("登录用户loginUserId不能为空");
		}
		
		domain.setCreater(loginUserId);// 操作人
		
		return tdsCustomerFeignService.attorn(domain);

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
		
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		if (CommonUtils.isNotString(token)) {
			return BackResult.error("token不能为空");
		}
		
		return tdsCustomerFeignService.updatePrice(domain);

	}

	/**
	 * 根据列表用户id查询改价信息
	 */
	@RequestMapping(value = "/selectAllByUserId", method = RequestMethod.POST)
	public BackResult<List<TdsUserDiscountDomain>> selectAllByUserId(HttpServletRequest request,
			HttpServletResponse response, Integer userId) {
		
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		return tdsCustomerFeignService.selectAllByUserId(userId);

	}

	/**
	 * 新增改价信息
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
			return BackResult.error("token不能为空");
		}
		return tdsCustomerFeignService.addTdsUserDiscount(domain);
	}

	/**
	 * 删除改价信息
	 * @param id
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/deleteById", method = RequestMethod.POST)
	public BackResult<Integer> deleteById(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		
		response.setContentType("text/json;charset=UTF-8");
		
		if (CommonUtils.isNotString(token)) {
			return BackResult.error("token不能为空");
		}
		if (CommonUtils.isNotIngeter(id)) {
			return BackResult.error("id不能为空");
		}
		
		return tdsCustomerFeignService.deleteById(id);
	}
	
	
	/**
	 * 客户审核操作
	 * @param isAgree 0:同意：2：驳回
	 * @param userId  用户id
	 * @param reas    驳回原因
	 * @return
	 */
	@RequestMapping(value = "/isAgree", method = RequestMethod.POST)
	public BackResult<Integer> isAgree(String isAgree,Integer userId,String reas,String token,String moblie){
		BackResult<Integer> result=new BackResult<Integer>();
		if (CommonUtils.isNotString(token)) {
			 return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
		if (CommonUtils.isNotIngeter(userId)) {
			 return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"用户id不能为空");
		}
		
		result=tdsCustomerFeignService.isAgree(isAgree, userId, reas);
		 
		//短信发送
		if(result.getResultCode().equals(ResultCode.RESULT_SUCCEED) && isSmsAdmin){
		
			if(isAgree.equals(CUSTOMERSTYPE.NORMAL.getCode()))
				AdminSmsUtil.getInstance().sendAdminSmsByCustomer(moblie,true);  //审核通过，短信发送
			else
				AdminSmsUtil.getInstance().sendAdminSmsByCustomer(moblie,false); //审核驳回，短信发送
			
		}
	    
		return result;
	}

}
