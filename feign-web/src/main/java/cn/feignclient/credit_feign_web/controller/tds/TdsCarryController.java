package cn.feignclient.credit_feign_web.controller.tds;


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
import cn.feignclient.credit_feign_web.service.tds.TdsCarryFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsCarryDomain;


@RestController
@RequestMapping("/carry")
public class TdsCarryController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(TdsCarryController.class);

	@Autowired
	private TdsCarryFeignService tdsCarryFeignService;
	
	@RequestMapping(value = "/checkTdsCarry", method = RequestMethod.POST)
    public  BackResult<PageDomain<TdsCarryDomain>> checkTdsCarry(TdsCarryDomain domain,HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsCarryFeignService.checkTdsCarry(domain);
	}
	
	@RequestMapping(value = "/pageTdsCarry", method = RequestMethod.POST)
    public  BackResult<PageDomain<TdsCarryDomain>> pageTdsCarry(TdsCarryDomain domain,HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsCarryFeignService.pageTdsCarry(domain);
	}
	
	
	
	@RequestMapping(value = "/getCarryByUserId", method = RequestMethod.POST)
    public  BackResult<Map<String, Object>> getCarryByUserId(Integer userId,HttpServletRequest request, HttpServletResponse response,String token){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		return tdsCarryFeignService.getCarryByUserId(userId);
	}
	
	
	
	@RequestMapping(value = "/getSubCarry", method = RequestMethod.POST)
	public BackResult<Integer> getSubCarry(TdsCarryDomain domain,String type,HttpServletRequest request, HttpServletResponse response,String token){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		if (CommonUtils.isNotString(type)) {
			return new BackResult<>(ResultCode.RESULT_PARAM_EXCEPTIONS, "type不能为空(提款方式)");
		}
		logger.info("用户ID："+domain.getUserId()+" 提款佣金:"+domain.getCarrMoney()+"");
		return tdsCarryFeignService.getSubCarry(domain, type);
	}
	
}
