package com.mycompany.smartcampusapi.resource;

import com.mycompany.smartcampusapi.model.Sensor;
import com.mycompany.smartcampusapi.model.SensorReading;
import com.mycompany.smartcampusapi.exception.SensorUnavailableException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;

    private static Map<String, List<SensorReading>> readingDB = new HashMap<>();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET readings
    @GET
    public List<SensorReading> getReadings() {
        return readingDB.getOrDefault(sensorId, new ArrayList<>());
    }

    // POST reading
    @POST
    public Response addReading(SensorReading reading) {

        Map<String, Sensor> sensorDB = SensorResource.getSensorDB();

        Sensor sensor = sensorDB.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found"))
                    .build();
        }

        // BUSINESS RULE
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is under maintenance and cannot accept readings");
        }

        readingDB.putIfAbsent(sensorId, new ArrayList<>());
        readingDB.get(sensorId).add(reading);

        // SIDE EFFECT
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}