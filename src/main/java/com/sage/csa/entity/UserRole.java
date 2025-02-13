package com.sage.csa.entity;

import lombok.Getter;


public enum UserRole {
    USER("USGP_user"),
    ADMIN("USGP_admin");

    private final String code;

    UserRole(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
