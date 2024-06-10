package com.zigaai.oauth2.repo;

import com.zigaai.core.model.security.Oauth2RegisteredClientModel;

public interface Oauth2RegisteredClientMapper {

    Oauth2RegisteredClientModel getByClientId(String clientId);

}
