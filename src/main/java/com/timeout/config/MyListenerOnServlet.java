package com.timeout.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.annotation.Configuration;

import com.timeout.thread.TestSubscribe;

@Configuration
@WebListener
public class MyListenerOnServlet implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("ServletContext容器初始化了。。。");
		Thread threadOnRedis = new TestSubscribe();
		threadOnRedis.start();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("ServletContext容器销毁了。。。");
	}

}
