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
import cn.feignclient.credit_feign_web.service.tds.TdsFunctionRoleFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsFunctionRoleDomain;

@RestController
@RequestMapping("/functionRole")
public class TdsFunctionRoleController extends BaseController {

	/**
	 * 检测token, 检测 是否登录， 登录的用户是否该有操作权限
	 * 
	 * 
	 */

	@Autowired
	private TdsFunctionRoleFeignService tdsFunctionRoleFeignService;

	@RequestMapping(value = "/loadById", method = RequestMethod.POST)
	public BackResult<TdsFunctionRoleDomain> loadById(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsFunctionRoleDomain> result = new BackResult<TdsFunctionRoleDomain>();
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
		result = tdsFunctionRoleFeignService.loadById(id);
		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BackResult<TdsFunctionRoleDomain> save(TdsFunctionRoleDomain tdsFunctionRoleDomain,HttpServletRequest request, HttpServletResponse response, String token) {
		BackResult<TdsFunctionRoleDomain> result = new BackResult<TdsFunctionRoleDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(tdsFunctionRoleDomain.getFunId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("功能id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(tdsFunctionRoleDomain.getRoleId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("角色id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsFunctionRoleFeignService.save(tdsFunctionRoleDomain);
		return result;
	}
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
    public BackResult<TdsFunctionRoleDomain>  update(TdsFunctionRoleDomain tdsFunctionRoleDomain,HttpServletRequest request, HttpServletResponse response, String token){
		BackResult<TdsFunctionRoleDomain> result = new BackResult<TdsFunctionRoleDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(tdsFunctionRoleDomain.getId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsFunctionRoleFeignService.update(tdsFunctionRoleDomain);
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
		result = tdsFunctionRoleFeignService.deleteById(id);
		return result;
	}
	
	@RequestMapping(value="selectAll")
	public BackResult<List<TdsFunctionRoleDomain>> selectAll(TdsFunctionRoleDomain tdsFunctionRoleDomain,HttpServletRequest request, HttpServletResponse response,String token){
		BackResult<List<TdsFunctionRoleDomain>> result = new BackResult<List<TdsFunctionRoleDomain>>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsFunctionRoleFeignService.selectAll(tdsFunctionRoleDomain);
		return result;
	}

}
