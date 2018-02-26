package cn.feignclient.credit_feign_web;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import feign.Feign;

@Configuration
public class CommonConfig {

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(1024L * 1024L * 10 * 10);
		return factory.createMultipartConfig();
	}
	
	@Bean
	@Scope("prototype")
	public Feign.Builder feignBuilder() {
	    return Feign.builder();
	}
}
