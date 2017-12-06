package cn.feignclient.credit_feign_web.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.redis.RedisClient;
import cn.feignclient.credit_feign_web.service.CreditProviderService;
import cn.feignclient.credit_feign_web.service.UserAccountFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import cn.feignclient.credit_feign_web.utils.FileUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.RedisKeys;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;
import main.java.cn.domain.CvsFilePathDomain;
import main.java.cn.domain.RunTestDomian;
import main.java.cn.domain.UserAccountDomain;

@RestController
@RequestMapping("/credit")
public class CreditProviderController extends BaseController{
	
	private final static Logger logger = LoggerFactory.getLogger(CreditProviderController.class);
	
	@Autowired
    private CreditProviderService creditProviderService;
	
	@Autowired
	private UserAccountFeignService userAccountFeignService;
	
	@Autowired
	private RedisClient redisClient;
	
//    @RequestMapping(value = "/runTheTest", method = RequestMethod.GET)
//	public BackResult<RunTestDomian> runTheTest(HttpServletRequest request, HttpServletResponse response,String fileUrl,String userId,String mobile,String token,String timestamp) {
//    	
//    	response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
//		response.setContentType("text/json;charset=UTF-8");
//		
//		BackResult<RunTestDomian> result = new BackResult<RunTestDomian>();
//
//		if (CommonUtils.isNotString(mobile)) {
//			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
//			result.setResultMsg("手机号码不能为空");
//			return result;
//		}
//
//		if (CommonUtils.isNotString(token)) {
//			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
//			result.setResultMsg("token不能为空");
//			return result;
//		}
//		
//		if (CommonUtils.isNotString(fileUrl)) {
//			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
//			result.setResultMsg("文件地址不能为空");
//			return result;
//		}
//
//		if (CommonUtils.isNotString(userId)) {
//			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
//			result.setResultMsg("用户ID 不能为空");
//			return result;
//		}
//		
//		if (CommonUtils.isNotString(timestamp)) {
//			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
//			result.setResultMsg("系统当前时间轴不能为空");
//			return result;
//		}
//		
//		if (!isLogin(mobile, token)) {
//			result.setResultCode(ResultCode.RESULT_SESSION_STALED);
//			result.setResultMsg("用户已经注销登录无法进行操作");
//			return result;
//		}
//		return creditProviderService.runTheTest(fileUrl, userId,timestamp,mobile);
//	} 
    
    @RequestMapping(value = "/theTest", method = RequestMethod.POST)
	public BackResult<RunTestDomian> theTest(HttpServletResponse response,String mobile,String token, String fileUrl,
			String source, String startLine, String type) {
    	
    	response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<RunTestDomian> result = new BackResult<RunTestDomian>();
		
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}

		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
		}
		
		if (!isLogin(mobile, token)) {
			result.setResultCode(ResultCode.RESULT_SESSION_STALED);
			result.setResultMsg("用户已经注销登录无法进行操作");
			return result;
		}

		if (CommonUtils.isNotString(fileUrl)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("文件地址不能为空");
			return result;
		}

		if (CommonUtils.isNotString(source)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("来源不能为空");
			return result;
		}

		if (CommonUtils.isNotString(startLine)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("开始条数不能为空");
			return result;
		}

		if (CommonUtils.isNotString(type)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("类型不能为空：1检测 2查询检测结果");
			return result;
		}

		if (type.equals("1")) {
			logger.info("PC网站通手机号：" + mobile + "请求进行实号检测");
		} else {
			logger.info("PC网站手机号：" + mobile + "请求进行实号检测，查询检测结果");
		}

		try {

			CreUserDomain user = findByMobile(mobile);

			if (null == user) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败，系统不存在该手机号码的用户");
				return result;
			}
			
			// 第一次检测的时候进行校验
			if (type.equals("1")) {
				RunTestDomian runTestDomian = new RunTestDomian();
				// 文件检测
				File file = new File(fileUrl);
				if (!file.isFile() || !file.exists()) {
					logger.error("PC网站手机号：" + mobile + "执行实号检测发现文件不存在");
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("执行号码检测发现文件地址不存在");
					return result;
				}

				int lines = FileUtils.getFileLinesNotNullRow(fileUrl);
				// 检测条数限制
				if ((lines) < 2999) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					runTestDomian.setStatus("5"); // 1执行中 2执行结束 // 3执行异常4账户余额不足5检测的条数小于3000条
					result.setResultObj(runTestDomian);
					result.setResultMsg("检测条数必须大于3000条");
					return result;
				}

				// 账户余额检测
				BackResult<UserAccountDomain> resultUserAccount = userAccountFeignService.findbyMobile(mobile);
				
				if (!resultUserAccount.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					runTestDomian.setStatus("3"); // 1执行中 2执行结束 3执行异常4账户余额不足
					result.setResultObj(runTestDomian);
					result.setResultMsg("检测用户余额信息失败");
					return result;
				}
				
				if (resultUserAccount.getResultObj().getAccount() < lines) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					runTestDomian.setStatus("4"); // 1执行中 2执行结束 3执行异常4账户余额不足
					result.setResultObj(runTestDomian);
					result.setResultMsg("账户余额不足");
					return result;
				}
				
				// 校验通过将检测的条数存入redis中
				String KhTestCountKey = RedisKeys.getInstance().getKhTestCountKey(String.valueOf(user.getId()));
				int expire = 4 * 60 * 60 * 1000;
				// 将需要检测的总条数放入redis
				redisClient.set(KhTestCountKey, String.valueOf(lines), expire);
			}

			result = creditProviderService.theTest(fileUrl, String.valueOf(user.getId()), source, mobile, startLine,
					type);
			
			// 执行成功 ，进行结算
			if (result.getResultCode().equals(ResultCode.RESULT_SUCCEED) && result.getResultObj().getStatus().equals("2")) {
				String succeedClearingCountkey = RedisKeys.getInstance().getkhSucceedClearingCountkey(String.valueOf(user.getId()));
				String succeedClearingCount =  redisClient.get(succeedClearingCountkey);
				
				if (!CommonUtils.isNotString(succeedClearingCount)) {
					BackResult<Boolean> resultConsume = userAccountFeignService.consumeAccount(String.valueOf(user.getId()), succeedClearingCount);
					if (resultConsume.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
						redisClient.remove(succeedClearingCountkey);
						logger.info("PC网站手机号：" + mobile + "请求进行实号检测，检测完毕，执行结账成功");
					}
					logger.error("PC网站手机号：" + mobile + "请求进行实号检测，出现结算错误请查账");
				} else {
					logger.error("PC网站手机号：" + mobile + "请求进行实号检测，出现结算错误请查账");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PC网站手机号：" + mobile + "请求进行实号检测，出现系统异常" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}
    
    @RequestMapping(value = "/findByUserId", method = RequestMethod.GET)
    public BackResult<List<CvsFilePathDomain>> findByUserId(HttpServletRequest request, HttpServletResponse response,String userId,String mobile,String token){
    	BackResult<List<CvsFilePathDomain>> result = new BackResult<List<CvsFilePathDomain>>();
    	
    	response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
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
		
		if (CommonUtils.isNotString(userId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户ID 不能为空");
			return result;
		}
		
		if (!isLogin(mobile, token)) {
			result.setResultCode(ResultCode.RESULT_SESSION_STALED);
			result.setResultMsg("用户已经注销登录无法进行操作");
			return result;
		}
		
		try {
			result = creditProviderService.findByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户手机号：" + mobile + "执行查询用户下载列表出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}
    	
    	return result;
    }
    
    @RequestMapping(value = "/deleteCvsByIds", method = RequestMethod.GET)
	public BackResult<Boolean> deleteCvsByIds(HttpServletRequest request, HttpServletResponse response,String ids,String userId,String token,String mobile) {
    	
    	response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		BackResult<Boolean> result = new BackResult<Boolean>();
		
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
		
		if (CommonUtils.isNotString(userId)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("用户ID 不能为空");
			return result;
		}
		
		if (!isLogin(mobile, token)) {
			result.setResultCode(ResultCode.RESULT_SESSION_STALED);
			result.setResultMsg("用户已经注销登录无法进行操作");
			return result;
		}
		
		try {
			result = creditProviderService.deleteCvsByIds(ids, userId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户手机号：" + mobile + "执行查询用户下载列表出现异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}
		
		return result;
    }
    
}
