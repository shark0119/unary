package com.cn.unary.initcopy.logger;

import org.slf4j.Logger;

public class LoggerTest {

	public static Logger getLogger () {
		StackTraceElement [] trace = Thread.currentThread().getStackTrace();
		for (StackTraceElement ste : trace) {
			System.out.println(ste.getClassName());
		}
		
		return null;
	}
	public static void main (String [] args) {
		try {
			/*AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanConfig.class);
			Base b = ac.getBean(Base.class);
			b.log();
			Base1 b1 = ac.getBean(Base1.class);
			b1.log();
			
			ac.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}



	