package com.serendipity.ecommerce.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;

import static com.twilio.rest.api.v2010.account.Message.creator;


public class SmsUtils {
    @Value("${twilio.phone-number}")
    public static String FROM_NUMBER;
    @Value("${twilio.account-sid}")
    public static String ACCOUNT_SID;
    @Value("${twilio.auth-token}")
    public static String AUTH_TOKEN;

    public static void sendSMS(String to, String messageBody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = creator(new PhoneNumber("+502" + to), new PhoneNumber(FROM_NUMBER), messageBody).create();
        System.out.println(message);
    }
}
