package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        for (; ;) {
            System.out.println("\nДля выхода введите exit\n");
            System.out.println("Введите дату в формате dd/MM/yyyy, например 20/10/2021: ");
            Scanner input = new Scanner(System.in);
            String date = input.next();
            if(date.equals("exit")){
                input.close();
                break;
            }
            System.out.println("Введите идентификатор валюты, например USD: ");
            String currency = input.next();
            if(currency.equals("exit")){
                input.close();
                break;
            }
            API.getResponse(date, currency);
        }
    }
}
