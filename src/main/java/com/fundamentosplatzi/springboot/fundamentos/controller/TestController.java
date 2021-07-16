package com.fundamentosplatzi.springboot.fundamentos.controller;


import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {


    Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

    private ComponentDependency componentDependency;
    private MyBean myBean;
    private MyBeanWithDependency myBeanWithDependency;
    private MyBeanWithProperties myBeanWithProperties;
    private UserPojo userPojo;
    private UserRepository userRepository;
    private UserService userService;

    public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService userService){

        this.componentDependency = componentDependency;
        this.myBean = myBean;
        this.myBeanWithDependency = myBeanWithDependency;
        this.myBeanWithProperties = myBeanWithProperties;
        this.userPojo = userPojo;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(FundamentosApplication.class, args);
    }


    @Override
    public void run(String... args) {
        //ejemplosAnteriores();
        saveUsersInDataBase();
        getInformationJpsqlFromUser();
        saveWithErrorTransactional();
    }

    private void getInformationJpsqlFromUser(){
        LOGGER.info("user with findByUserEmail" +  userRepository.findByUserEmail("Santiago@mail.com")
                .orElseThrow(() -> new RuntimeException("no se encontro el usuario")));

        userRepository.findAndSort("Mig", Sort.by("id").descending())
                .stream()
                .forEach(user -> LOGGER.info("User with sort method" + user));

        userRepository.findByName("Edgard")
                .stream()
                .forEach(user -> LOGGER.info("user with query method" + user));

        LOGGER.info("usuario con query method findByEmailAndName" + userRepository.findByEmailAndName("ed@mail.com", "Edgard")
                .orElseThrow(() -> new RuntimeException("usuario no encontrado")));


        userRepository.findByNameLike("%Mi%")
                .stream()
                .forEach(user -> LOGGER.info("usuario findByNameLike" + user));

        userRepository.findByNameOrEmail(null, "ed@mail.com")
                .stream()
                .forEach(user -> LOGGER.info("usuario findByNameOrEmail" + user));


        userRepository.findByBirthDateBetween(LocalDate.of(2021, 01, 01), LocalDate.of(2021, 12, 31))
                .stream()
                .forEach(user -> LOGGER.info("usuario con intervalo de fechas "+ user));


        userRepository.findByNameLikeOrderByIdDesc("%Miguel%")
                .stream()
                .forEach(user -> LOGGER.info("usuario encontrado con like y ordenado "+ user));


        LOGGER.info("el usuario a partir del named parameter es: " + userRepository.getAllByBirthDateAndEmail(LocalDate.of(2021, 07, 15),
                "Santiago2@mail.com")
                .orElseThrow(() -> new RuntimeException("no se encontro usuario a partir del named parameter")));
    }

    private void saveWithErrorTransactional(){
        User test1 = new User("TestTransactional1", "test1@mail.com", LocalDate.now());
        User test2 = new User("TestTransactional2", "test2@mail.com", LocalDate.now());
        User test3 = new User("TestTransactional3", "test1@mail.com", LocalDate.now());
        User test4 = new User("TestTransactional4", "test4@mail.com", LocalDate.now());

        List<User> users = Arrays.asList(test1, test2, test3, test4);

        try {
            userService.saveTransactional(users);
        }catch (Exception e){
            LOGGER.error("esta es una excepcion dentro del metodo transaccional" + e.getMessage());
        }
        userService.getAllUsers()
                .stream()
                .forEach(user ->
                        LOGGER.info("usuario dentro de transaccional" + user));
    }

    private void saveUsersInDataBase(){
        User user1 = new User("Miguel", "Santiago@mail.com", LocalDate.of(2021, 07, 13));
        User user2 = new User("Miguel2", "Santiago2@mail.com", LocalDate.of(2021, 07, 15));
        User user3 = new User("Edgard", "ed@mail.com", LocalDate.of(2021, 8, 01));

        List<User> list = Arrays.asList(user1, user2, user3);
        list.stream().forEach(userRepository::save);
    }

    private void ejemplosAnteriores(){
        componentDependency.saludar();
        myBean.print();
        myBeanWithDependency.printWithDependency();
        System.out.println(myBeanWithProperties.function());
        System.out.println(userPojo.getEmail() +" "+ userPojo.getPassword());
        try{
            int value = 10/0;
            LOGGER.debug("my valor: "+ value);
        }catch (Exception e){
            LOGGER.error("es un error al dividir por cero: " + e.getMessage());
        }
    }
}