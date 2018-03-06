package cn.feignclient.credit_feign_web.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.alibaba.fastjson.JSONObject;

import main.java.cn.common.BackResult;
import main.java.cn.domain.ApiResultDomain;

/**
 * 飓金荣通api调用
 * @author liuh
 *
 */
@FeignClient(value = "carriersService")
public interface OpenApiService{
	
	@RequestMapping(value = "/chuanglan/openApi", method = RequestMethod.POST)
	BackResult<List<ApiResultDomain>> openApi(@RequestBody JSONObject paramJsonString);
}
