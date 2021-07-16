package com.fundamentosplatzi.springboot.fundamentos.component.bean;

public class MyBeanImplement implements MyBean{
    @Override
    public void print() {
        System.out.println("Hola desde Bean!");
    }
}
