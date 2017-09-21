package cn.feignclient.credit_feign_web.domain;

import java.io.Serializable;

public class TokenUserDomain implements Serializable{

	private static final long serialVersionUID = 7358757345381118714L;
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
