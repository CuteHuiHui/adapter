package com.dbapp.ahcloud.adapter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"com.dbapp.ahcloud.adapter.dao.**"})
@SpringBootApplication
public class AdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdapterApplication.class, args);
	}

}
