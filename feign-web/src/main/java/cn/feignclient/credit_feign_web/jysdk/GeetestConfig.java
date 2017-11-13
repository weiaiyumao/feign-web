package cn.feignclient.credit_feign_web.jysdk;

/**
 * GeetestWeb配置文件
 * 
 *
 */
public class GeetestConfig {

	// 填入自己的captcha_id和private_key
	private static final String geetest_id = "5a13e89f56473ad6deed5fe31e5caa15";
	private static final String geetest_key = "baac4bbb88c9d2b17714919367143d0a";
	private static final boolean newfailback = true;

	public static final String getGeetest_id() {
		return geetest_id;
	}

	public static final String getGeetest_key() {
		return geetest_key;
	}
	
	public static final boolean isnewfailback() {
		return newfailback;
	}

}
