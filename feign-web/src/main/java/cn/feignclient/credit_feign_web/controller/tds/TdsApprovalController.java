package cn.feignclient.credit_feign_web.controller.tds;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsApprovalFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageAuto;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsCustomerViewDomain;

/**
 * 客服审核
 * @author ChuangLan
 *
 */
@RestController
@RequestMapping("/approval")
public class TdsApprovalController extends BaseController{

	private final static Logger logger = LoggerFactory.getLogger(TdsApprovalController.class);
	
	@Autowired
	private TdsApprovalFeignService tdsApprovalFeignService;
	
	
	/**
	 * 客户审核展示
	 * @param auto
	 * @return
	 */
	@RequestMapping(value = "/pageTdsApproval", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsCustomerViewDomain>> pageTdsApproval(PageAuto auto,String token){
		BackResult<PageDomain<TdsCustomerViewDomain>> result=new BackResult<PageDomain<TdsCustomerViewDomain>>();
		if (CommonUtils.isNotString(token)) {
			 return new BackResult<PageDomain<TdsCustomerViewDomain>>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
		
		result=tdsApprovalFeignService.pageTdsApproval(auto);
		logger.info("===客户审核展示===");
		return result;
	}
	
	
	/**
	 * 客户审核操作
	 * @param isAgree 0:同意：2：驳回
	 * @param userId  用户id
	 * @param reas    驳回原因
	 * @return
	 */
	@RequestMapping(value = "/isAgree", method = RequestMethod.POST)
	public BackResult<Integer> isAgree(Integer isAgree,Integer userId,String reas,String token){
		BackResult<Integer> result=new BackResult<Integer>();
		if (CommonUtils.isNotString(token)) {
			 return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
		if (CommonUtils.isNotIngeter(userId)) {
			 return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"用户id不能为空");
		}
		if (CommonUtils.isNotIngeter(isAgree)) {
			 return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"操作<isAgree>参数不能为空");
		}
		result=tdsApprovalFeignService.isAgree(isAgree, userId, reas);
		return result;
	}
	
	
	
	
}
