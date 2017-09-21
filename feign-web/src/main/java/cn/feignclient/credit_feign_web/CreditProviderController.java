package cn.feignclient.credit_feign_web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import main.java.cn.common.BackResult;
import main.java.cn.domain.CvsFilePathDomain;

@RestController
public class CreditProviderController {
	
	@Autowired
    CreditProviderService creditProviderService;
	
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    @ResponseBody
    public String sayHi(@RequestParam String name){
        return creditProviderService.sayHiFromClientOne(name);
    }
    
    @RequestMapping(value = "/runTheTest", method = RequestMethod.GET)
	public BackResult<CvsFilePathDomain> runTheTest(@RequestParam(value = "fileUrl")String fileUrl,@RequestParam(value = "userId")String userId) {
		
    	Assert.notNull(fileUrl, "The param fileUrl not be null!");
		Assert.notNull(userId, "The param userId not be null!");
		
		return creditProviderService.runTheTest(fileUrl, userId);
	} 
    
    
    
}
