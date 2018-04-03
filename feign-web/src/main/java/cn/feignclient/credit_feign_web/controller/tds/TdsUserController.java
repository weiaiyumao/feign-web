package cn.feignclient.credit_feign_web.controller.tds;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.redis.RedisClient;
import cn.feignclient.credit_feign_web.service.tds.TdsUserBankApyFeignServer;
import cn.feignclient.credit_feign_web.service.tds.TdsUserFeignService;
import cn.feignclient.credit_feign_web.service.tds.TdsUserRoleFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.common.StatusType;
import main.java.cn.constants.RedisConstants;
import main.java.cn.domain.tds.TdsCompanyDomain;
import main.java.cn.domain.tds.TdsUserBankApyDomain;
import main.java.cn.domain.tds.TdsUserDomain;
import main.java.cn.domain.tds.TdsUserRoleDomain;
import main.java.cn.enums.TdsEnum.ROLETYPE;

@RestController
@RequestMapping("/tdsUser")
public class TdsUserController extends BaseTdsController {

	private final static Logger logger = LoggerFactory.getLogger(TdsUserController.class);

	@Autowired
	private TdsUserFeignService tdsUserFeignService;
	
	@Autowired
	private TdsUserBankApyFeignServer tdsUserBankApyFeignServer;
	
	@Autowired
	private TdsUserRoleFeignService tdsUserRoleFeignService;
    
	@Autowired
	protected RedisClient redisClinet;
	

	@RequestMapping(value = "/loadById", method = RequestMethod.POST)
	public BackResult<TdsUserDomain> loadById(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsUserDomain> result = new BackResult<TdsUserDomain>();
		if (CommonUtils.isNotIngeter(id)) {
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS, "id不能为空");
		}
		if (CommonUtils.isNotString(token)) {
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		result = tdsUserFeignService.loadById(id);
		return result;
	}

	/**
	 * 先从缓存获取用户信息，如果没有则调用接口
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/loadByPhone", method = RequestMethod.POST)
	public BackResult<TdsUserDomain> loadById(HttpServletRequest request, HttpServletResponse response, String token,
			String phone) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsUserDomain> result = new BackResult<TdsUserDomain>();
		if (CommonUtils.isNotString(token)) {
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		if (CommonUtils.isNotString(phone)) {
			return new BackResult<TdsUserDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS, "手机号不能为空");
		}
		// 进入缓存
		TdsUserDomain user = this.getUserInfo(phone);
		user.setPassword(null);
		result.setResultObj(user);
		return result;
	}

	
	/**
	 * 注册
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public BackResult<Integer> save(TdsUserDomain tdsUserDomain, HttpServletRequest request,
			HttpServletResponse response, String code) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		String uid = request.getParameter("uid");

		// 关联uid父级用户，来源其他注册推广
		if (!CommonUtils.isNotString(uid)) {
			tdsUserDomain.setParentUserId(Integer.valueOf(uid));
		}

		if (CommonUtils.isNotString(tdsUserDomain.getPhone())) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "电话号码不能为空");
		}
		
		if (CommonUtils.isNotString(tdsUserDomain.getPassword())) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "密码不能为空");
		}

		String codeKey=String.format(RedisConstants.ADMINREGISTERCODE,tdsUserDomain.getPhone());
		
		String isCode=redisClinet.get(codeKey);
		
		//HttpSession session = request.getSession();	
		//String isCode = (String) session.getAttribute("adminRegisterCode_"+tdsUserDomain.getPhone());
		
		if(CommonUtils.isNotString(isCode)){
			
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "验证码失效,重新发送");
		}
		
		if (null!=isCode && !isCode.equals(code)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "验证码验证失败");
		}
		
		redisClinet.remove(codeKey);  //移除缓存
		
        
		tdsUserDomain.setUserName("nic_" + tdsUserDomain.getPhone());// 用户名先默认手机号码
		
		tdsUserDomain.setSource(StatusType.ADD_REGISTER);
		
		tdsUserDomain.setLoginIp(this.getIpAddr(request));
		
		result = tdsUserFeignService.save(tdsUserDomain);
		
		//保存失败
		if(!result.getResultCode().equals(ResultCode.RESULT_SUCCEED)){
		      return result;
		}
		
		//新用户默认为业务员角色  
		TdsUserRoleDomain userRole=new TdsUserRoleDomain();
		userRole.setUserId(result.getResultObj());
		userRole.setRoleId(ROLETYPE.SALESMAN.getCode()); //业务员角色
		tdsUserRoleFeignService.save(userRole);
		
		///add 绑定信息
		TdsUserBankApyDomain domain=new TdsUserBankApyDomain();
		
		domain.setUserId(result.getResultObj());  //id
		
		domain.setUserPhone(tdsUserDomain.getPhone());
		
		domain.setCreateTime(new Date());
		
		result=tdsUserBankApyFeignServer.addBankApy(domain);
		
		if(!result.getResultCode().equals(ResultCode.RESULT_SUCCEED)){
		      return result;
		}
		

		return result;
	}

	
	/**
	 * 点击获取验证码
	 * 
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/clickeCode", method = RequestMethod.POST)
	public boolean clickeCode(String mobile) {
		return this.clickeCode(mobile,String.format(RedisConstants.ADMINREGISTERCODE, mobile));
	}
	
	
	

	@RequestMapping(value = "/deleteById")
	public BackResult<Integer> deleteById(@RequestParam("id") Integer id, HttpServletRequest request,
			HttpServletResponse response, String token) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(id)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "用户id不能为空");
		}
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		result = tdsUserFeignService.deleteById(id);
		return result;
	}

	// @RequestMapping(value = "/pageSelectAll", method = RequestMethod.POST)
	// public BackResult<PageDomain<TdsUserDomain>> pageSelectAll(TdsUserDomain
	// tdsUserDomain, Integer pageSize,
	// Integer curPage, HttpServletRequest request, HttpServletResponse
	// response) {
	// BackResult<PageDomain<TdsUserDomain>> result = new
	// BackResult<PageDomain<TdsUserDomain>>();
	//
	// response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
	// response.setContentType("text/json;charset=UTF-8");
	//
	// if (CommonUtils.isNotIngeter(pageSize)) {
	// return new
	// BackResult<PageDomain<TdsUserDomain>>(ResultCode.RESULT_PARAM_EXCEPTIONS,
	// "显示条数不能为空");
	// }
	//
	// if (CommonUtils.isNotIngeter(curPage)) {
	// return new
	// BackResult<PageDomain<TdsUserDomain>>(ResultCode.RESULT_PARAM_EXCEPTIONS,
	// "显示页码不能为空");
	// }
	//
	// logger.info("============用户分页查询==========");
	//
	// result = tdsUserFeignService.pageSelectAll(tdsUserDomain, pageSize,
	// curPage);
	// return result;
	// }

	/**
	 * 修改密码
	 * 
	 * @param usedPass
	 *            旧密码
	 * @param newPass
	 *            新密码
	 * @param userId
	 *            修改密码用户
	 * @return
	 */
	@RequestMapping(value = "/upPassWord", method = RequestMethod.POST)
	public BackResult<Integer> upPassWord(String usedPass, String newPass, Integer userId, String token,
			HttpServletRequest request, HttpServletResponse response) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(userId)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "修改用户id不能为空");
		}
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		result = tdsUserFeignService.upPassWord(usedPass, newPass, userId);
		logger.info("用户id：" + userId + "===密码修改===");
		return result;
	}

	/**
	 * 编辑个人
	 * 
	 * @param domain
	 * @param isPersOrCom
	 * @return
	 */
	@RequestMapping(value = "/editUserInfo", method = RequestMethod.POST)
	public BackResult<Integer> editUserInfo(Integer userId, String token, TdsUserDomain domain,
			HttpServletRequest request, HttpServletResponse response, String phone) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}

		if (CommonUtils.isNotIngeter(userId)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "userId不能为空");
		}

		domain.setId(userId);
		result = tdsUserFeignService.editUserInfo(domain);

		// 重新覆盖redias缓存
		if (result.getResultCode().equals("000000")) {
			this.replaceAndSaveObj(phone);
		}

		return result;
	}

	/**
	 * 编辑企业信息
	 * 
	 * @param token
	 * @param request
	 * @param response
	 * @param domain
	 * @param userId
	 *            用户id
	 * @param userName
	 *            用户名
	 * @param phone
	 *            手机
	 * @param contact
	 *            联系人
	 * @return
	 */
	@RequestMapping(value = "/editComInfo", method = RequestMethod.POST)
	public BackResult<Integer> editComInfo(String token, HttpServletRequest request, HttpServletResponse response,
			TdsCompanyDomain domain, Integer userId, String userName, String phone, String contact) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		result = tdsUserFeignService.editComInfo(domain, userId, userName, phone, contact);
		// 重新覆盖redias缓存
		if (result.getResultCode().equals("000000")) {
			this.replaceAndSaveObj(phone);
		}

		return result;
	}

	/**
	 * 获取企业信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/queryComByUserId", method = RequestMethod.POST)
	public BackResult<TdsCompanyDomain> queryComByUserId(Integer userId, String token, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			return new BackResult<TdsCompanyDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS, "token不能为空");
		}
		if (CommonUtils.isNotIngeter(userId)) {
			return new BackResult<TdsCompanyDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS, "用户id不能为空");
		}
		return tdsUserFeignService.queryComByUserId(userId);
	}

}
