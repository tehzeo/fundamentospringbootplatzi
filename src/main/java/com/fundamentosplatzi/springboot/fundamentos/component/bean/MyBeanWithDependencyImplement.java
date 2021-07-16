package com.fundamentosplatzi.springboot.fundamentos.component.bean;

public class MyBeanWithDependencyImplement implements MyBeanWithDepency{

     private MyOperation myOperation;

    public MyBeanWithDependencyImplement(MyOperation myOperation){
        this.myOperation = myOperation;
    }

    @Override
    public void printWithDependency() {
        LOGGER.info("hemos ingresado al metodo printWithDependency");
        int numero=1;
        LOGGER.debug("el numero enviado como parametro es: " + number);
        System.out.println(myOperation.sum(numero));
        System.out.println("Hola desde la implementacion de un bean con Dependencia");
    }
}
