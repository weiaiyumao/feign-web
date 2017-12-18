package cn.feignclient.credit_feign_web.controller.tds;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsUserFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsUserDomain;

@RestController
@RequestMapping("/tdsUser")
public class TdsUserController extends BaseController {
   
	private final static Logger logger = LoggerFactory.getLogger(TdsUserController.class);
	/**
	 * 检测token, 检测 是否登录， 登录的用户是否该有操作权限
	 * 
	 * 
	 */

	@Autowired
	private TdsUserFeignService tdsUserFeignService;

	@RequestMapping(value = "/loadById", method = RequestMethod.POST)
	public BackResult<TdsUserDomain> loadById(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsUserDomain> result = new BackResult<TdsUserDomain>();
		if (CommonUtils.isNotIngeter(id)) {
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"id不能为空");
		}
		if (CommonUtils.isNotString(token)) {
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
		result = tdsUserFeignService.loadById(id);
		return result;
	}
   
	
	/**
	 * 注册
	 * 发送验证码，接收验证码，进行判断
	 * @param tdsUserDomain
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	//TODO
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BackResult<TdsUserDomain> save(TdsUserDomain tdsUserDomain,HttpServletRequest request, HttpServletResponse response, String token) {
		BackResult<TdsUserDomain> result = new BackResult<TdsUserDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		if (CommonUtils.isNotString(tdsUserDomain.getPhone())){
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"电话号码不能为空");
		}
		if (CommonUtils.isNotString(tdsUserDomain.getName())){
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"用户账户不能为空");
		}
		if (CommonUtils.isNotString(tdsUserDomain.getPassword())){
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"密码不能为空");
		}
		result = tdsUserFeignService.save(tdsUserDomain);
		return result;
	}
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
    public BackResult<TdsUserDomain>  update(TdsUserDomain tdsUserDomain,HttpServletRequest request, HttpServletResponse response, String token){
		BackResult<TdsUserDomain> result = new BackResult<TdsUserDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(tdsUserDomain.getPhone())){
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"电话号码不能为空");
		}
		
		if (CommonUtils.isNotIngeter(tdsUserDomain.getId())){
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"用户id不能为空");
		}
		if (CommonUtils.isNotString(token)) {
			 return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
		
		//TODO
//		if (!isLogin(tdsUserDomain.getPhone(), token)) {
//			 return new BackResult<TdsUserDomain>(ResultCode.RESULT_SESSION_STALED,"注销校验失败无法注销");
//		}
		result = tdsUserFeignService.update(tdsUserDomain);
		return result;
	}
	
	@RequestMapping(value="/deleteById")
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id,HttpServletRequest request, HttpServletResponse response,String token,String phone){
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(id)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"用户id不能为空");
		}
		if (CommonUtils.isNotString(phone)){
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"电话号码不能为空");
		}
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
//		if (!isLogin(phone,token)) {
//			return new BackResult<Integer>(ResultCode.RESULT_SESSION_STALED,"注销校验失败无法注销");
//		}
		result = tdsUserFeignService.deleteById(id);
		return result;
	}
	
	
	@RequestMapping(value="/pageSelectAll",method = RequestMethod.POST)
	public BackResult<PageDomain<TdsUserDomain>> pageSelectAll(TdsUserDomain tdsUserDomain,Integer pageSize,Integer curPage,HttpServletRequest request,HttpServletResponse response,String token){
		BackResult<PageDomain<TdsUserDomain>> result=new BackResult<PageDomain<TdsUserDomain>>();
		response.setHeader("Access-Control-Allow-Origin","*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
  
		if(CommonUtils.isNotIngeter(pageSize)){
			return new BackResult<PageDomain<TdsUserDomain>>(ResultCode.RESULT_PARAM_EXCEPTIONS,"显示条数不能为空");
		}
		
		if(CommonUtils.isNotIngeter(curPage)){
			return new BackResult<PageDomain<TdsUserDomain>>(ResultCode.RESULT_PARAM_EXCEPTIONS,"显示页码不能为空");
		}
		
		if (CommonUtils.isNotString(token)) {
			return new BackResult<PageDomain<TdsUserDomain>>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
		
		logger.info("============用户分页查询==========");
		
		result = tdsUserFeignService.pageSelectAll(tdsUserDomain, pageSize, curPage);
		return result;
	}
	

}
