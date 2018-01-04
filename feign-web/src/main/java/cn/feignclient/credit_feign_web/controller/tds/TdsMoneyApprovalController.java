package cn.feignclient.credit_feign_web.controller.tds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.tds.TdsMoneyApprovalFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsMoneyApprovalDomain;

@RestController
@RequestMapping("/moneyApproval")
public class TdsMoneyApprovalController {
	
	@Autowired
	private TdsMoneyApprovalFeignService tdsMoneyApprovalFeignService;
	
	
	
	/**
	 * 下单
	 * @param domain
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/downAddOrder", method = RequestMethod.POST)
	public BackResult<Integer> downAddOrder(TdsMoneyApprovalDomain domain,HttpServletRequest request, HttpServletResponse response,String token){
		BackResult<Integer> result=new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result=tdsMoneyApprovalFeignService.downAddOrder(domain);
		return result;
	}
	
	
	

}
