package cn.feignclient.credit_feign_web.controller.tds;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsAccounBankFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsAccountBankDomain;

@RestController
@RequestMapping("/accounBank")
public class TdsAccounBankController extends BaseController{

	private final static Logger logger = LoggerFactory.getLogger(TdsAccounBankController.class);
	
	
	@Autowired
	private TdsAccounBankFeignService tdsAccounBankFeignService;

	/**
	 * 新增
	 * 
	 * @param tdsRoleDomain
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BackResult<Integer> saveTdsFunction(TdsAccountBankDomain tdsAccountBankDomain, HttpServletRequest request,
			HttpServletResponse response, String token) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		logger.info("==========入账银行进行新增===========");
		result = tdsAccounBankFeignService.save(tdsAccountBankDomain);
		return result;
	}

	/**
	 * 修改编辑
	 * 
	 * @param tdsRoleDomain
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public BackResult<Integer> update(TdsAccountBankDomain tdsAccountBankDomain, HttpServletRequest request,
			HttpServletResponse response, String token) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		logger.info("==========入账银行进行编辑修改===========");
		result = tdsAccounBankFeignService.update(tdsAccountBankDomain);
		return result;
	}

	/**
	 * 停用
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/isDisableById")
	public BackResult<Integer> isDisableById(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(id)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		logger.info("==========入账银行进行停用===========");
		result = tdsAccounBankFeignService.isDisableById(id);
		return result;
	}

	/**
	 * <分页>条模糊查询
	 * 
	 * @param sname
	 *            关键字
	 * @param currentPage
	 * @param numPerPage
	 * @param selected
	 *            0：全部 1：简称 2：账号名称 3：开户银行 4：开户账号
	 * @return
	 */
	@RequestMapping(value = "pageTdsAccountBank")
	public BackResult<PageDomain<TdsAccountBankDomain>> pageTdsAccountBank(HttpServletRequest request,
			HttpServletResponse response,TdsAccountBankDomain tdsAccountBankDomain) {
		BackResult<PageDomain<TdsAccountBankDomain>> result = new BackResult<PageDomain<TdsAccountBankDomain>>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		result = tdsAccounBankFeignService.pageTdsAccountBank(tdsAccountBankDomain);
		return result;
	}
	
	
	
	/**
	 * 根据id获取对象
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/loadById")
	public BackResult<TdsAccountBankDomain> loadById(Integer id, HttpServletRequest request, HttpServletResponse response) {
		BackResult<TdsAccountBankDomain> result = new BackResult<TdsAccountBankDomain>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(id)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("id不能为空");
			return result;
		}
		result = tdsAccounBankFeignService.loadById(id);
		return result;
	}
	
	
	
	@RequestMapping(value = "/selectAllBankName")
	public BackResult<List<Map<String, Object>>> selectAllBankName(){
		BackResult<List<Map<String, Object>>> result=tdsAccounBankFeignService.selectAllBankName();
        return result;
	}

}
