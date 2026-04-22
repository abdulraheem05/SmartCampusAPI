package com.mycompany.smartcampusapi.mapper;

import com.mycompany.smartcampusapi.exception.SensorUnavailableException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;
import java.util.*;

@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {

    public Response toResponse(SensorUnavailableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return Response.status(403).entity(error).build();
    }
}