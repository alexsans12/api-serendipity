package com.serendipity.ecommerce.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;

import static com.twilio.rest.api.v2010.account.Message.creator;


public class SmsUtils {
    @Value("${twilio.phone-number}")
    public static String FROM_NUMBER = "+14157020957";
    @Value("${twilio.account-sid}")
    public static String ACCOUNT_SID = "AC0ffc9a251c4834f23f306010ed905522";
    @Value("${twilio.auth-token}")
    public static String AUTH_TOKEN = "07141543c643471dfb052a4adfa474bd";

    public static void sendSMS(String to, String messageBody) {
        System.out.println(FROM_NUMBER);
        System.out.println(ACCOUNT_SID);
        System.out.println(AUTH_TOKEN);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = creator(new PhoneNumber("+502" + to), new PhoneNumber(FROM_NUMBER), messageBody).create();
        System.out.println(message);
    }
}
