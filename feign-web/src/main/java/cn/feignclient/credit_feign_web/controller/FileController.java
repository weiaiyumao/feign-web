package cn.feignclient.credit_feign_web.controller;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.feignclient.credit_feign_web.service.CreditProviderService;
import cn.feignclient.credit_feign_web.utils.DateUtils;
import cn.feignclient.credit_feign_web.utils.FileUtils;
import cn.feignclient.credit_feign_web.utils.UUIDTool;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.FileUploadDomain;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
	private final static Logger logger = LoggerFactory.getLogger(FileController.class);

	@Value("${fielUrl}")
	private String fielUrl;

	@Autowired
	private CreditProviderService creditProviderService;

	// 文件上传相关代码
	@RequestMapping("/upload")
	@ResponseBody
	public BackResult<FileUploadDomain> upload(HttpServletRequest request, HttpServletResponse response,
			MultipartFile file, String mobile, String token) {

		BackResult<FileUploadDomain> result = new BackResult<FileUploadDomain>();

		if (!isLogin(mobile, token)) {
			result.setResultCode(ResultCode.RESULT_SESSION_STALED);
			result.setResultMsg("用户校验失败");
			return result;
		}

		if (null == file || file.isEmpty()) {
			logger.error("用户手机号：【" + mobile + "】执行文件上传出现异常文件不存在");
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("文件地址为空");
			return result;
		}
		
		if (file.getSize() > 10485760) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("上传文件过大最大10M 10485760kb");
			return result;
		}

		// 获取文件名
		String fileName = file.getOriginalFilename();
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		
		if (!suffixName.equalsIgnoreCase("txt")) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("文件格式不正常");
			return result;
		}
		
		// 文件上传后的路径
		String filePath = fielUrl + DateUtils.formatDate(new Date()) + "//";
		
		// 解决中文问题，liunx下中文路径，图片显示问题
		fileName = UUIDTool.getInstance().getUUID() + "_" + mobile + suffixName;
		File dest = new File(filePath + fileName);
		
		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}

		try {

			file.transferTo(dest);
			// 文件名存入数据库
			FileUploadDomain domain = new FileUploadDomain();
			domain.setUserId(this.findByMobile(mobile).getId().toString());
			domain.setFileName(file.getOriginalFilename());
			domain.setFileRows(FileUtils.getFileLinesNotNullRow(filePath + fileName));
			domain.setFileUploadUrl(filePath + fileName);
			BackResult<FileUploadDomain> resultFileUpload = creditProviderService.saveFileUpload(domain);
			if (!resultFileUpload.getResultCode().equals(ResultCode.RESULT_SUCCEED)) {
				result = new BackResult<>(resultFileUpload.getResultCode(), resultFileUpload.getResultMsg());
			}

			result.setResultObj(resultFileUpload.getResultObj());
			result.setResultMsg("上传成功");
			
			logger.info("用户手机号：" + "【" + mobile + "】执行文件上传(" + fileName + suffixName + ")成功!");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户手机号：【" + mobile + "】执行文件上传出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}

		return result;
	}

	public static void main(String[] args) {
		System.out.println(DateUtils.formatDate(new Date()));
	}
}
