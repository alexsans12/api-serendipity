package com.serendipity.ecommerce.utils;

import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import static nl.basjes.parse.useragent.UserAgent.*;

public class RequestUtils {
    public static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";
    public static final String USER_AGENT = "User-Agent";
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = "IP Desconocida";
        if (request != null) {
            ipAddress = request.getHeader(X_FORWARDED_FOR);
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getRemoteAddr();
            }
        }

        return ipAddress;
    }

    public static String getDevice(HttpServletRequest request) {
        UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .withCache(10000)
                .build();
        UserAgent agent = userAgentAnalyzer.parse(request.getHeader(USER_AGENT));
        //return agent.getValue(OPERATING_SYSTEM_NAME) + " - " + agent.getValue(AGENT_NAME) + " - " + agent.getValue(DEVICE_NAME);
        return agent.getValue(AGENT_NAME) + " - " + agent.getValue(DEVICE_NAME);
    }
}
