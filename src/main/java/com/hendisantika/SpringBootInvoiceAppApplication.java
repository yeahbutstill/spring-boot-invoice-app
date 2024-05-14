package com.hendisantika;

import com.hendisantika.service.UploadFileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringBootInvoiceAppApplication implements CommandLineRunner {
    private final UploadFileService uploadFileService;
    private final BCryptPasswordEncoder passwordEncoder;

    public SpringBootInvoiceAppApplication(UploadFileService uploadFileService, BCryptPasswordEncoder passwordEncoder) {
        this.uploadFileService = uploadFileService;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootInvoiceAppApplication.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {
        uploadFileService.deleteAll();
        uploadFileService.init();

		/*String password = "12345";
		for(int i=0; i<2; i++) {
			String bcryptPassword = passwordEncoder.encode(password);
			System.out.println("bcryptPassword = " + bcryptPassword);
		}*/
    }
}
