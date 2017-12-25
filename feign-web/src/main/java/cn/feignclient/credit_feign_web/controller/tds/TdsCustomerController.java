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
import main.java.cn.domain.tds.TdsUserDomain;

/**
 * 客服列表
 * @author ChuangLan
 *
 */
@RestController
@RequestMapping("/customer")
public class TdsCustomerController extends BaseController{

	private final static Logger logger = LoggerFactory.getLogger(TdsCustomerController.class);
	
	@Autowired
	private TdsCustomerFeignService  tdsCustomerFeignService;
	
	/**
	 * 修改编辑客户信息
	 * @param tdsUserDomain  用户信息
	 * @param departmentId  部门id
	 * @param comUrl  网址
	 * @return
	 */
	@RequestMapping(value="/update",method = RequestMethod.POST)
    public BackResult<TdsUserDomain>  update(TdsUserDomain tdsUserDomain,Integer departmentId,String comUrl, 
    		HttpServletRequest request, HttpServletResponse response, String token,String loginMobile){
		BackResult<TdsUserDomain> result = new BackResult<TdsUserDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			 return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
		if (CommonUtils.isNotString(comUrl)) {
			 return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"网址不能为空");
		}
		//获取登录用户信息，增加修改人
		TdsUserDomain loginUser=this.getUserInfo(loginMobile);
		logger.info("用户ID:"+loginUser.getId()+"===修改客户列表操作,修改信息用户id:"+tdsUserDomain.getId()+"===");
		tdsUserDomain.setUpdater(loginUser.getId());  //修改人
		result = tdsCustomerFeignService.update(tdsUserDomain, departmentId, comUrl);
		return result;
	}
	
	
	/**
	 * 分页显示
	 * @param auto
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/pageTdsCustomer", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsCustomerViewDomain>> pageTdsCustomer(PageAuto auto,HttpServletRequest request, HttpServletResponse response){
		BackResult<PageDomain<TdsCustomerViewDomain>> result = new BackResult<PageDomain<TdsCustomerViewDomain>>();
		
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		logger.info("===客户列表显示===");
		result = tdsCustomerFeignService.pageTdsCustomer(auto);
		return result;
		
	}
	
	
	/**
	 * 转让
	 * @param auto
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/attorn", method = RequestMethod.POST)
	public BackResult<PageAuto> attorn(PageAuto auto,HttpServletRequest request, HttpServletResponse response){
		BackResult<PageAuto> result = new BackResult<PageAuto>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		logger.info("===客户列表显示===");
		result = tdsCustomerFeignService.attorn(auto);
		return result;
		
	}
	
	
	
	
	
}
