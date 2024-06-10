package com.zigaai.core.model.security;

public interface AuthenticationModel {

    Long getId();

    String getUsername();

    String getPassword();

    String getSalt();

    Boolean getDeleted();

    String getUserType();

}
