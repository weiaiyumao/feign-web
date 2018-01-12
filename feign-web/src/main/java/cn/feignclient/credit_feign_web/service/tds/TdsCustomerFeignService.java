package cn.feignclient.credit_feign_web.service.tds;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.cn.common.BackResult;
import main.java.cn.domain.page.PageAuto;
import main.java.cn.domain.page.PageDomain;
import main.java.cn.domain.tds.TdsAttornLogDomain;
import main.java.cn.domain.tds.TdsCustomerViewDomain;
import main.java.cn.domain.tds.TdsUserDiscountDomain;

@FeignClient(value = "user-provider-service")
public interface TdsCustomerFeignService {
	@RequestMapping(value = "/customer/pageTdsCustomer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<PageDomain<TdsCustomerViewDomain>> pageTdsCustomer(PageAuto auto);

	@RequestMapping(value = "/customer/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> update(@RequestParam("loginUserId") Integer loginUserId, PageAuto auto,
			@RequestParam("upUserId") Integer upUserId, @RequestParam("arrRoles") Integer[] arrRoles);

	@RequestMapping(value = "/customer/addTdsCustomer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> addTdsCustomer(PageAuto auto, @RequestParam("loginUserId") Integer loginUserId,
			@RequestParam("arrRoles") Integer[] arrRoles);

	@RequestMapping(value = "/customer/loadById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<TdsCustomerViewDomain> loadByIdView(@RequestParam("userId") Integer userId);

	@RequestMapping(value = "/customer/attorn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> attorn(TdsAttornLogDomain domain);

	@RequestMapping(value = "/customer/updatePrice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> updatePrice(@RequestParam("id") Integer id, @RequestParam("discount") String discount);

	// ====改价
	@RequestMapping(value = "/customer/updatePrice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> updatePrice(TdsUserDiscountDomain domain);

	@RequestMapping(value = "/customer/selectAllByUserId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<TdsUserDiscountDomain>> selectAllByUserId(@RequestParam("userId") Integer userId);

	@RequestMapping(value = "/customer/addTdsUserDiscount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> addTdsUserDiscount(TdsUserDiscountDomain domain);

	@RequestMapping(value = "/customer/deleteById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> deleteById(@RequestParam("id") Integer id);

	// ====end

}
