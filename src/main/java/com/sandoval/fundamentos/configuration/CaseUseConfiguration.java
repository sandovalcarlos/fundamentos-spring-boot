package com.sandoval.fundamentos.configuration;

import com.sandoval.fundamentos.caseuse.GetUser;
import com.sandoval.fundamentos.caseuse.GetUserImplement;
import com.sandoval.fundamentos.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaseUseConfiguration {

    @Bean
    GetUser getUser(UserService userService) {
        return new GetUserImplement(userService);
    }
}
