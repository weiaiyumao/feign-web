package cn.feignclient.credit_feign_web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.java.cn.domain.BackResult;
import main.java.cn.domain.CvsFilePathDomain;

@RestController
public class HiController {
	
	@Autowired
    SchedualServiceHi schedualServiceHi;
	
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        return schedualServiceHi.sayHiFromClientOne(name);
    }
    
    @RequestMapping(value = "/runTheTest", method = RequestMethod.GET)
	public BackResult<CvsFilePathDomain> runTheTest(@RequestParam(value = "fileUrl")String fileUrl,@RequestParam(value = "userId")String userId) {
		System.out.println("1111");
		// "D:/test/mk0001.txt", "1255"
		return schedualServiceHi.runTheTest(fileUrl, userId);
	}
}
