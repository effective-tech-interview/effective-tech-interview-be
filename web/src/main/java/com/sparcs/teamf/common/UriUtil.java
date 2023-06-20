package com.sparcs.teamf.common;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UriUtil {

    public static URI build(String path, Object... uriVariableValues) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
            .path(path)
            .buildAndExpand(uriVariableValues)
            .toUri();
    }
}
