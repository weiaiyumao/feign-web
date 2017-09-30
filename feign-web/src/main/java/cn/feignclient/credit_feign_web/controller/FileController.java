package cn.feignclient.credit_feign_web.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.feignclient.credit_feign_web.domain.FileDomain;
import cn.feignclient.credit_feign_web.utils.DateUtils;
import cn.feignclient.credit_feign_web.utils.UUIDTool;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController{
	private final static Logger logger = LoggerFactory.getLogger(FileController.class);

	@Value("${fielUrl}")
	private String fielUrl;

	// 文件上传相关代码
	@RequestMapping("/upload")
	@ResponseBody
	public BackResult<FileDomain> upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file, String mobile,String token) {

		response.setHeader("Access-Control-Allow-Origin", "*"); // 有效，前端可以访问
		response.setContentType("text/json;charset=UTF-8");
		
		BackResult<FileDomain> result = new BackResult<FileDomain>();
		
		if (!isLogin(mobile, token)) {
			result.setResultCode(ResultCode.RESULT_SESSION_STALED);
			result.setResultMsg("用户校验失败");
			return result;
		}
		
		
		if (file.isEmpty()) {
			logger.error("用户手机号：【" + mobile + "】执行文件上传出现异常文件不存在");
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("文件地址为空");
			return result;
		}

		// 获取文件名
		String fileName = file.getOriginalFilename();
		logger.info("上传的文件名为：" + fileName);
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		logger.info("上传的后缀名为：" + suffixName);
		// 文件上传后的路径
		String filePath = fielUrl + DateUtils.formatDate(new Date()) + "//";
		// 解决中文问题，liunx下中文路径，图片显示问题
		fileName = UUIDTool.getInstance().getUUID() + "_" + mobile + suffixName;
		File dest = new File(filePath + fileName); 
		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}

		LineNumberReader rf = null;
		
		try {

			file.transferTo(dest);

			File test = new File(filePath + fileName);
			long fileLength = test.length();
			

			rf = new LineNumberReader(new FileReader(test));
			int lines = 0;
			if (rf != null) {
				rf.skip(fileLength);
				lines = rf.getLineNumber();
				rf.close();
			}
			
			FileDomain fileDomain = new FileDomain();
			fileDomain.setFileUploadUrl(filePath + fileName);
			fileDomain.setTxtCount(lines);
			result.setResultObj(fileDomain);
			result.setResultMsg("上传成功");

		} catch (IllegalStateException e) {
			e.printStackTrace();
			logger.error("用户手机号：【" + mobile + "】执行文件上传出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("用户手机号：【" + mobile + "】执行文件上传出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");

			if (rf != null) {
				try {
					rf.close();
				} catch (IOException ee) {
					logger.error("用户手机号：【" + mobile + "】执行文件上传出现系统异常：" + ee.getMessage());
					result.setResultCode(ResultCode.RESULT_FAILED);
					result.setResultMsg("系统异常");
				}
			}
		}

		return result;
	}

	public static void main(String[] args) {
		System.out.println(DateUtils.formatDate(new Date()));
	}
}
