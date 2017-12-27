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
import cn.feignclient.credit_feign_web.service.tds.TdsUserRoleFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageAuto;
import main.java.cn.domain.tds.TdsUserDomain;
import main.java.cn.domain.tds.TdsUserRoleDomain;

@RestController
@RequestMapping("/userRole")
public class TdsUserRoleController extends BaseController {

	/**
	 * 检测token, 检测 是否登录， 登录的用户是否该有操作权限
	 * 
	 * 
	 */

	@Autowired
	private TdsUserRoleFeignService tdsUserRoleFeignService;

	@RequestMapping(value = "/loadById", method = RequestMethod.POST)
	public BackResult<TdsUserRoleDomain> loadById(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsUserRoleDomain> result = new BackResult<TdsUserRoleDomain>();
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
		result = tdsUserRoleFeignService.loadById(id);
		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BackResult<TdsUserRoleDomain> save(TdsUserRoleDomain tdsUserRoleDomain,HttpServletRequest request, HttpServletResponse response, String token) {
		BackResult<TdsUserRoleDomain> result = new BackResult<TdsUserRoleDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(tdsUserRoleDomain.getUserId())){
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户id不能为空");
			return result;
		}
		if (CommonUtils.isNotIngeter(tdsUserRoleDomain.getRoleId())){
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("角色id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsUserRoleFeignService.save(tdsUserRoleDomain);
		return result;
	}
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
    public BackResult<TdsUserRoleDomain>  update(TdsUserRoleDomain tdsUserRoleDomain,HttpServletRequest request, HttpServletResponse response, String token){
		BackResult<TdsUserRoleDomain> result = new BackResult<TdsUserRoleDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(tdsUserRoleDomain.getId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsUserRoleFeignService.update(tdsUserRoleDomain);
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
		result = tdsUserRoleFeignService.deleteById(id);
		return result;
	}
	
	@RequestMapping(value="selectAll")
	public BackResult<List<TdsUserRoleDomain>> selectAll(TdsUserRoleDomain tdsUserRoleDomain,HttpServletRequest request, HttpServletResponse response,String token){
		BackResult<List<TdsUserRoleDomain>> result = new BackResult<List<TdsUserRoleDomain>>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsUserRoleFeignService.selectAll(tdsUserRoleDomain);
		return result;
	}
	
	@RequestMapping(value="/upStatusById",method = RequestMethod.POST)
	public BackResult<Integer> upStatusById(TdsUserRoleDomain tdsUserRoleDomain,String token,String loginMobile){
		BackResult<Integer> result=new BackResult<Integer>();
		if (CommonUtils.isNotString(loginMobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("登录手机号码不能为空");
				return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		TdsUserDomain loginUser=this.getUserInfo(loginMobile);
		//tdsUserRoleDomain.getStatus 0：正常  1：禁用
		result=tdsUserRoleFeignService.upStatusById(tdsUserRoleDomain,loginUser.getId());
		return result;
	}
	
	
	@RequestMapping(value="/queryRoleIsStatus",method = RequestMethod.POST)
	public BackResult<List<PageAuto>> queryRoleIsStatus(PageAuto auto){
		return tdsUserRoleFeignService.queryRoleIsStatus(auto);
	}

}