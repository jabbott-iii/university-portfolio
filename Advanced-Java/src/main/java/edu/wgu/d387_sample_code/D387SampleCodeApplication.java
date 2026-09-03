package edu.wgu.d387_sample_code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

@SpringBootApplication
public class D387SampleCodeApplication {

	static ExecutorService messageExecutor=newFixedThreadPool(5);
	public static void main(String[] args) {

		SpringApplication.run(D387SampleCodeApplication.class, args);
		Properties properties=new Properties();
		messageExecutor.execute(()->{
			try {
				InputStream stream = new
                        ClassPathResource("message_en.properties").getInputStream();
				properties.load(stream);
				System.out.println(properties.getProperty("greeting"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		messageExecutor.execute(()->{
			try {
				InputStream stream = new
                        ClassPathResource("message_fr.properties").getInputStream();
				properties.load(stream);
				System.out.println(properties.getProperty("greeting"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
