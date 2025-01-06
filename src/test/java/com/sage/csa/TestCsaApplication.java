package com.sage.csa;

import org.springframework.boot.SpringApplication;

public class TestCsaApplication {

	public static void main(String[] args) {
		SpringApplication.from(CsaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
