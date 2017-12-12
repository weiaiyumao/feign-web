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
import cn.feignclient.credit_feign_web.service.tds.TdsModularFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsModularDomain;

@RestController
@RequestMapping("/modular")
public class TdsModularController extends BaseController {

	/**
	 * 检测token, 检测 是否登录， 登录的用户是否该有操作权限
	 * 
	 * 
	 */

	@Autowired
	private TdsModularFeignService tdsModularFeignService;

	@RequestMapping(value = "/loadById", method = RequestMethod.POST)
	public BackResult<TdsModularDomain> loadById(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsModularDomain> result = new BackResult<TdsModularDomain>();
		if (CommonUtils.isNotIngeter(id)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("模块id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsModularFeignService.loadById(id);
		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BackResult<TdsModularDomain> saveTdsFunction(TdsModularDomain tdsModularDomain,HttpServletRequest request, HttpServletResponse response, String token) {
		BackResult<TdsModularDomain> result = new BackResult<TdsModularDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(tdsModularDomain.getName())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("模块名称不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsModularFeignService.save(tdsModularDomain);
		return result;
	}
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
    public BackResult<TdsModularDomain>  update(TdsModularDomain tdsModularDomain,HttpServletRequest request, HttpServletResponse response, String token){
		BackResult<TdsModularDomain> result = new BackResult<TdsModularDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(tdsModularDomain.getId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("模块id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsModularFeignService.update(tdsModularDomain);
		return result;
	}
	
	@RequestMapping(value="/deleteById")
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id,HttpServletRequest request, HttpServletResponse response,String token){
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(id)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("模块id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsModularFeignService.deleteById(id);
		return result;
	}
	
	@RequestMapping(value="selectAll")
	public BackResult<List<TdsModularDomain>> selectAll(TdsModularDomain tdsModularDomain,HttpServletRequest request, HttpServletResponse response,String token){
		BackResult<List<TdsModularDomain>> result = new BackResult<List<TdsModularDomain>>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		if (CommonUtils.isNotIngeter(tdsModularDomain.getId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("模块id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsModularFeignService.selectAll(tdsModularDomain);
		return result;
	}

}
