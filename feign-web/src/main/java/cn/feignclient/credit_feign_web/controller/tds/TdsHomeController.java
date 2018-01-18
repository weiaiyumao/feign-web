package cn.feignclient.credit_feign_web.controller.tds;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.tds.TdsUserLoginFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;

@RestController
@RequestMapping("/home")
public class TdsHomeController {

	@Autowired
	private TdsUserLoginFeignService tdsUserLoginFeignService;
	

	/**
	 * 统计客户数量，累计充值金额，剩余佣金
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/countByUserId", method = RequestMethod.POST)
	BackResult<Map<String, Object>> countByUserId(Integer userId, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<Map<String, Object>> result = new BackResult<Map<String, Object>>();
		if (CommonUtils.isNotIngeter(userId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户userId不能为空");
			return result;
		}
		result = tdsUserLoginFeignService.countByUserId(userId);
		return result;
	}
    
	
	
	/**
	 * 获取对应的产品剩余数量
	 * @param userId
	 * @param pnameId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getAccByNumber", method = RequestMethod.POST)
	public BackResult<Integer> getAccByNumber(Integer userId, Integer pnameId, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<Integer> result = new BackResult<Integer>();
		if (CommonUtils.isNotIngeter(userId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户userId不能为空");
			return result;
		}
		result = tdsUserLoginFeignService.getAccByNumber(userId, pnameId);
		return result;
	}

}
