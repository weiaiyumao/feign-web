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
import cn.feignclient.credit_feign_web.service.tds.TdsSuperFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsFunctionDomain;
import main.java.cn.domain.tds.UserRoleDepartmentViewDomain;

@RestController
@RequestMapping("/super")
public class TdsDepartmentController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(TdsDepartmentController.class);

	@Autowired
	private TdsSuperFeignService tdsSuperFeignService;

	/**
	 * 账号权限
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/pageUserRoleDepartmentView", method = RequestMethod.POST)
	public BackResult<PageDomain<UserRoleDepartmentViewDomain>>  pageUserRoleDepartmentView(String token,
			HttpServletRequest request, HttpServletResponse response,String departName,String roleName,String createTime,String contact,
			Integer currentPage,Integer numPerPage) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<PageDomain<UserRoleDepartmentViewDomain>> result = new BackResult<PageDomain<UserRoleDepartmentViewDomain>>();
		
		if (CommonUtils.isNotString(token)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("token不能为空");
				return result;
			}
			if(null==departName || "".equals(departName)){
				logger.info("查询全部部门");
			} 
			if(null==createTime || "".equals(roleName)){
				logger.info("查询全部注册时间");
			}
			if(null==contact || "".equals(contact)){
				logger.info("查询全部联系人");
			}
			if(null==roleName || "".equals(roleName)){
				logger.info("查询全部角色");
			}
	   	   result=tdsSuperFeignService.pageUserRoleDepartmentView(departName,roleName,createTime,contact,currentPage,numPerPage);
		   logger.info("账号权限查询分页列表");
    	return result;
	}
	
	
	@RequestMapping(value="funByUserId",method = RequestMethod.POST)
    public BackResult<List<TdsFunctionDomain>> funByuserId(Integer usreId,HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<List<TdsFunctionDomain>> result=new BackResult<List<TdsFunctionDomain>>();
		if (CommonUtils.isNotIngeter(usreId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("userid不能为空");
			return result;
		}
	 	result=tdsSuperFeignService.funByuserId(usreId);
		return result;
	}
	 
}
