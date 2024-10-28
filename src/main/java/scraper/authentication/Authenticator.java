package scraper.authentication;

public interface Authenticator {
    void login(String username, String email, String password);
}
