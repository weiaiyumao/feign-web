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
import cn.feignclient.credit_feign_web.service.tds.TdsRoleFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.BasePageParam;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsFunctionDomain;
import main.java.cn.domain.tds.TdsRoleDomain;

@RestController
@RequestMapping("/role")
public class TdsRoleController extends BaseController {

	/**
	 * 检测token, 检测 是否登录， 登录的用户是否该有操作权限
	 * 
	 * 
	 */

	@Autowired
	private TdsRoleFeignService tdsRoleFeignService;

	
	/**
	 * 根据id查询角色信息
	 * @param id
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/loadById", method = RequestMethod.POST)
	public BackResult<TdsRoleDomain> loadById(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsRoleDomain> result = new BackResult<TdsRoleDomain>();
		if (CommonUtils.isNotIngeter(id)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("角色id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsRoleFeignService.loadById(id);
		return result;
	}
    
	 /**
	  * 新增角色
	  * @param tdsRoleDomain
	  * @param request
	  * @param response
	  * @param token
	  * @return
	  */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BackResult<TdsRoleDomain> saveTdsFunction(TdsRoleDomain tdsRoleDomain,HttpServletRequest request, HttpServletResponse response, String token) {
		BackResult<TdsRoleDomain> result = new BackResult<TdsRoleDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(tdsRoleDomain.getRoleName())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("角色名称不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsRoleFeignService.save(tdsRoleDomain);
		return result;
	}
	
	
	/**
	 * 修改角色
	 * @param tdsRoleDomain
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/update",method = RequestMethod.POST)
    public BackResult<TdsRoleDomain>  update(TdsRoleDomain tdsRoleDomain,HttpServletRequest request, HttpServletResponse response, String token){
		BackResult<TdsRoleDomain> result = new BackResult<TdsRoleDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(tdsRoleDomain.getId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("角色id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsRoleFeignService.update(tdsRoleDomain);
		return result;
	}
	
	
	/**
	 * 删除角色
	 * @param id
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/deleteById")
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id,HttpServletRequest request, HttpServletResponse response,String token){
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(id)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("角色id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		result = tdsRoleFeignService.deleteById(id);
		return result;
	}
	
	
	/**
	 * 查询所有角色名称列表
	 * @param tdsRoleDomain
	 * @param request
	 * @param token
	 * @return
	 */
	@RequestMapping(value="selectAll")
	public BackResult<List<TdsRoleDomain>> selectAll(TdsRoleDomain tdsRoleDomain,HttpServletRequest request, HttpServletResponse response){
		BackResult<List<TdsRoleDomain>> result = new BackResult<List<TdsRoleDomain>>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		result = tdsRoleFeignService.selectAll(tdsRoleDomain);
		return result;
	}
	
	
	
	@RequestMapping(value ="/loadingBydRoleId", method = RequestMethod.POST)
	public BackResult<List<TdsFunctionDomain>> loadingBydRoleId(Integer roleId,HttpServletRequest request, HttpServletResponse response){
		BackResult<List<TdsFunctionDomain>> result = new BackResult<List<TdsFunctionDomain>>();
		if (CommonUtils.isNotIngeter(roleId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("roleId不能为空");
			return result;
		}
		result = tdsRoleFeignService.loadingBydRoleId(roleId);
		return result;
	}
	
	
	@RequestMapping(value ="/pageByRole", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsRoleDomain>> pageByRole(String roleName,HttpServletRequest request, HttpServletResponse response,BasePageParam basePageParam){
		BackResult<PageDomain<TdsRoleDomain>> result = new BackResult<PageDomain<TdsRoleDomain>>();
		result = tdsRoleFeignService.pageByRole(roleName,basePageParam);
		return result;
	}

}
