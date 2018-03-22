package cn.feignclient.credit_feign_web.controller.tds;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.tds.TdsUserBankApyFeignServer;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.constants.RedisConstants;
import main.java.cn.domain.tds.TdsUserBankApyDomain;
import main.java.cn.enums.TdsEnum.ISBUND_PHONE;

@SuppressWarnings("all")
@RestController
@RequestMapping("/home")
public class TdsHomeController extends BaseTdsController {

	//private final static Logger logger = LoggerFactory.getLogger(TdsHomeController.class);

	@Autowired
	private TdsUserBankApyFeignServer tdsUserBankApyFeignServer;

	
	/**
	 * 统计客户数量，累计充值金额，剩余佣金,绑定信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/countByUserId", method = RequestMethod.POST)
	BackResult<Map<String, Object>> countByUserId(Integer userId, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<Map<String, Object>> result = new BackResult<Map<String, Object>>();

		if (CommonUtils.isNotIngeter(userId)) {
			return result.error(ResultCode.RESULT_FAILED,"用户ud不能为空");
		}

		result = tdsUserBankApyFeignServer.countByUserId(userId);

		return result;
	}

	
	/**
	 * 手机解绑
	 * isBundPhoneType  0:解绑  1绑定
	 * @return
	 */
	@RequestMapping(value = "/isBundPhone", method = RequestMethod.POST)
	BackResult<Boolean> unbundPhone(Integer userId, HttpServletRequest request, HttpServletResponse response,
			String token, String mobile, String code,String isBundPhoneType) {

		BackResult<Boolean> result = new BackResult<>();

		if (CommonUtils.isNotIngeter(userId)) {
			return result.error(ResultCode.RESULT_FAILED,"用户ud不能为空");
		}

		if (CommonUtils.isNotString(mobile)) {
			return result.error(ResultCode.RESULT_FAILED,"号码不能为空");
		}

		if (CommonUtils.isNotString(token)) {
			return result.error(ResultCode.RESULT_FAILED,"token不能为空");
		}

		
		String redisKey=String.format(RedisConstants.ADMINBUNDCODEMOBLIE, mobile);
		
		// 获取code
		String isCode = redisClinet.get(redisKey);
		

		if (CommonUtils.isNotString(isCode)) {
			return result.error(ResultCode.RESULT_FAILED,"验证码失效,重新发送");
			
		}

		if (null!=isCode && !isCode.equals(code)) {
			return result.error(ResultCode.RESULT_FAILED,"验证码错误");
		}
		
		
		//移除缓存
		redisClinet.remove(redisKey);
		
		TdsUserBankApyDomain domain=new TdsUserBankApyDomain();
		domain.setUserId(userId);
		
		//如果 参数为0 ：解绑,则 1：绑定
		if(isBundPhoneType.equals(ISBUND_PHONE.BUNDING.getCode())){
			
			domain.setUserPhone(mobile);
			
		}else{
			domain.setUserPhone(""); //解绑为空
		}
		
		BackResult<Integer> fag=tdsUserBankApyFeignServer.updateByUserId(domain);
		
		//数据是否保存成功
		if(!fag.getResultCode().equals(ResultCode.RESULT_SUCCEED)){
			return result.error("号码解绑,绑定失败");
		}
		
		result.setResultObj(true);
		
		return result;

	}
	
	
	/**
	 * 点击获取验证码
	 * 解绑
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "unbundOrbundPhone/clickeCode", method = RequestMethod.POST)
	public boolean clickeCode(String mobile) {
	     return this.clickeCode(mobile,String.format(RedisConstants.ADMINBUNDCODEMOBLIE, mobile));
	}

	
	/**
	 * 
	 * 修改更换用户绑定银行卡， 支付宝信息接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updateByUserId", method = RequestMethod.POST)
	public BackResult<Integer> updateByUserId(TdsUserBankApyDomain domain, HttpServletRequest request,
			HttpServletResponse response, String token) {

		BackResult<Integer> result = new BackResult<>();

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		if (CommonUtils.isNotString(token)) {
			return result.error(ResultCode.RESULT_FAILED,"token不能为空");
		}

		if (CommonUtils.isNotIngeter(domain.getUserId())) {

			return result.error(ResultCode.RESULT_FAILED,"用户id不能为空");


		}

		return tdsUserBankApyFeignServer.updateByUserId(domain);

	}

	@RequestMapping(value = "/loadBankApyByUserId", method = RequestMethod.POST)
	public BackResult<TdsUserBankApyDomain> loadByUserId(Integer userId, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		BackResult<TdsUserBankApyDomain> result = new BackResult<TdsUserBankApyDomain>();

		if (CommonUtils.isNotIngeter(userId)) {
			return result.error(ResultCode.RESULT_FAILED,"用户userId不能为空");
		}
		return tdsUserBankApyFeignServer.loadBankApyByUserId(userId);
	}

	/**
	 * 获取对应的产品剩余数量
	 * @param userId
	 * @param pnameId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getAccByNumber", method = RequestMethod.POST)
	public BackResult<Integer> getAccByNumber(Integer userId, Integer pnameId, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<Integer> result = new BackResult<Integer>();
		
		if (CommonUtils.isNotIngeter(userId)) {
			return result.error(ResultCode.RESULT_FAILED,"用户userId不能为空");
		}

		result = tdsUserBankApyFeignServer.getAccByNumber(userId, pnameId);

		return result;
	}

}
