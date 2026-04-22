package com.mycompany.smartcampusapi.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;
import java.util.*;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    public Response toResponse(Throwable ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal server error");

        return Response.status(500).entity(error).build();
    }
}