package cn.feignclient.credit_feign_web.controller.tds;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsFunctionFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsFunctionDomain;

@RestController
@RequestMapping("/function")
public class TdsFunctionController extends BaseController {

	/**
	 * 检测token, 检测 是否登录， 登录的用户是否该有操作权限
	 * 
	 * 
	 */

	@Autowired
	private TdsFunctionFeignService tdsFunctionFeignService;

	@RequestMapping(value = "/loadById", method = RequestMethod.POST)
	public BackResult<TdsFunctionDomain> loadById(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsFunctionDomain> result = new BackResult<TdsFunctionDomain>();
		if (CommonUtils.isNotIngeter(id)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsFunctionFeignService.loadById(id);
		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BackResult<TdsFunctionDomain> saveTdsFunction(TdsFunctionDomain tdsFunctionDomain,HttpServletRequest request, HttpServletResponse response, String token) {
		BackResult<TdsFunctionDomain> result = new BackResult<TdsFunctionDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(tdsFunctionDomain.getName())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("功能名称不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsFunctionFeignService.save(tdsFunctionDomain);
		return result;
	}
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
    public BackResult<TdsFunctionDomain>  update(TdsFunctionDomain tdsFunctionDomain,HttpServletRequest request, HttpServletResponse response, String token){
		BackResult<TdsFunctionDomain> result = new BackResult<TdsFunctionDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(tdsFunctionDomain.getId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsFunctionFeignService.update(tdsFunctionDomain);
		return result;
	}
	
	@RequestMapping(value="/deleteById")
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id,HttpServletRequest request, HttpServletResponse response,String token){
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(id)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsFunctionFeignService.deleteById(id);
		return result;
	}
	
	@RequestMapping(value="selectAll")
	public BackResult<List<TdsFunctionDomain>> selectAll(TdsFunctionDomain tdsFunctionDomain,HttpServletRequest request, HttpServletResponse response,String token){
		BackResult<List<TdsFunctionDomain>> result = new BackResult<List<TdsFunctionDomain>>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsFunctionFeignService.selectAll(tdsFunctionDomain);
		return result;
	}

}
