package com.mycompany.smartcampusapi.resource;

import com.mycompany.smartcampusapi.model.Sensor;
import com.mycompany.smartcampusapi.model.Room;
import com.mycompany.smartcampusapi.exception.LinkedResourceNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private static Map<String, Sensor> sensorDB = new HashMap<>();

    // POST create sensor
    @POST
    public Response createSensor(Sensor sensor) {

        Map<String, Room> roomDB = RoomResource.getRoomDB();

        // VALIDATION
        if (!roomDB.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room does not exist for given sensor");
        }

        sensorDB.put(sensor.getId(), sensor);

        // Link sensor to room
        Room room = roomDB.get(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    // GET sensors (with optional filtering)
    @GET
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null) {
            return sensorDB.values();
        }

        List<Sensor> filtered = new ArrayList<>();

        for (Sensor s : sensorDB.values()) {
            if (s.getType().equalsIgnoreCase(type)) {
                filtered.add(s);
            }
        }

        return filtered;
    }

    // SUB-RESOURCE LOCATOR
    @Path("/{id}/readings")
    public SensorReadingResource getReadingResource(@PathParam("id") String id) {
        return new SensorReadingResource(id);
    }

    // Utility for other classes
    public static Map<String, Sensor> getSensorDB() {
        return sensorDB;
    }
}