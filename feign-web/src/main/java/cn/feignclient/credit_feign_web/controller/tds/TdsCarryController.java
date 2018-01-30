package cn.feignclient.credit_feign_web.controller.tds;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsCarryFeignService;
import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsCarryDomain;


@RestController
@RequestMapping("/carry")
public class TdsCarryController extends BaseController {

	//private final static Logger logger = LoggerFactory.getLogger(TdsCarryController.class);

	@Autowired
	private TdsCarryFeignService tdsCarryFeignService;
	
	@RequestMapping(value = "/pageTdsCarry", method = RequestMethod.POST)
    public  BackResult<PageDomain<TdsCarryDomain>> pageTdsCarry(TdsCarryDomain domain,HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsCarryFeignService.pageTdsCarry(domain);
	}
	
	
	
	@RequestMapping(value = "/getCarryByUserId", method = RequestMethod.POST)
    public  BackResult<Map<String, Object>> getCarryByUserId(Integer userId,HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsCarryFeignService.getCarryByUserId(userId);
	}
	
}
