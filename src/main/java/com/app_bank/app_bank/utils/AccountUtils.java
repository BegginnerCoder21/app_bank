package com.app_bank.app_bank.utils;

import com.app_bank.app_bank.entity.User;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "Un utilisateur a déjà été crée avec ce email";
    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Votre compte a été crée avec succès";
    public static final String ACCOUNT_CREATION_MAIL_SUBJECT = "Créaction de compte.";

    public static String generateAccountNumber(){

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String year = String.valueOf(currentYear);
        String randNumber = String.valueOf(randomNumber);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randNumber).toString();

    }

    public static String fullNameUser(User saveUser){
        return saveUser.getFirstname() + " " + saveUser.getLastname() + " " + saveUser.getOtherName();
    }
}
