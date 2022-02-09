package com.springboot.app.commons;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


//CLASE75 : @EnableAutoConfiguration : SE EXCLUYE LA AUTOCONFIGURACIÓN DEL DATA SOURCE(SE EXCLUYE JPA).

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class SpringbootServicioCommonsApplication {

	//CLASE74
	//SE QUITA EL MAIN Y EL IMPORT.
}
