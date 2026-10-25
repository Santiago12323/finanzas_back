package com.ingresos.back.domain.port;

import com.ingresos.back.domain.model.GoogleUserInfo;

public interface GoogleTokenVerifier {
    GoogleUserInfo verify(String idToken);
}