package cn.abelib.biz;

import cn.abelib.biz.controller.filter.SessionExpireFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author abel
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * SpringBoot移除了 web.xml Filter需要这里以Bean的形式配置
	 * @return
	 */
	@Bean
	public FilterRegistrationBean sessionFilter(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(new SessionExpireFilter());
		registrationBean.addUrlPatterns("/***");
		return registrationBean;
	}
}
