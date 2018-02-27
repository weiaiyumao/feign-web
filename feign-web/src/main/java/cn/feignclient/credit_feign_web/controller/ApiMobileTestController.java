package cn.feignclient.credit_feign_web.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.common.SysCode;
import cn.feignclient.credit_feign_web.redis.DistributedLock;
import main.java.cn.common.RedisKeys;
import main.java.cn.domain.ApiAccountInfoDomain;
import main.java.cn.domain.UserAccountDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.ApiAccountInfoFeignService;
import cn.feignclient.credit_feign_web.service.ApiMobileTestService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.MobileInfoDomain;
import main.java.cn.domain.MobileTestLogDomain;
import main.java.cn.domain.page.PageDomain;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/feign/apiMobileTest")
public class ApiMobileTestController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(ApiAccountInfoController.class);

	@Autowired
	private ApiAccountInfoFeignService apiAccountInfoFeignService;

	@Autowired
	private ApiMobileTestService apiMobileTestService;

	@Autowired
	private JedisPool jedisPool;

	@Autowired
	private RedisTemplate<String, ApiAccountInfoDomain> redisTemplate;

	@Autowired
	private RedisTemplate<String, UserAccountDomain> userredisTemplate;

//	@Autowired
//	private UserAccountFeignService userAccountFeignService;

	/**
	 * 对外API账户2次清洗接口
	 * 
	 * @param apiName
	 * @param password
	 * @param
	 * @param
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
			BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkApiAccount(apiName, password, ip, 1,mobile, SysCode.rq_api_type);

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

	/**
	 * 空号API接口
	 * 
	 * @param apiName
	 * @param password
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/findByMobile", method = RequestMethod.POST)
	public BackResult<MobileInfoDomain> findByMobile(HttpServletRequest request,
			HttpServletResponse response, String apiName, String password, String mobile) {

		BackResult<MobileInfoDomain> result = new BackResult<MobileInfoDomain>();
		String lockName = RedisKeys.getInstance().getkhApifunFeignKey(mobile);
		DistributedLock lock = new DistributedLock(jedisPool);
		// 加锁
		String identifier = lock.lockWithTimeout(lockName, 10L, 3 * 1000);
		Long s = System.currentTimeMillis();
		try {

			// 处理加锁业务
			if (null != identifier) {
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

//			logger.info("---------不发短息----------账户号：" + apiName + "的IP地址是：" + ip);


				Long m = System.currentTimeMillis();
				// 1、账户信息检测
//				BackResult<Integer> resultCreUser = apiAccountInfoFeignService.checkApiAccount(apiName, password, ip, 1,mobile, SysCode.kh_api_type);

				BackResult<Integer> resultCreUser =  this.checkApiAccount(apiName, password, ip,1,mobile, SysCode.kh_api_type);

				if (!resultCreUser.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
					result.setResultCode(resultCreUser.getResultCode());
					result.setResultMsg(resultCreUser.getResultMsg());
					return result;
				}

				Long l = System.currentTimeMillis();
				// 2、执行检测返回检测结果
				result = apiMobileTestService.findByMobile(mobile, resultCreUser.getResultObj() != null ? resultCreUser.getResultObj().toString(): "");

				logger.info("手机号：" + mobile +"请求userService账户检测消耗时间：【" + (System.currentTimeMillis() - m) + "ms】；" +"credit消耗时间：【" + (System.currentTimeMillis() - l) + "ms】；" + "总共消耗时间：【" + (System.currentTimeMillis() - s) + "ms】；");
			} else {
				return new BackResult<MobileInfoDomain>(ResultCode.RESULT_API_NOTCONCURRENT, "正在计算中");
			}


		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商户号：" + apiName + "执行空号API出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常！");
		}

		return result;
	}

	public BackResult<Integer> checkApiAccount (String apiName,String password ,String ip ,Integer count ,String mobile ,String type){

		BackResult<Integer> result = new BackResult<Integer>();

		// 缓存中获取redis API用户信息对象
		String skey = RedisKeys.getInstance().getAPIAccountInfoKey(apiName);
		ApiAccountInfoDomain domain = redisTemplate.opsForValue().get(skey);
		// 缓存中获取redis 用户账户信息对象

		if (null == domain) {
			// 1、账户信息检测
			logger.info("用户apiName" + apiName + "接口请求检测账户信息");
			return apiAccountInfoFeignService.checkApiAccount(apiName, password, ip, count,mobile, SysCode.kh_api_type);
		}

		// 2、检测api账户ip绑定信息 如果商户设置了ip则进行验证 反之不验证改参数
		if (!CommonUtils.isNotString(domain.getBdIp())) {

			Boolean fag = false;
			String[] ips = domain.getBdIp().split(",");
			for (String str : ips) {
				if (str.equals(ip)) {
					fag = true;
				}
			}

			if (!fag) {
				result.setResultCode(ResultCode.RESULT_API_NOTIPS);
				result.setResultMsg("API商户绑定的IP地址验证校验失败！");
				return result;
			}

		}

		// 先从redis中获取
		String uskey = RedisKeys.getInstance().getAPIAccountKey(domain.getCreUserId());
		UserAccountDomain userAccountDomain = userredisTemplate.opsForValue().get(uskey);

		if (null == userAccountDomain) {
			result.setResultCode(ResultCode.RESULT_API_NOTACCOUNT);
			result.setResultMsg("API商户信息不存在，或者已经删除请联系数据中心客户人员！");
			logger.error("用户id为：" + domain.getCreUserId() + "的账户出现数据完整性异常");
			return result;
		}

		if (type.equals(SysCode.kh_api_type)) {
			if (userAccountDomain.getApiAccount() < 1) {
				result.setResultCode(ResultCode.RESULT_API_NOTCOUNT);
				result.setResultMsg("API商户信息剩余可消费条数为：" + userAccountDomain.getApiAccount() + "本次执行消费：" + 1 + "无法执行消费，请充值！");
				return result;
			}
		} else if (type.equals(SysCode.rq_api_type)) {
			if (userAccountDomain.getRqAccount() < 1) {
				result.setResultCode(ResultCode.RESULT_API_NOTCOUNT);
				result.setResultMsg("账户二次清洗API商户信息剩余可消费条数为：" + userAccountDomain.getRqAccount() + "本次执行消费：" + 1 + "无法执行消费，请充值！");
				return result;
			}
		}

		return result;
	}

}
