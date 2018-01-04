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
import cn.feignclient.credit_feign_web.service.tds.TdsStateInfoFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.page.PageAuto;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsEnumDomain;
import main.java.cn.domain.tds.TdsProductMoneyDomain;
import main.java.cn.domain.tds.TdsStateInfoDomain;

@RestController
@RequestMapping("/state")
public class TdsStateInfoController  extends BaseController{
  
	private final static Logger logger = LoggerFactory.getLogger(TdsStateInfoController.class);
	

	@Autowired
	private TdsStateInfoFeignService tdsStateInfoFeignService;
	
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
    public BackResult<Integer>  update(TdsStateInfoDomain tdsStateInfoDomain,HttpServletRequest request, HttpServletResponse response, String token){
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		if (CommonUtils.isNotIngeter(tdsStateInfoDomain.getId())) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"id不能为空");
		}
		if (CommonUtils.isNotString(token)) {
			 return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
		logger.info("=====ID更新："+tdsStateInfoDomain.getId()+" 状态信息========");
		result = tdsStateInfoFeignService.update(tdsStateInfoDomain);
		return result;
	}
	
	@RequestMapping(value="/deleteById")
	public BackResult<Integer> deleteById(Integer id,HttpServletRequest request, HttpServletResponse response,String token){
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(id)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"用户id不能为空");
		}
		
		if (CommonUtils.isNotString(token)) {
			return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}

		result = tdsStateInfoFeignService.deleteById(id);
		logger.info("=====ID删除："+id+" 状态信息========");
		return result;
	}
	
	
	@RequestMapping(value = "/pageTdsStateInfo", method = RequestMethod.POST)
	public BackResult<PageDomain<TdsStateInfoDomain>> pageTdsStateInfo(PageAuto auto,HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin","*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		logger.info("============用户分页查询==========");
		if(CommonUtils.isNotString(auto.getRinput())){
			auto.setRinput(null);
		}
		return tdsStateInfoFeignService.pageTdsStateInfo(auto);
	}
	
	
	@RequestMapping(value="/save",method = RequestMethod.POST)
    public BackResult<Integer>  save(TdsStateInfoDomain tdsStateInfoDomain,HttpServletRequest request, HttpServletResponse response, String token){
		BackResult<Integer> result = new BackResult<Integer>();
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");

		if (CommonUtils.isNotString(token)) {
			 return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
		result=tdsStateInfoFeignService.save(tdsStateInfoDomain);
		return result;
	}
	
	
	
	@RequestMapping(value = "/loadById", method = RequestMethod.POST)
	public BackResult<TdsStateInfoDomain> loadById(Integer id,HttpServletRequest request,HttpServletResponse response,String token){
		response.setHeader("Access-Control-Allow-Origin","*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		logger.info("============用户分页查询==========");
		if (CommonUtils.isNotString(token)) {
			 return new BackResult<TdsStateInfoDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
		if (CommonUtils.isNotIngeter(id)) {
			 return new BackResult<TdsStateInfoDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"id不能为空");
		}
		return tdsStateInfoFeignService.loadById(id);
	}
	
	
	/**
	 * 增加项目价格表信息
	 * @param domain  
	 * @param request 
	 * @param response
	 * @param token  
	 * @param logiUserId  
	 * @return
	 */
	@RequestMapping(value = "/addProductTable", method = RequestMethod.POST)
	public BackResult<Integer> addProductTable(TdsProductMoneyDomain domain,HttpServletRequest request,HttpServletResponse response,String token,Integer logiUserId){
		response.setHeader("Access-Control-Allow-Origin","*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(token)) {
			 return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
		}
	    
		if (CommonUtils.isNotIngeter(logiUserId)) {
			 return new BackResult<Integer>(ResultCode.RESULT_PARAM_EXCEPTIONS,"登录用户id不能为空");
		}
		logger.info("============项目价格管理==========");
		domain.setCreater(logiUserId);
		return tdsStateInfoFeignService.addProductTable(domain);
	}
	
	/**
	 * 获取项目列表信息
	 * @param codeName
	 * @return
	 */
	@RequestMapping(value = "/queryByTypeCode", method = RequestMethod.POST)
	public BackResult<List<TdsEnumDomain>> queryByTypeCode(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin","*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		//获取项目类型代码 TDS
		return tdsStateInfoFeignService.queryByTypeCode("TDS");
		
	}
	
}
