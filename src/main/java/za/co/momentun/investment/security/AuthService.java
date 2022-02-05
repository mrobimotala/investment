package za.co.momentun.investment.security;

public interface AuthService {
    Boolean validateBasicAuthentication(String userName, String password, String basicAuthHeaderValue);
}