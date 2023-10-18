package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.enumeration.VerificationType;

public interface EmailService {
    void sendVerificationEmail(String nombre, String email, String verificationUrl, VerificationType verificationType);
}
