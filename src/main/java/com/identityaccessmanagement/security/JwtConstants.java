package com.identityaccessmanagement.security;

public class JwtConstants {

    public static final long ACCESS_TOKEN_EXPIRATION = 900000;      //15 min

    public static final long REFRESH_TOKEN_EXPIRATION = 604800000;  //7 days

    public static final String TOKEN_PREFIX = "Bearer ";

}
