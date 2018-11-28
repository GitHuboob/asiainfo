package com.asiainfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication //核心注解，开启了springboot的自动配置功能
@EnableTransactionManagement //开启事务管理
@ServletComponentScan(basePackages={"com.asiainfo.filter"})//自动创建......bean
@EnableScheduling //开启Spring Task
public class Application /*extends SpringBootServletInitializer*/ {

    // 部署war需要继承SpringBootServletInitializer
	/*@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}*/

	public static void main(String[] args) {
		//启动了springboot程序
		SpringApplication.run(Application.class, args);
	}
}