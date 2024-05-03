package com.RestApi.task.TestRestApi;

import liquibase.integration.spring.SpringLiquibase;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

@SpringBootApplication
public class TestRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestRestApiApplication.class, args);
	}


	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public SpringLiquibase liquibase(DataSource dataSource, ResourceLoader resourceLoader) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("db/changelog/db.changelog-master.yaml");
		liquibase.setResourceLoader(resourceLoader);
		return liquibase;
	}

}
