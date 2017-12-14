//package cn.feignclient.credit_feign_web;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import cn.feignclient.credit_feign_web.service.UserFeignService;
//import main.java.cn.common.BackResult;
//import main.java.cn.common.RedisKeys;
//import main.java.cn.common.ResultCode;
//import main.java.cn.domain.CreUserDomain;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)  
//@SpringBootTest(classes=FeignApplication.class)// 指定spring-boot的启动类   
//public class RedisTest {
//	
//	@Autowired
//	private UserFeignService userFeignService;
//	
//	@Autowired
//	private RedisTemplate<String,CreUserDomain> redisTemplate;
//	
//	@Test  
//    public void saveCreUser()  {  
//		String mobile = "13817367247";
//		CreUserDomain creuserdomain = new CreUserDomain();
//		String skey = RedisKeys.getInstance().getSessUserInfo(mobile);
//		creuserdomain = redisTemplate.opsForValue().get(skey);
//		if (null == creuserdomain) {
//			BackResult<CreUserDomain> result = userFeignService.findbyMobile(mobile);
//			
//			if (result.getResultCode().equals(ResultCode.RESULT_SUCCEED)) { 
//				creuserdomain = result.getResultObj();
//				System.out.println("数据库中读取数据手机号码：" + creuserdomain.getUserPhone());
//				redisTemplate.opsForValue().set(skey, creuserdomain, 30*60, TimeUnit.SECONDS);
//			}
//		} else {
//			System.out.println("缓存手机号码：" + creuserdomain.getUserPhone());
//		}
//		
//		
//		
//    } 
//	
//	@Test  
//    public void testList()  {  
//		String mobile = "13817367247";
//		CreUserDomain creuserdomain = new CreUserDomain();
//		
//		List<CreUserDomain> list = (List<CreUserDomain>) redisTemplate.opsForList().leftPop("listKeyTest");
//		
//		
//		
//		
//		if (null == list) {
//			BackResult<CreUserDomain> result = userFeignService.findbyMobile(mobile);
//			
//			if (result.getResultCode().equals(ResultCode.RESULT_SUCCEED)) { 
//				creuserdomain = result.getResultObj();
//				System.out.println("数据库中读取数据手机号码：" + creuserdomain.getUserPhone());
//				List<CreUserDomain> list1 = new ArrayList<CreUserDomain>();
//				list1.add(creuserdomain);
//				redisTemplate.opsForList().leftPushAll("listKeyTest", list1);
//			}
//		} else {
//			System.out.println("缓存手机号码：" + list.get(0).getUserPhone());
//		}
//		
//		
//		
//    } 
//	
//
//}
