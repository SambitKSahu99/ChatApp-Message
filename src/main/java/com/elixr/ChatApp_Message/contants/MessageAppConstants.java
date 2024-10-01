package com.elixr.ChatApp_Message.contants;

public class MessageAppConstants {
    public static final String ALLOWED_HEADERS = "*";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CLIENT_ERROR = "Client Error: ";
    public static final String DATE_FORMAT = "dd:MM:yyyy HH:mm:ss";
    public static final String EXPIRED = "expired";
    public static final String INVALID = "invalid";
    public static final String MESSAGE_COLLECTION = "messages";
    public static final int MAXIMUM_CHARACTER_LIMIT = 250;
    public static final String REGISTERED_CORS_PATTERN = "/**";
    public static final String SENDER_USERNAME = "senderUserName";
    public static final String SERVER_ERROR = "Server Error: ";
    public static final String RECEIVER_USERNAME = "receiverUserName";
    public static final String VERIFY_TOKEN_ENDPOINT = "/verifyToken";
    public static final String MESSAGE_ENDPOINT = "/message";
    private static final String AUTH_BASEURL = "auth.baseurl";
    public static final String AUTH_URL_VALUE = "${"+AUTH_BASEURL +"}";
    private static final String UI_BASEURL = "ui.baseurl";
    public static final String UI_URL_VALUE = "${"+UI_BASEURL +"}";
    private static final String USER_BASEURL = "user.baseurl";
    public static final String USER_URL_VALUE = "${"+USER_BASEURL +"}";

}
