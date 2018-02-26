package cn.feignclient.credit_feign_web.service.tds;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.feignclient.credit_feign_web.service.tds.hihystric.TdsMoneyApprovalFeignServiceHiHystric;
import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsApprovalOutDomain;
import main.java.cn.domain.tds.TdsApprovalOutQueryDomain;
import main.java.cn.domain.tds.TdsCommissionDomain;
import main.java.cn.domain.tds.TdsMoneyApprovalDomain;
import main.java.cn.domain.tds.TdsSerualInfoDomain;

@FeignClient(value = "user-provider-service", fallback = TdsMoneyApprovalFeignServiceHiHystric.class)
public interface TdsMoneyApprovalFeignService {
	
	
	
	 
	/**
	   * 佣金分页查询
	   * @param id
	   * @return  obj
	   */
	@RequestMapping(value="/moneyApproval/pageCommission",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsCommissionDomain>> pageCommission(TdsCommissionDomain domain);
	
	 /**
	   * 流水分页查询
	   * @param id
	   * @return  obj
	   */
	@RequestMapping(value="/moneyApproval/pageTdsSerualInfo",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsSerualInfoDomain>> pageTdsSerualInfo( TdsSerualInfoDomain domain);
	
	
	
    /**
     * 下单
     * @param domain
     * @return
     */
	@RequestMapping(value = "/moneyApproval/downAddOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> downAddOrder(TdsMoneyApprovalDomain domain);

	/**
	 * 进账分页查询
	 * 
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "/moneyApproval/pageApprovalByUpStatusGo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsMoneyApprovalDomain>> pageApprovalByUpStatusGo(TdsMoneyApprovalDomain domain);

	/**
	 * 出账分页查询
	 * 
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "/moneyApproval/pageApprovalByUpStatusOut", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsApprovalOutDomain>> pageApprovalByUpStatusOut(TdsApprovalOutQueryDomain domain);

	/**
	 * 出账审核状态修改
	 * 
	 * @return obj
	 */
	@RequestMapping(value = "/moneyApproval/updatePageApprovalByUpStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> updatePageApprovalByUpStatus(@RequestParam("userId") String userId,@RequestParam("tdsCarryId") String tdsCarryId,
			@RequestParam("status") String status);
	
	/**
	 * 退账分页查询
	 * 
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = "/moneyApproval/pageApprovalByUpStatusBack", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsMoneyApprovalDomain>> pageApprovalByUpStatusBack(TdsMoneyApprovalDomain domain);

	/**
	 * 1进账审核操作 审核类型 1进账审核 2出账审核 3退款审核 : approval_type
	 * 
	 * @return obj
	 */
	@RequestMapping(value = "/moneyApproval/approvalByUpStatusGo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> approvalByUpStatusGo(TdsMoneyApprovalDomain domain,
			@RequestParam("appRemarks") String appRemarks);

	/**
	 * 2出账审核操作 审核类型 1进账审核 2出账审核 3退款审核 : approval_type
	 * 
	 * @return obj
	 */
	@RequestMapping(value = "/moneyApproval/approvalByUpStatusOut", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> approvalByUpStatusOut(TdsMoneyApprovalDomain domain,
			@RequestParam("appRemarks") String appRemarks);

	/**
	 * 2出账审核操作 审核类型 1进账审核 2出账审核 3退款审核 : approval_type
	 * 
	 * @return obj
	 */
	@RequestMapping(value = "/moneyApproval/approvalByUpStatusBack", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> approvalByUpStatusBack(TdsMoneyApprovalDomain domain,
			@RequestParam("appRemarks") String appRemarks);
}
