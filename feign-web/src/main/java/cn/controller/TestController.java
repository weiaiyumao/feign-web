//package cn.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import cn.feignApi.RestForeignService;
//import main.java.cn.domain.BackResult;
//import main.java.cn.domain.CvsFilePathDomain;
//
//@RestController
//public class TestController {
//
//	@Autowired
//	private RestForeignService restForeignService;
//
//	@RequestMapping(value = "/runTheTest", method = RequestMethod.GET)
//	public BackResult<CvsFilePathDomain> sayHi(@RequestParam(value = "fileUrl")String fileUrl,@RequestParam(value = "userId")String userId) {
//		System.out.println("1111");
//		// "D:/test/mk0001.txt", "1255"
//		return restForeignService.runTheTest(fileUrl, userId);
//	}
//}
