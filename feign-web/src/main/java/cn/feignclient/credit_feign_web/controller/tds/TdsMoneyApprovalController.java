package cn.feignclient.credit_feign_web.controller.tds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsMoneyApprovalFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsApprovalOutDomain;
import main.java.cn.domain.tds.TdsApprovalOutQueryDomain;
import main.java.cn.domain.tds.TdsCommissionDomain;
import main.java.cn.domain.tds.TdsMoneyApprovalDomain;
import main.java.cn.domain.tds.TdsSerualInfoDomain;

@RestController
@RequestMapping("/moneyApproval")
public class TdsMoneyApprovalController extends BaseController {

	@Autowired
	private TdsMoneyApprovalFeignService tdsMoneyApprovalFeignService;

	/**
	 * 下单
	 * 
	 * @param domain
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/downAddOrder", method = RequestMethod.POST)
	public BackResult<Integer> downAddOrder(TdsMoneyApprovalDomain domain, HttpServletRequest request,
			HttpServletResponse response, String token) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsMoneyApprovalFeignService.downAddOrder(domain);
		return result;
	}
	
	
	
	
	
	/**
	 * 佣金分页查询
	 * 
	 * @param id
	 * @return obj
	 * @throws Exception 
	 */
	@RequestMapping(value = "/pageCommission", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsCommissionDomain>> pageCommission(TdsCommissionDomain domain,HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsMoneyApprovalFeignService.pageCommission(domain);
	}
	
	
	
	
	 /**
	   * 流水分页查询
	   * @param id
	   * @return  obj
	   */
	@RequestMapping(value="/pageTdsSerualInfo",method = RequestMethod.POST)
	public BackResult<PageDomain<TdsSerualInfoDomain>> pageTdsSerualInfo( TdsSerualInfoDomain domain,HttpServletRequest request,
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsMoneyApprovalFeignService.pageTdsSerualInfo(domain);
	}
	
	
	
	
	
	
	
	

	/**
	 * 进账分页查询
	 * 
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "/pageApprovalByUpStatusGo", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsMoneyApprovalDomain>> pageApprovalByUpStatusGo(TdsMoneyApprovalDomain domain,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsMoneyApprovalFeignService.pageApprovalByUpStatusGo(domain);
	}

	/**
	 * 出账分页查询
	 * 
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "/pageApprovalByUpStatusOut", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsApprovalOutDomain>> pageApprovalByUpStatusOut(TdsApprovalOutQueryDomain domain,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsMoneyApprovalFeignService.pageApprovalByUpStatusOut(domain);
	}
	
	/**
	 * 出账审核状态修改
	 * 
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "/updatePageApprovalByUpStatus", method = RequestMethod.POST)
	public BackResult<Integer> updatePageApprovalByUpStatus(String userId, String tdsCarryId, String status,String remarks,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsMoneyApprovalFeignService.updatePageApprovalByUpStatus(userId,tdsCarryId,status,remarks);
	}
	
	/**
	 * 退账分页查询
	 * 
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "/pageApprovalByUpStatusBack", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsMoneyApprovalDomain>> pageApprovalByUpStatusBack(TdsMoneyApprovalDomain domain,
			HttpServletRequest request, HttpServletResponse response,String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		return tdsMoneyApprovalFeignService.pageApprovalByUpStatusBack(domain);
	}

	/**
	 * 1进账审核操作
	 * 审核类型 1进账审核 2出账审核 3退款审核 : approval_type
	 * 
	 * @return obj
	 */
	@RequestMapping(value = "/approvalByUpStatusGo", method = RequestMethod.POST)
	public BackResult<Integer> approvalByUpStatusGo(TdsMoneyApprovalDomain domain, String appRemarks,
			HttpServletRequest request, HttpServletResponse response,String token) {
		BackResult<Integer> result=new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		return tdsMoneyApprovalFeignService.approvalByUpStatusGo(domain, appRemarks);
	}

	/**
	 * 2出账审核操作 
	 * 审核类型 1进账审核 2出账审核 3退款审核 : approval_type
	 * 
	 * @return obj
	 */
	@RequestMapping(value = "/approvalByUpStatusOut", method = RequestMethod.POST)
	public BackResult<Integer> approvalByUpStatusOut(TdsMoneyApprovalDomain domain, String appRemarks,
			HttpServletRequest request, HttpServletResponse response,String token) {
		BackResult<Integer> result=new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		return tdsMoneyApprovalFeignService.approvalByUpStatusOut(domain, appRemarks);
	}

	/**
	 * 3退款审核操作
	 *  审核类型 1进账审核 2出账审核 3退款审核 : approval_type
	 * 
	 * @return obj
	 */
	@RequestMapping(value = "/approvalByUpStatusBack", method = RequestMethod.POST)
	public BackResult<Integer> approvalByUpStatusBack(TdsMoneyApprovalDomain domain, String appRemarks,
			HttpServletRequest request, HttpServletResponse response,String token) {
		BackResult<Integer> result=new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		return tdsMoneyApprovalFeignService.approvalByUpStatusBack(domain, appRemarks);
	}
	
	
	
	

}
