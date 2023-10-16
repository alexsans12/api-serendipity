package com.serendipity.ecommerce.constant;

public class Constants {
    // Security constants
    public static final String[] PUBLIC_URLS = {
            "/api/v1/usuario/login/**",
            "/api/v1/usuario/register/**",
            "/api/v1/usuario/verify/code/**",
            "/api/v1/usuario/reset-password/**",
            "/api/v1/usuario/verify/password/**",
            "/api/v1/usuario/verify/account/**",
            "/api/v1/usuario/refresh/token/**",
            "/api/v1/usuario/image/**",
            "/api/v1/usuario/new/password/**",
            "/api/v1/producto/list/**",
            "/api/v1/producto/get/**",
            "/api/v1/producto/sku/**",
            "/api/v1/producto/search/**",
            "/api/v1/producto/marca/**",
            "/api/v1/producto/categoria/**",
            "/api/v1/producto/image/**",
            "/api/v1/category/list/**",
            "/api/v1/review/get-reviews/**"
    };

    // Security filter constants
    public static final String HTTP_OPTIONS_METHOD = "OPTIONS";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String[] PUBLIC_ROUTES = {
            "/api/v1/usuario/login",
            "/api/v1/usuario/register",
            "/api/v1/usuario/verify/code",
            "/api/v1/usuario/verify/account",
            "/api/v1/usuario/verify/password",
            "/api/v1/usuario/refresh/token",
            "/api/v1/usuario/image",
            "/api/v1/usuario/new/password",
            "/api/v1/producto/list",
            "/api/v1/producto/get",
            "/api/v1/producto/sku",
            "/api/v1/producto/search",
            "/api/v1/producto/marca",
            "/api/v1/producto/categoria",
            "/api/v1/producto/image",
            "/api/v1/category/list",
            "/api/v1/review/get-reviews"
    };

    // Token provider constants
    public static final String SERENDIPITY_LLC = "SERENDIPITY_LLC";
    public static final String ECOMMERCE_SERVICE = "ECOMMERCE_SERVICE";
    public static final String AUTHORITIES = "authorities";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 432_000_000; //1_800_000; // 30 minutes
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000; // 5 days
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token no puede ser verificado";

    // Format constants
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    // Request utils constants
    public static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";
    public static final String USER_AGENT = "User-Agent";
}
