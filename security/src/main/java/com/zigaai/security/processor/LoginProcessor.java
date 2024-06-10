package com.zigaai.security.processor;

import com.zigaai.core.enumeration.LoginType;
import com.zigaai.core.strategy.Strategy;
import com.zigaai.security.model.LoginDTO;
import org.springframework.security.core.Authentication;

public interface LoginProcessor extends Strategy<LoginType> {

    Authentication buildUnauthenticated(LoginDTO params);

}
