package com.mycompany.smartcampusapi.resource;

import com.mycompany.smartcampusapi.model.Sensor;
import com.mycompany.smartcampusapi.model.Room;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private static Map<String, Sensor> sensorDB = new HashMap<>();

    @POST
    public Response createSensor(Sensor sensor) {

        Map<String, Room> roomDB = RoomResource.getRoomDB();

        if (!roomDB.containsKey(sensor.getRoomId())) {
            return Response.status(422).entity("Room does not exist").build();
        }

        sensorDB.put(sensor.getId(), sensor);

        Room room = roomDB.get(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());

        return Response.status(201).entity(sensor).build();
    }

    @GET
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null) return sensorDB.values();

        List<Sensor> filtered = new ArrayList<>();

        for (Sensor s : sensorDB.values()) {
            if (s.getType().equalsIgnoreCase(type)) {
                filtered.add(s);
            }
        }

        return filtered;
    }
}