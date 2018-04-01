package cn.abelib.shop;

import cn.abelib.shop.controller.filter.SessionExpireFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// SpringBoot移除了 web.xml Filter需要这里以Bean的形式配置
	@Bean
	public FilterRegistrationBean sessionFilter(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(new SessionExpireFilter());
		registrationBean.addUrlPatterns("");
		return registrationBean;
	}
}
