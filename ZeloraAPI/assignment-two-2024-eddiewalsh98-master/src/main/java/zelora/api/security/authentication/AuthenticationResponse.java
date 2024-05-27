package zelora.api.security.authentication;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String jwt;
}

