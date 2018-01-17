package cn.feignclient.credit_feign_web.controller.tds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsMoneyApprovalBackFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsMoneyApprovalBackDomain;

@RestController
@RequestMapping("/moneyApprovalBack")
public class TdsMoneyApprovalBackController extends BaseController {

	@Autowired
	private TdsMoneyApprovalBackFeignService tdsMoneyApprovalBackFeignService;
	
	
	/**
	 * 退款审核分页列表
	 * 
	 * @param id
	 * @return obj
	 * @throws Exception 
	 */
	@RequestMapping(value = "/pageApprovalBack", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsMoneyApprovalBackDomain>> pageApprovalBack(TdsMoneyApprovalBackDomain domain,HttpServletRequest request,
			HttpServletResponse response){
		
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(domain.getParentUserId())) {
			return new BackResult<>(ResultCode.RESULT_PARAM_EXCEPTIONS, "登录用户不能为空参数<ParentUserId>");
		}
		return tdsMoneyApprovalBackFeignService.pageApprovalBack(domain);
	}
	
	
	
	
	/**
	 * 退单
	 * 
	 * @param id
	 * @return obj
	 * @throws Exception 
	 */
	@RequestMapping(value = "/backOrderMoney", method = RequestMethod.POST)
	public BackResult<Integer> backOrderMoney(TdsMoneyApprovalBackDomain domain,HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsMoneyApprovalBackFeignService.backOrderMoney(domain);
	}
	
	
	

	/**
	 * 退单审核状态改变
	 * @param domain
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/approvalByUpStatusBack", method = RequestMethod.POST)
	BackResult<Integer> approvalByUpStatusBack(TdsMoneyApprovalBackDomain domain, String appRemarks,HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsMoneyApprovalBackFeignService.approvalByUpStatusBack(domain,appRemarks);
		
	}
	

}
