package cn.feignclient.credit_feign_web.controller.tds;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsDeparTmentFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageAuto;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsDepartmentDomain;
import main.java.cn.domain.tds.TdsFunctionDomain;
import main.java.cn.domain.tds.TdsUserDomain;
import main.java.cn.domain.tds.UserRoleDepartmentViewDomain;

@RestController
@RequestMapping("/super")
public class TdsDepartmentController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(TdsDepartmentController.class);

	@Autowired
	private TdsDeparTmentFeignService tdsDeparTmentFeignService;

	/**
	 * 账号权限-配置列表
	 * 
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/pageUserRoleDepartmentView", method = RequestMethod.POST)
	public BackResult<PageDomain<UserRoleDepartmentViewDomain>> pageUserRoleDepartmentView(String token,
			HttpServletRequest request, HttpServletResponse response,PageAuto auto) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<PageDomain<UserRoleDepartmentViewDomain>> result = new BackResult<PageDomain<UserRoleDepartmentViewDomain>>();
		result = tdsDeparTmentFeignService.pageUserRoleDepartmentView(auto);
		return result;
	}

	@RequestMapping(value = "funByUserId", method = RequestMethod.POST)
	public BackResult<List<TdsFunctionDomain>> funByuserId(Integer userId, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<List<TdsFunctionDomain>> result = new BackResult<List<TdsFunctionDomain>>();
		if (CommonUtils.isNotIngeter(userId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("userid不能为空");
			return result;
		}
		result = tdsDeparTmentFeignService.funByuserId(userId);
		return result;
	}

	/**
	 * 查询所有部门列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selectAll", method = RequestMethod.POST)
	public BackResult<List<TdsDepartmentDomain>> selectAll(TdsDepartmentDomain domain, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsDeparTmentFeignService.selectAll(domain);
	}

	/**
	 * 添加账号  模块功能
	 * 
	 * @param tdsUserDomain
	 *            用户信息
	 * @param departmentId
	 *            部门id
	 * @param positionId
	 *            职位id
	 * @param comId
	 *            公司id
	 * @param checkboxRole
	 *            角色
	 * @return
	 */
	@RequestMapping(value = "/addUserConfig",method = RequestMethod.POST)
	public BackResult<Integer> addUserConfig(String token, Integer departmentId, Integer positionId, Integer comId,
			Integer[] arrRoles, HttpServletRequest request, HttpServletResponse response, String loginMobile,
			String name, String passWord, String phone) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<Integer> result = new BackResult<Integer>();
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		if (CommonUtils.isNotString(loginMobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("登录者手机号不能为空");
			return result;
		}
		if (CommonUtils.isNotString(name)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("姓名不能为空");
			return result;
		}
		if (CommonUtils.isNotString(passWord)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("密码不能为空");
			return result;
		}
		
		if (CommonUtils.isNotString(phone)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("注册手机号不能为空");
			return result;
		}
		if (CommonUtils.isNotIngeter(departmentId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("部门id不能为空");
			return result;
		}
//		if (CommonUtils.isNotIngeter(positionId)) {
//			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
//			result.setResultMsg("职位id不能为空");
//			return result;
//		}
		if (CommonUtils.isNotIngeter(comId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("公司id不能为空");
			return result;
		}
		if (arrRoles.length < 1) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("角色分配不能为空");
			return result;
		}
		TdsUserDomain loginUser = this.getUserInfo(loginMobile); // 获取登录用户信息
		result = tdsDeparTmentFeignService.addUserConfig(name, passWord, phone, departmentId, positionId, comId,
				arrRoles, loginUser.getId());
		return result;
	}

	/**
	 * 自定义角色权限
	 * 
	 * @param token
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addCustomPermissions",method = RequestMethod.POST)
	public BackResult<Integer> addCustomPermissions(String token, HttpServletRequest request,
			HttpServletResponse response, String soleName, String loginMobile, Integer[] arrfuns) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<Integer> result = new BackResult<Integer>();

		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		
		TdsUserDomain loginUser = this.getUserInfo(loginMobile); // 获取登录用户信息
		result = tdsDeparTmentFeignService.addCustomPermissions(soleName, loginUser.getId(), arrfuns);
		return result;

	}
	
	
	/**
	 * 自定义功能
	 * @return
	 */
	@RequestMapping(value = "/addFun",method = RequestMethod.POST)
	public BackResult<Integer> addFun(String token, HttpServletRequest request,
			HttpServletResponse response,Integer loginUserId,TdsFunctionDomain domain) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<Integer> result = new BackResult<Integer>();

		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		if(CommonUtils.isNotIngeter(loginUserId)){
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("loginUserId登录用户id不能为空");
			return result;
		}
		if(CommonUtils.isNotString(domain.getName())){
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("权限名称不能为空");
			return result;
		}
		if(CommonUtils.isNotString(domain.getUrl())){
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("权限url不能为空");
			return result;
		}
		domain.setCreater(loginUserId); //创建人
		result = tdsDeparTmentFeignService.addFun(domain);
		return result;

	}

}
