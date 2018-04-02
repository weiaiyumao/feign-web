package cn.feignclient.credit_feign_web.controller.tds;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.service.tds.TdsFunctionFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.domain.page.BasePageParam;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsFunctionDomain;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/modular")
public class TdsModularController extends BaseController {

	
	@Autowired
	private TdsFunctionFeignService tdsFunctionFeignService;

	
	@RequestMapping(value="selectAll")
	public BackResult<List<TdsFunctionDomain>> selectAll(TdsFunctionDomain tdsFunctionDomain,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		tdsFunctionDomain.setRemarks("MODUL");  //只查  code 为MODUl
		return tdsFunctionFeignService.selectAll(tdsFunctionDomain);
	}
	
	@RequestMapping(value = "/loadById", method = RequestMethod.POST)
	public BackResult<TdsFunctionDomain> loadById(Integer id,HttpServletResponse response,
			String token) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsFunctionFeignService.loadById(id);
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BackResult<Integer> saveModular(TdsFunctionDomain tdsFunctionDomain,HttpServletResponse response, String token,
			Integer[] arrModulars) {
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotString(tdsFunctionDomain.getName())) {
			return BackResult.error("模块名称不能为空");
		}
		
		if (CommonUtils.isNotString(token)) {
			return BackResult.error("token不能为空");
		}
		return tdsFunctionFeignService.saveModular(tdsFunctionDomain);
	}
	
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
    public BackResult<Integer>  updateModular(String name,Integer selectedId,Integer newId,HttpServletResponse response, String token,String arrModulars){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		if (CommonUtils.isNotIngeter(newId)) {
			return BackResult.error("更新newId不能为空");
		}
		if (CommonUtils.isNotString(token)) {
			return BackResult.error("token不能为空");
		}
		return tdsFunctionFeignService.updateModular(name, selectedId, newId,arrModulars);
	}
	

	
	@RequestMapping(value="/deleteById")
	public BackResult<Integer> deleteById(@RequestParam("id")Integer id,HttpServletResponse response,String token){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		if (CommonUtils.isNotIngeter(id)) {
			return BackResult.error("模块id不能为空");
		}
		if (CommonUtils.isNotString(token)) {
			return BackResult.error("token不能为空");
		}
		return tdsFunctionFeignService.deleteById(id);
	}
	
	
	
	@RequestMapping(value="pageByModular")
	BackResult<PageDomain<Map<String,Object>>> pageByModular(String name,BasePageParam basePageParam,HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		return tdsFunctionFeignService.pageByModular(name,basePageParam);
	}
	
	


}
