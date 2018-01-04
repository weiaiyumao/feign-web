package cn.feignclient.credit_feign_web.controller.tds;

import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.feignclient.credit_feign_web.controller.BaseController;
import cn.feignclient.credit_feign_web.domain.FileDomain;
import cn.feignclient.credit_feign_web.service.tds.TdsUserFeignService;
import cn.feignclient.credit_feign_web.utils.CommonUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.tds.TdsUserDomain;


@RestController
@RequestMapping("/fileTds")
public class FileTdsController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(FileTdsController.class);

	@Value("${fielHeadimg}")
	private String fielHeadimg;
	
	@Autowired
	private TdsUserFeignService tdsUserFeignService;

	/**
	 * 文件,头像上传
	 * 
	 * @param request
	 * @param file
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/uploadfile")
	@ResponseBody
	public BackResult<FileDomain> uploadfile(HttpServletRequest request, MultipartFile file, String mobile,String token) {
		BackResult<FileDomain> result = new BackResult<FileDomain>();
		try {
			
			if (CommonUtils.isNotString(token)) {
				 return new BackResult<FileDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"token不能为空");
			}
			
			if(null==file)
				 return new BackResult<FileDomain>(ResultCode.RESULT_PARAM_EXCEPTIONS,"没有上传文件");
			
			TdsUserDomain tdsUser=this.getUserInfo(mobile);
			if(null==tdsUser){
				result.setResultCode(ResultCode.RESULT_SESSION_STALED);
				result.setResultMsg("用户校验失败，系统不存在该手机号码的用户");
				return result;
			}
			FileDomain fileDomain = new FileDomain();
			// 创建文件
			File dir = new File(fielHeadimg);
			if (!dir.exists()) {
				dir.mkdirs();
			}
            
			// 返回原来在客户端的文件系统的文件名
			String fileName = file.getOriginalFilename();
			// xxxx.后缀
			String img = "uid_"+tdsUser.getId()+"_"+mobile + fileName.substring(fileName.lastIndexOf("."));
			FileOutputStream imgOut = new FileOutputStream(new File(dir, img));
			// 根据 dir 抽象路径名和 img 路径名字符串创建一个新 File 实例。															
			imgOut.write(file.getBytes());// 返回一个字节数组文件的内容
			imgOut.close();
			String fileUploadUrl =fielHeadimg +img;
			
			//保存头像图片
			tdsUserFeignService.updateHeadImg(tdsUser.getId(), fileUploadUrl);
			
			fileDomain.setTxtCount(1);
			fileDomain.setFileUploadUrl(fileUploadUrl);
			result.setResultObj(fileDomain);
			logger.info("=====用户:" + mobile + "上传头像======");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户手机号：【" + mobile + "】执行文件上传出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}
		return result;
	}
	
	
}
