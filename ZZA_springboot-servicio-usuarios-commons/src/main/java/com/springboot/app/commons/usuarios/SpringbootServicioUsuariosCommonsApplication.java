package com.springboot.app.commons.usuarios;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude= {DataSourceAutoConfiguration.class})    //SE EXCLUYE LA AUTOCONFIGURACIÓN DE SPRING DATA JPA.
public class SpringbootServicioUsuariosCommonsApplication {

	//CLASE88: SE QUITÓ EL MAIN.

}
