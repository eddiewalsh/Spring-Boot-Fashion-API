package zelora.api.security.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;

    public AuthenticationRequest(@JsonProperty("username") String username,
                                 @JsonProperty("password") String password){
        this.username = username;
        this.password = password;
    }

    public AuthenticationRequest(){
    }
}

