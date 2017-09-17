package cn.feignclient.credit_feign_web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.java.cn.domain.BackResult;
import main.java.cn.domain.TrdOrderDomain;
import main.java.cn.domain.UserAccountDomain;

@RestController
public class UserProviderController {
	
	@Autowired
    private UserProviderService userProviderService;
	
    @RequestMapping(value = "/user/findbyMobile",method = RequestMethod.GET)
    public BackResult<UserAccountDomain> findbyMobile(@RequestParam String mobile){
    	
    	Assert.notNull(mobile, "The param mobile not be null!");
    	
        return userProviderService.findbyMobile(mobile);
    }
    
    @RequestMapping(value = "/user/rechargeOrRefunds", method = RequestMethod.GET)
	public BackResult<Boolean> rechargeOrRefunds(TrdOrderDomain trdOrderDomain) {
		
    	Assert.notNull(trdOrderDomain, "The param trdOrderDomain not be null!");
		Assert.notNull(trdOrderDomain.getCreUserId(), "The param creUserId not be null!");
		Assert.notNull(trdOrderDomain.getClOrderNo(), "The param clOrderNo not be null!");
		Assert.notNull(trdOrderDomain.getNumber(), "The param number not be null!");
		Assert.notNull(trdOrderDomain.getMoney(), "The param money not be null!");
		Assert.notNull(trdOrderDomain.getPayType(), "The param payType not be null!");
		Assert.notNull(trdOrderDomain.getPayTime(), "The param payTime not be null!");
		Assert.notNull(trdOrderDomain.getType(), "The param type not be null!");
		
		return userProviderService.rechargeOrRefunds(trdOrderDomain);
	} 
    
    
    
}
