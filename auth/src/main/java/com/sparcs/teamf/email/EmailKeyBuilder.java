package com.sparcs.teamf.email;

import com.sparcs.teamf.emailauth.Event;
import org.springframework.stereotype.Component;

@Component
public class EmailKeyBuilder {

    private static final String EMAIL_KEY_FORMAT = "%s:%s";

    public String generate(String email, Event event) {
        return String.format(EMAIL_KEY_FORMAT, email, event);
    }
}
