package cn.zzfyip.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.component.mail.MailSenderInfo;
import cn.zzfyip.search.component.mail.SimpleMailSender;

@Service
public class MailService {
	
	@Autowired
	GlobalConstant globalConstant;

	public void sendMail(String subject,String content,String attachFileName) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.163.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("zzfyweb2@163.com");
		mailInfo.setPassword("zzfy070110");// 您的邮箱密码
		mailInfo.setFromAddress("zzfyweb2@163.com");
		mailInfo.setToAddress("changsure@163.com;zzfyip@126.com;changsure312@gmail.com;");
		mailInfo.setAttachFilePath(globalConstant.getBasePath()+"temp/"+attachFileName);
		mailInfo.setAttachFileName(attachFileName);
		mailInfo.setSubject(subject);
		mailInfo.setContent(content);
		// 这个类主要来发送邮件
		SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式
	}
	
}
