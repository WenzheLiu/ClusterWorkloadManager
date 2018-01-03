package org.wenzhe.cwm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.wenzhe.cwm.console.ConsoleMain;

@SpringBootApplication
public class CwmServerApplication {

	public static void main(String[] args) {
		if (System.getProperty("cwm.console") != null) {
			ConsoleMain.main(args);
		} else {
			SpringApplication.run(CwmServerApplication.class, args);
		}
	}
}
