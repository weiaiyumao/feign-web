package cn.feignclient.credit_feign_web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.feignclient.credit_feign_web.encryption.Constants;
import cn.feignclient.credit_feign_web.encryption.RsaSignature;
import cn.feignclient.credit_feign_web.encryption.ThreeDesUtil;
import cn.feignclient.credit_feign_web.service.ApiAccountInfoFeignService;
import cn.feignclient.credit_feign_web.service.ApiMobileTestService;
import cn.feignclient.credit_feign_web.service.OpenApiService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.RedisKeys;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.AccountInfoDomain;
import main.java.cn.domain.ApiLogPageDomain;
import main.java.cn.domain.ApiResultDomain;
import main.java.cn.domain.MobileInfoDomain;
import main.java.cn.domain.MobileTestLogDomain;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.untils.KeyUtil;

@RestController
@RequestMapping("/feign/apiMobileTest")
public class ApiMobileTestController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(ApiAccountInfoController.class);

	@Autowired
	private ApiAccountInfoFeignService apiAccountInfoFeignService;

	@Autowired
	private ApiMobileTestService apiMobileTestService;

//	@Autowired
//	private UserAccountFeignService userAccountFeignService;
	
	@Autowired
	private OpenApiService openApiService;
	
	/**
	 * 对外API账户2次清洗接口
	 * 
	 * @param apiName
	 * @param password
	 * @param ip
	 * @param mobileNumbers
	 * @return
	 */
	@RequestMapping(value = "/findByMobileNumbers", method = RequestMethod.POST)
	public synchronized BackResult<List<MobileInfoDomain>> findByMobileNumbers(HttpServletRequest request,
			HttpServletResponse response, String apiName, String password, String mobile) {

		BackResult<List<MobileInfoDomain>> result = new BackResult<List<MobileInfoDomain>>();

		try {

			if (CommonUtils.isNotString(apiName)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户名不能为空");
				return result;
			}

			if (CommonUtils.isNotString(password)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户密码不能为空");
				return result;
			}

			if (CommonUtils.isNotString(mobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的手机号码不能为空");
				return result;
			}

			String[] phones = mobile.split(",");

			String ip = super.getIpAddr(request);

			logger.info("账户号：" + apiName + "的IP地址是：" + ip);

			// 1、账户信息检测
			BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkRqApiAccount(apiName, password, ip,
					phones.length);

			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}

			// 2、执行检测返回检测结果
			result = apiMobileTestService.findByMobileNumbers(mobile, resultCreUser.getResultObj().toString());

//			if (!result.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
//				return result;
//			}
//
//			int changeCount = 0;
//
//			if (!CommonUtils.isNotEmpty(result.getResultObj())) {
//				for (MobileInfoDomain domain : result.getResultObj()) {
//					if (domain.getChargesStatus().equals("1")) {
//						changeCount = changeCount + 1;
//					}
//				}
//			}
//
//			if (changeCount > 0) {
//
//				// 3、结算
//				BackResult<Boolean> resultConsume = userAccountFeignService
//						.consumeRqApiAccount(resultCreUser.getResultObj().toString(), String.valueOf("1"));
//
//				if (!resultConsume.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
//					logger.error("------------------------------------商户号：" + apiName + "执行账户2次清洗出现记账系统异常："
//							+ resultConsume.getResultMsg());
//				}
//
//			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商户号：" + apiName + "执行账户2次清洗出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
			result.setResultObj(null);
		}

		return result;
	}

	@RequestMapping(value = "/getPageByUserId", method = RequestMethod.POST)
	public BackResult<PageDomain<MobileTestLogDomain>> getPageByUserId(HttpServletRequest request,
			HttpServletResponse response, int pageNo, int pageSize, String creUserId, String mobile, String token,String type) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<PageDomain<MobileTestLogDomain>> result = new BackResult<PageDomain<MobileTestLogDomain>>();

		try {

			if (CommonUtils.isNotIngeter(pageNo)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("分页PageNo不能为空");
				return result;
			}

			if (CommonUtils.isNotIngeter(pageSize)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("分页pageSize不能为空");
				return result;
			}

			if (CommonUtils.isNotString(creUserId)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("用户creUserId不能为空");
				return result;
			}

			if (CommonUtils.isNotString(type)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("类型不能为空");
				return result;
			}

			if (CommonUtils.isNotString(mobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("手机号码不能为空");
				return result;
			}

			if (CommonUtils.isNotString(token)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("token不能为空");
				return result;
			}

			if (!isLogin(mobile, token)) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败");
				return result;
			}

			result = apiMobileTestService.getPageByUserId(pageNo, pageSize, creUserId,type);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户ID：" + creUserId + "获取检测列表系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
			result.setResultObj(null);
		}

		return result;

	}
	
	@RequestMapping(value = "/getPageByCustomerId", method = RequestMethod.POST)
	public BackResult<PageDomain<ApiLogPageDomain>> getPageByCustomerId(HttpServletRequest request,
			HttpServletResponse response, int pageNo, int pageSize, String customerId, String mobile, String token, String method) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<PageDomain<ApiLogPageDomain>> result = new BackResult<PageDomain<ApiLogPageDomain>>();

		try {

			if (CommonUtils.isNotIngeter(pageNo)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("分页PageNo不能为空");
				return result;
			}

			if (CommonUtils.isNotIngeter(pageSize)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("分页pageSize不能为空");
				return result;
			}

			if (CommonUtils.isNotString(customerId)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("用户API帐号不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(mobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("手机号码不能为空");
				return result;
			}

			if (CommonUtils.isNotString(method)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("方法名不能为空");
				return result;
			}

			if (CommonUtils.isNotString(token)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("token不能为空");
				return result;
			}

			if (!isLogin(mobile, token)) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败");
				return result;
			}

			result = apiMobileTestService.getPageByCustomerId(pageNo, pageSize, customerId,method);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户API帐号：" + customerId + "获取检测列表系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
			result.setResultObj(null);
		}

		return result;

	}

	/**
	 * 空号API接口
	 * 
	 * @param apiName
	 * @param password
	 * @param ip
	 * @param mobileNumbers
	 * @return
	 */
	@RequestMapping(value = "/findByMobile", method = RequestMethod.POST)
	public synchronized BackResult<MobileInfoDomain> findByMobile(HttpServletRequest request,
			HttpServletResponse response, String apiName, String password, String mobile) {

		BackResult<MobileInfoDomain> result = new BackResult<MobileInfoDomain>();

		Long s = System.currentTimeMillis();
		try {

			if (CommonUtils.isNotString(apiName)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户名不能为空");
				return result;
			}

			if (CommonUtils.isNotString(password)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户密码不能为空");
				return result;
			}

			if (CommonUtils.isNotString(mobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的手机号码不能为空");
				return result;
			}

			// 验证是否为正常的１１位有效数字
			if (!CommonUtils.isNumeric(mobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("请输入正确的11位手机号码");
				return result;
			}

			String ip = super.getIpAddr(request);
			
			logger.info("手机号：" + mobile + "请求基础验证消耗时间：" + (s - System.currentTimeMillis()));

//			logger.info("---------不发短息----------账户号：" + apiName + "的IP地址是：" + ip);

			
			Long m = System.currentTimeMillis();
			// 1、账户信息检测
			BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkApiAccount(apiName, password, ip, 1);
			logger.info("手机号：" + mobile + "请求userService账户检测消耗时间：" + (m - System.currentTimeMillis()));
			
			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}

			Long l = System.currentTimeMillis();
			// 2、执行检测返回检测结果
			result = apiMobileTestService.findByMobile(mobile, resultCreUser.getResultObj().toString());
			logger.info("手机号：" + mobile + "请求credit检测消耗时间：" + (l - System.currentTimeMillis()));
			
//			if (result.getResultCode().equals(ResultCode.RESULT_SUCCEED) && result.getResultObj().getChargesStatus().equals("1")) {
//				// 3、结算
//				BackResult<Boolean> resultConsume = userAccountFeignService
//						.consumeApiAccount(resultCreUser.getResultObj().toString(), String.valueOf(1));
//
//				if (!resultConsume.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
//					logger.error("------------------------------------商户号：" + apiName + "执行空号API出现记账系统异常："
//							+ resultConsume.getResultMsg());
//				}
//
//			}
			logger.info("手机号：" + mobile + "请求总共消耗时间：" + (s - System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商户号：" + apiName + "执行空号API出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
		}

		return result;
	}
	
	/**
	 * 外部ApI接口
	 * 
	 * @param apiName
	 * @param password
	 * @param method
	 * @param paramString
	 * @return
	 */
	@RequestMapping(value = "/checkUserInfo", method = RequestMethod.POST)
	public synchronized BackResult<List<ApiResultDomain>> checkUserInfo(HttpServletRequest request,
			HttpServletResponse response, String apiName, String password, String method, String paramString) {
		//返回结果
		BackResult<List<ApiResultDomain>> result = new BackResult<List<ApiResultDomain>>();

		BackResult<List<ApiResultDomain>> resultApi = new BackResult<List<ApiResultDomain>>();
		
		Long s = System.currentTimeMillis();
		try {

			if (CommonUtils.isNotString(apiName)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户名不能为空");
				return result;
			}

			if (CommonUtils.isNotString(password)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户密码不能为空");
				return result;
			}

			if (CommonUtils.isNotString(method)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的方法名不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(paramString)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的参数字符串不能为空");
				return result;
			}
			//获取用户调用接口的ip地址
			String ip = super.getIpAddr(request);			
			// 1、账户信息检测
			BackResult<AccountInfoDomain> resultCreUser = apiAccountInfoFeignService.checkTcAccount(apiName, password,method, ip);
			logger.info("API帐号：" + apiName + "请求userService账户检测消耗时间：" + (System.currentTimeMillis()-s));
			
			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}
			AccountInfoDomain accountInfo = resultCreUser.getResultObj();
			Integer tcAccount = accountInfo.getTcAccount();
			Integer fcAccount = accountInfo.getFcAccount();
			if("normal_checkUserNameByIDno".equals(method) && tcAccount<=0){
				result.setResultCode(ResultCode.RESULT_API_NOTCOUNT);
				result.setResultMsg("API商户信息剩余可消费条数为：" + tcAccount + ",本次执行需消费：" + 1 + " 条, 无法执行消费，请充值！");
				return result;
			}
			if("normal_checkBankInfo".equals(method) && fcAccount<=0){
				result.setResultCode(ResultCode.RESULT_API_NOTCOUNT);
				result.setResultMsg("API商户信息剩余可消费条数为：" + fcAccount + ",本次执行需消费：" + 1 + " 条, 无法执行消费，请充值！");
				return result;
			}
			//获取参数json串
			JSONObject paramJson = KeyUtil.getParamJson(apiName, method, paramString,System.nanoTime()+"");
			// 2、执行检测返回检测结果
			resultApi = openApiService.openApi(paramJson);
			if(resultApi != null){
				if("0".equals(resultApi.getResultCode())){
						//调用成功扣除用户的条数
						Map<String,Object> params = new HashMap<>();
						params.put("method", method);
						params.put("tcAccount", tcAccount);
						params.put("fcAccount", fcAccount);
						params.put("msAccount", accountInfo.getMsAccount());
						params.put("creUserId", accountInfo.getCreUserId());
						params.put("checkCount", 1);
						params.put("version", accountInfo.getVersion());
						BackResult<Integer> apiResult = apiAccountInfoFeignService.updateTcAccount(params);
						if(!ResultCode.RESULT_SUCCEED.equals(apiResult.getResultCode())){
							result.setResultCode(apiResult.getResultCode());
							result.setResultMsg(apiResult.getResultMsg());
							return result;
						}
				}
			}else{
				result.setResultCode(ResultCode.RESULT_FAILED);
				result.setResultMsg("接口调用失败！");
				return result;
			}
			
			logger.info("API帐号：" + apiName + "请求总共消耗时间：" + (System.currentTimeMillis()-s));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商户号：" + apiName + "执行外部接口："+method+"API出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
			return result;
		}

		return resultApi;
	}

	/**
	 * 号码状态实时查询
	 * 
	 * @param apiName
	 * @param password
	 * @param method
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/checkMobileState", method = RequestMethod.POST)
	public synchronized BackResult<MobileInfoDomain> checkMobileState(HttpServletRequest request,
			HttpServletResponse response, String apiName, String password, String mobile) {
		//返回结果
		BackResult<MobileInfoDomain> result = new BackResult<MobileInfoDomain>();
		String method = "normal_checkMobileState";
		Long s = System.currentTimeMillis();
		try {

			if (CommonUtils.isNotString(apiName)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户名不能为空");
				return result;
			}

			if (CommonUtils.isNotString(password)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户密码不能为空");
				return result;
			}

			if (CommonUtils.isNotString(method)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的方法名不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(mobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的参数手机号码不能为空");
				return result;
			}
			//获取用户调用接口的ip地址
			String ip = super.getIpAddr(request);			
			// 1、账户信息检测
			BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkMsAccount(apiName, password, ip,1);
			logger.info("API帐号：" + apiName + "请求userService账户检测消耗时间：" + (System.currentTimeMillis()-s));			
			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}

			Long l = System.currentTimeMillis();
			result =  apiMobileTestService.findByMobileToAmi(mobile,resultCreUser.getResultObj().toString(),method,System.nanoTime()+"");
			logger.info("API帐号：" + apiName + "请求credit检测消耗时间：" + (System.currentTimeMillis()-l));			
			logger.info("API帐号：" + apiName + "请求总共消耗时间：" + (System.currentTimeMillis()-s));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商户号：" + apiName + "执行外部接口："+method+"API出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
		}

		return result;
	}
	
	/**
	 * 外部ApI接口
	 * 
	 * @param apiName
	 * @param password
	 * @param method
	 * @param paramString
	 * @return
	 */
	@RequestMapping(value = "/checkUserInfoN", method = RequestMethod.POST)
	public synchronized BackResult<List<ApiResultDomain>> checkUserInfoN(HttpServletRequest request,
			HttpServletResponse response,  String appid,String encryptParamStr) {
		//返回结果
		BackResult<List<ApiResultDomain>> result = new BackResult<List<ApiResultDomain>>();
		BackResult<List<ApiResultDomain>> resultApi = new BackResult<List<ApiResultDomain>>();
		String des_key = null;
		String rsa_key = null;
		
		if (CommonUtils.isNotString(encryptParamStr)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("参数不能为空");
			return result;
		}
		
		//获取用户的des密钥和rsa公钥
		String key = redisClinet.get(RedisKeys.getInstance().getUserEncryptKey(appid));
		if(StringUtils.isBlank(key)){
			des_key = Constants.des_key;
			rsa_key = Constants.rsa_key;
		}else{
			des_key = JSONObject.parseObject(key).getString("des_key");
			rsa_key = JSONObject.parseObject(key).getString("rsa_key");
		}
		
		//des解密参数串
		String resultstr = new String(ThreeDesUtil.decryptMode(des_key.getBytes(),ThreeDesUtil.hexStringToBytes(encryptParamStr)));
		JSONObject paramJson = JSONObject.parseObject(resultstr);
		String apiName = paramJson.getString("apiName");
		String password = paramJson.getString("password");
		String method = paramJson.getString("method");
		String paramString = paramJson.getString("paramString");
		String order_no = paramJson.getString("order_no");
		String sign = paramJson.getString("sign");
		
		Long s = System.currentTimeMillis();
		try {

			if (CommonUtils.isNotString(apiName)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户名不能为空");
				return result;
			}

			if (CommonUtils.isNotString(password)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户密码不能为空");
				return result;
			}

			if (CommonUtils.isNotString(method)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的方法名不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(paramString)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的参数字符串不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(order_no)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("交易流水号不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(sign)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("签名不能为空");
				return result;
			}
			
			StringBuilder builder = new StringBuilder();
			builder.append(Constants.apiName + "=" + apiName);
			builder.append("&" + Constants.method + "=" + method);
			builder.append("&" + Constants.order_no + "=" + order_no);
			builder.append("&" + Constants.paramString + "=" + paramString);
			builder.append("&" + Constants.password + "=" + password);
			
			//验证sign		
			boolean signResult = RsaSignature.rsaCheckContent(builder.toString(), sign, rsa_key,
					Constants.CHARSET_UTF8);
			
			if(signResult == false){
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("签名错误");
				return result;
			}
			
			//获取用户调用接口的ip地址
			String ip = super.getIpAddr(request);			
			// 1、账户信息检测
			BackResult<AccountInfoDomain> resultCreUser = apiAccountInfoFeignService.checkTcAccount(apiName, password,method, ip);
			logger.info("API帐号：" + apiName + "请求userService账户检测消耗时间：" + (System.currentTimeMillis()-s));
			
			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}
			AccountInfoDomain accountInfo = resultCreUser.getResultObj();
			Integer tcAccount = accountInfo.getTcAccount();
			Integer fcAccount = accountInfo.getFcAccount();
			if("normal_checkUserNameByIDno".equals(method) && tcAccount<=0){
				result.setResultCode(ResultCode.RESULT_API_NOTCOUNT);
				result.setResultMsg("API商户信息剩余可消费条数为：" + tcAccount + ",本次执行需消费：" + 1 + " 条, 无法执行消费，请充值！");
				return result;
			}
			if("normal_checkBankInfo".equals(method) && fcAccount<=0){
				result.setResultCode(ResultCode.RESULT_API_NOTCOUNT);
				result.setResultMsg("API商户信息剩余可消费条数为：" + fcAccount + ",本次执行需消费：" + 1 + " 条, 无法执行消费，请充值！");
				return result;
			}
			//获取参数json串
			JSONObject paramJson2 = KeyUtil.getParamJson(apiName, method, paramString,order_no);
			// 2、执行检测返回检测结果
			resultApi = openApiService.openApi(paramJson2);
			if(resultApi != null){
				if("0".equals(resultApi.getResultCode())){
						//调用成功扣除用户的条数
						Map<String,Object> params = new HashMap<>();
						params.put("method", method);
						params.put("tcAccount", tcAccount);
						params.put("fcAccount", fcAccount);
						params.put("msAccount", accountInfo.getMsAccount());
						params.put("creUserId", accountInfo.getCreUserId());
						params.put("checkCount", 1);
						params.put("version", accountInfo.getVersion());
						BackResult<Integer> apiResult = apiAccountInfoFeignService.updateTcAccount(params);
						if(!ResultCode.RESULT_SUCCEED.equals(apiResult.getResultCode())){
							result.setResultCode(apiResult.getResultCode());
							result.setResultMsg(apiResult.getResultMsg());
							return result;
						}
				}
			}else{
				result.setResultCode(ResultCode.RESULT_FAILED);
				result.setResultMsg("接口调用失败！");
				return result;
			}
			
			logger.info("API帐号：" + apiName + "请求总共消耗时间：" + (System.currentTimeMillis()-s));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商户号：" + apiName + "执行外部接口："+method+"API出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
			return result;
		}

		return resultApi;
	}

	/**
	 * 号码状态实时查询
	 * 
	 * @param apiName
	 * @param password
	 * @param method
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/checkMobileStateN", method = RequestMethod.POST)
	public synchronized BackResult<MobileInfoDomain> checkMobileStateN(HttpServletRequest request,
			HttpServletResponse response,String appid, String encryptParamStr) {
		//返回结果
		BackResult<MobileInfoDomain> result = new BackResult<MobileInfoDomain>();
		String method = "normal_checkMobileState";
		String des_key = null;
		String rsa_key = null;
		
		if (CommonUtils.isNotString(encryptParamStr)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("参数不能为空");
			return result;
		}
		
		//获取用户的des密钥和rsa公钥
		String key = redisClinet.get(RedisKeys.getInstance().getUserEncryptKey(appid));
		if(StringUtils.isBlank(key)){
			des_key = Constants.des_key;
			rsa_key = Constants.rsa_key;
		}else{
			des_key = JSONObject.parseObject(key).getString("des_key");
			rsa_key = JSONObject.parseObject(key).getString("rsa_key");
		}
		
		//des解密参数串
		String resultstr = new String(ThreeDesUtil.decryptMode(des_key.getBytes(),ThreeDesUtil.hexStringToBytes(encryptParamStr)));
		JSONObject paramJson = JSONObject.parseObject(resultstr);
		String apiName = paramJson.getString("apiName");
		String password = paramJson.getString("password");
		String mobile = paramJson.getString("mobile");
		String order_no = paramJson.getString("order_no");
		String sign = paramJson.getString("sign");
		
		Long s = System.currentTimeMillis();
		try {

			if (CommonUtils.isNotString(apiName)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户名不能为空");
				return result;
			}

			if (CommonUtils.isNotString(password)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("商户API账户密码不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(mobile)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("检测的参数手机号码不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(order_no)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("交易流水号不能为空");
				return result;
			}
			
			if (CommonUtils.isNotString(sign)) {
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("签名不能为空");
				return result;
			}
			
			StringBuilder builder = new StringBuilder();
			builder.append(Constants.apiName + "=" + apiName);
			builder.append("&" + Constants.mobile + "=" + mobile);
			builder.append("&" + Constants.order_no + "=" + order_no);
			builder.append("&" + Constants.password + "=" + password);
			
			//验证sign		
			boolean signResult = RsaSignature.rsaCheckContent(builder.toString(), sign, rsa_key,
					Constants.CHARSET_UTF8);
			
			if(signResult == false){
				result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
				result.setResultMsg("签名错误");
				return result;
			}
			//获取用户调用接口的ip地址
			String ip = super.getIpAddr(request);			
			// 1、账户信息检测
			BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkMsAccount(apiName, password, ip,1);
			logger.info("API帐号：" + apiName + "请求userService账户检测消耗时间：" + (System.currentTimeMillis()-s));			
			if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result.setResultCode(resultCreUser.getResultCode());
				result.setResultMsg(resultCreUser.getResultMsg());
				return result;
			}

			Long l = System.currentTimeMillis();
			result =  apiMobileTestService.findByMobileToAmi(mobile,resultCreUser.getResultObj().toString(),method,order_no);
			logger.info("API帐号：" + apiName + "请求credit检测消耗时间：" + (System.currentTimeMillis()-l));			
			logger.info("API帐号：" + apiName + "请求总共消耗时间：" + (System.currentTimeMillis()-s));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商户号：" + apiName + "执行外部接口："+method+"API出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
		}

		return result;
	}
	
}
