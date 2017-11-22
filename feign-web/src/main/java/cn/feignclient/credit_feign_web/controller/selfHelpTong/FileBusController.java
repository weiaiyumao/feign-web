package cn.feignclient.credit_feign_web.controller.selfHelpTong;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.domain.FileDomain;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import cn.feignclient.credit_feign_web.utils.DateUtils;
import cn.feignclient.credit_feign_web.utils.UUIDTool;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.CreUserDomain;

/**
 * 自助通文件上传
 * 
 * @author ChuangLan
 *
 */
@RestController
@RequestMapping("/fileBus")
public class FileBusController extends BaseController {
	private final static Logger logger = LoggerFactory.getLogger(FileBusController.class);

	@Value("${fielUrl}")
	private String fielUrl;

	/**
	 * 文件上传
	 * 
	 * @param request
	 * @param file
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/upload11111")
	@ResponseBody
	public BackResult<FileDomain> upload111(HttpServletRequest request, String mobile, String mobiles) {

		logger.info("自助通手机号：" + mobile + "请求上传文件");

		// 转型为MultipartHttpRequest：
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 从其中取出一个文件 后续可使用spring 上传文件方法：file.transferTo(destFile);
		MultipartFile file = null;
		for (Iterator<String> it = multipartRequest.getFileNames(); it.hasNext();) {
			file = multipartRequest.getFile((String) it.next());
		}

		BackResult<FileDomain> result = new BackResult<FileDomain>();

		if (!checkSign(request)) {
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("签名验证失败");
			return result;
		}

		if (file.isEmpty()) {
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
		logger.info("上传的文件名为：" + fileName);
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
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

			CreUserDomain user = findByMobile(mobile);

			if (null == user) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败，系统不存在该手机号码的用户");
				return result;
			}

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
			logger.error("自助通手机号：" + mobile + "请求上传文件出现系统异常：" + e.getMessage());
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
					logger.error("自助通手机号：" + mobile + "请求上传文件出现系统异常：" + ee.getMessage());
					result.setResultCode(ResultCode.RESULT_FAILED);
					result.setResultMsg("系统异常");
				}
			}
		}

		return result;
	}

	/**
	 * 文件上传
	 * 
	 * @param request
	 * @param file
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public BackResult<FileDomain> upload(HttpServletRequest request, String mobile, String mobiles) {

		logger.info("自助通手机号：" + mobile + "请求上传文件");

		BackResult<FileDomain> result = new BackResult<FileDomain>();

		if (!checkSign(request)) {
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("签名验证失败");
			return result;
		}
		
		if (CommonUtils.isNotString(mobile)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("手机号码不能为空");
			return result;
		}
		
		if (CommonUtils.isNotString(mobiles)) {
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg("检测的手机号码串不能为空");
			return result;
		}

		// 文件上传后的路径
		String filePath = fielUrl + DateUtils.formatDate(new Date()) + "//";
		// 解决中文问题，liunx下中文路径，图片显示问题
		String fileName = UUIDTool.getInstance().getUUID() + "_" + mobile + ".txt";
		File dest = new File(filePath + fileName);
		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}

		LineNumberReader rf = null;

		try {

			CreUserDomain user = findByMobile(mobile);

			if (null == user) {
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败，系统不存在该手机号码的用户");
				return result;
			}

			// 写入文件
			Boolean fag = this.contentToTxt(filePath + fileName, mobiles);

			if (!fag) {
				result.setResultCode(ResultCode.RESULT_FAILED);
				result.setResultMsg("写入文件系统异常！");
				return result;
			}
			
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

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自助通手机号：" + mobile + "请求上传文件出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		} 

		return result;
	}

	public Boolean contentToTxt(String filePath, String content) {
		try {
			String[] mobiles = content.split(",");
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath), true));
			for (String string : mobiles) {
				if (CommonUtils.isNotString(string)) {
					continue;
				}

				// 验证是否为正常的１１位有效数字
				if (!CommonUtils.isNumeric(string)) {
					continue;
				}
				writer.write(string + "\n");
			}
			writer.close();
			return Boolean.TRUE;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

}
