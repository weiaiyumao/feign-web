package cn.feignclient.credit_feign_web.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.ApiResultDomain;

@Component
public class OpenApiServiceHiHystric implements OpenApiService {

	@Override
	public BackResult<List<ApiResultDomain>> openApi(JSONObject paramJsonString) {
		return new BackResult<>(ResultCode.RESULT_FAILED, "carriersService服务openApi出现异常");
	}

}
