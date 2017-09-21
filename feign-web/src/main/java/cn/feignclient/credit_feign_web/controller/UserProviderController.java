package cn.feignclient.credit_feign_web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.UserAccountFeignService;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.TrdOrderDomain;
import main.java.cn.domain.UserAccountDomain;

@RestController
@RequestMapping("/userAccount")
public class UserProviderController extends BaseController{

	@Autowired
	private UserAccountFeignService userAccountFeignService;

	@RequestMapping(value = "/findbyMobile", method = RequestMethod.GET)
	@ResponseBody
	public BackResult<UserAccountDomain> findbyMobile(HttpServletRequest request, HttpServletResponse response,@RequestParam String mobile,String token) {

		 response.setHeader("Access-Control-Allow-Origin", "*");   // 有效，前端可以访问
         response.setContentType("text/json;charset=UTF-8");
		
		Assert.notNull(mobile, "The param mobile not be null!");

		BackResult<UserAccountDomain> result = new BackResult<UserAccountDomain>();
		
		if (isLogin(mobile, token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("注销校验失败无法注销");
		}
		
		result = userAccountFeignService.findbyMobile(mobile);
		
		return result;
	}

	@RequestMapping(value = "/rechargeOrRefunds", method = RequestMethod.GET)
	@ResponseBody
	public BackResult<Boolean> rechargeOrRefunds(TrdOrderDomain trdOrderDomain) {

		Assert.notNull(trdOrderDomain, "The param trdOrderDomain not be null!");
		Assert.notNull(trdOrderDomain.getCreUserId(), "The param creUserId not be null!");
		Assert.notNull(trdOrderDomain.getClOrderNo(), "The param clOrderNo not be null!");
		Assert.notNull(trdOrderDomain.getNumber(), "The param number not be null!");
		Assert.notNull(trdOrderDomain.getMoney(), "The param money not be null!");
		Assert.notNull(trdOrderDomain.getPayType(), "The param payType not be null!");
		Assert.notNull(trdOrderDomain.getPayTime(), "The param payTime not be null!");
		Assert.notNull(trdOrderDomain.getType(), "The param type not be null!");

		return userAccountFeignService.rechargeOrRefunds(trdOrderDomain);
	}

}
