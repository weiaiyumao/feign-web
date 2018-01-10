package cn.feignclient.credit_feign_web.controller.tds;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.tds.TdsMoneyApprovalFeignService;
import cn.feignclient.credit_feign_web.service.tds.TdsStateInfoFeignService;
import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsCommissionDomain;
import main.java.cn.domain.tds.TdsEnumDomain;
import main.java.cn.domain.tds.TdsSerualInfoDomain;

/**
 * 流水明细，佣金列表
 * @author ChuangLan
 *
 */
@RestController
@RequestMapping("/serualComm")
public class TdsSerualInfoAndComissionController {
	
	
	@Autowired
	private TdsMoneyApprovalFeignService tdsMoneyApprovalFeignService;
	
	@Autowired
	private TdsStateInfoFeignService tdsStateInfoFeignService;
	
	
	@RequestMapping(value = "/pageSerual", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsSerualInfoDomain>> pageTdsSerualInfo(TdsSerualInfoDomain domain,HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsMoneyApprovalFeignService.pageTdsSerualInfo(domain);
	}
	
	
	@RequestMapping(value = "/pageCommission", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsCommissionDomain>> pageCommission(TdsCommissionDomain domain,HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsMoneyApprovalFeignService.pageCommission(domain);
	}
	
	
	/**
	 * 获取项目列表信息
	 * @param codeName
	 * @return
	 */
	@RequestMapping(value = "/queryByTypeCode", method = RequestMethod.POST)
	public BackResult<List<TdsEnumDomain>> queryByTypeCode(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin","*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		//获取项目类型代码FLOW
		return tdsStateInfoFeignService.queryByTypeCode("FLOW");
		
	}
	
	

}
