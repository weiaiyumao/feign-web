package cn.feignclient.credit_feign_web.controller.tds;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsFunctionFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsFunMoViewDomain;
import main.java.cn.domain.tds.TdsFunctionDomain;

@RestController
@RequestMapping("/function")
public class TdsFunctionController extends BaseController {



	@Autowired
	private TdsFunctionFeignService tdsFunctionFeignService;

	@RequestMapping(value = "/loadByIdView", method = RequestMethod.POST)
	public BackResult<TdsFunMoViewDomain> loadByIdView(Integer id, HttpServletRequest request, HttpServletResponse response,
			String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		BackResult<TdsFunMoViewDomain> result = new BackResult<TdsFunMoViewDomain>();
		if (CommonUtils.isNotIngeter(id)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("id不能为空");
			return result;
		}
		result = tdsFunctionFeignService.loadByIdView(id);
		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BackResult<Integer> saveTdsFunction(TdsFunctionDomain tdsFunctionDomain,HttpServletRequest request, HttpServletResponse response, String token,Integer[] arrFuns) {
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(tdsFunctionDomain.getName())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("功能名称不能为空");
			return result;
		}
		
		if (CommonUtils.isNotString(tdsFunctionDomain.getUrl())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("url不能为空");
			return result;
		}
		
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		String strArr=StringUtils.join(arrFuns, ",");
		tdsFunctionDomain.setRemarks(strArr);
		result = tdsFunctionFeignService.save(tdsFunctionDomain);
		return result;
	}
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
    public BackResult<Integer>  update(TdsFunctionDomain tdsFunctionDomain,HttpServletRequest request, HttpServletResponse response, String token,Integer[] arrFuns){
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(tdsFunctionDomain.getId())) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("id不能为空");
			return result;
		}
		if (CommonUtils.isNotString(token)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("token不能为空");
			return result;
		}
		String strArr=StringUtils.join(arrFuns, ",");
		tdsFunctionDomain.setRemarks(strArr);
		result = tdsFunctionFeignService.update(tdsFunctionDomain);
		return result;
	}
	
	@RequestMapping(value="/deleteById")
	public BackResult<Integer> deleteById(Integer id,HttpServletRequest request, HttpServletResponse response,String token){
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
		result = tdsFunctionFeignService.deleteById(id);
		return result;
	}
	
	
	@RequestMapping(value="/pageTdsFunction")
	public BackResult<PageDomain<TdsFunMoViewDomain>> pageTdsFunction(TdsFunMoViewDomain domain,HttpServletRequest request, HttpServletResponse response,String token){
		BackResult<PageDomain<TdsFunMoViewDomain>> result = new BackResult<PageDomain<TdsFunMoViewDomain>>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		result = tdsFunctionFeignService.pageTdsFunction(domain);
		return result;
	}

}
