package pe.tuna.app.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthoritiesMixin {

    // Anotacion propia de pai jackson para trabajar con Json
    @JsonCreator
    public SimpleGrantedAuthoritiesMixin(@JsonProperty("authority") String role) {
    }
}
