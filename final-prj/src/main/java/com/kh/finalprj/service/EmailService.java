package com.kh.finalprj.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kh.finalprj.configuration.EmailProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private EmailProperties emailProperties;
	
	
	//회원가입 링크 + 임시비밀번호 발송 메소드 
	public void invite(String empEmail, String tempPassword) throws IOException, MessagingException {
		
		String url = "http://localhost:5173/me";
				

		ClassPathResource resource = new ClassPathResource("templates/invite.html");
		File target = resource.getFile();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(target), StandardCharsets.UTF_8));
			String content = reader.lines()
								.collect(
										Collectors.joining(
												System.lineSeparator()
										)
								);
			
			
			Document document = Jsoup.parse(content);
			Elements password = document.select(".password-text");
			password.first().text(tempPassword);
			Elements link = document.select(".invite-link");
			link.attr("href", url);
			reader.close();
			
			
			
			MimeMessage message = sender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			
			helper.setFrom(emailProperties.getFrom());
			helper.setTo(empEmail);
			helper.setSubject("[KH정보교육원] 회원가입 링크가 도착하였습니다");
			helper.setText(document.toString(), true);
			
			sender.send(message);
			
	}

}
