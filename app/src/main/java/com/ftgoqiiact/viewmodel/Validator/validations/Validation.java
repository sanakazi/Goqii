package com.ftgoqiiact.viewmodel.Validator.validations;

 
public interface Validation {

    String getErrorMessage();

    boolean isValid(String text);

}