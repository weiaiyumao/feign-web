package cn.feignclient.credit_feign_web.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * http://www.cnblogs.com/0201zcr/p/5942748.html
 * @author ChuangLan
 *
 */
public class Test {

	public static void main(String[] args) {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		RedisLock lock = new RedisLock(redisTemplate, "13817367247", 0, 0);
		
		try {
			System.out.println(lock.lock());
			// 处理加锁业务
//			if (lock.lock()) {
//				System.out.println();
//			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			//为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，            //操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。 ————这里没有做
            lock.unlock();
		}
	}
}
