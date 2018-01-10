package cn.feignclient.credit_feign_web.controller.tds;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.service.tds.TdsPositionFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsCompanyDomain;
import main.java.cn.domain.tds.TdsPositionDomain;

/**
 * 职务，公司
 * @author ChuangLan
 *
 */
@RestController
@RequestMapping("/position")
public class TdsPositionController{

	
	@Autowired
	private TdsPositionFeignService tdsPositionFeignService;
	
	
	/**
	 * 根据部门id查询职务
	 * @param auto
	 * @return
	 */
	@RequestMapping(value = "/selectByDeparId", method = RequestMethod.POST)
	public BackResult<List<TdsPositionDomain>> selectByDeparId(Integer departmentId,HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(departmentId)) {
			 return new BackResult<>(ResultCode.RESULT_PARAM_EXCEPTIONS,"部门id不能为空");
		}
		
		return tdsPositionFeignService.selectByDeparId(departmentId);
	}
	
	
	
	
	@RequestMapping(value = "/selectCompanyAll", method = RequestMethod.POST)
	public BackResult<List<TdsCompanyDomain>> selectCompanyAll(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsPositionFeignService.selectCompanyAll();
	}
	

}
