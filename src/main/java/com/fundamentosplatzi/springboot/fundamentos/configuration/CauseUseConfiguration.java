package com.fundamentosplatzi.springboot.fundamentos.configuration;

import com.fundamentos.springboot.fundamentos.caseuse.GetUser;
import com.fundamentos.springboot.fundamentos.caseuse.GetUserImplement;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.springframework.context.annotation.Bean;

public class CaseUseConfiguration {

    @Bean
    GetUser getUser(UserService userService){
        return new GetUserImplement(userService);
    }
}