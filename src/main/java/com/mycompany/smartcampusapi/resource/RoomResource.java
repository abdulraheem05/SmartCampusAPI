package com.mycompany.smartcampusapi.resource;

import com.mycompany.smartcampusapi.model.Room;
import com.mycompany.smartcampusapi.exception.RoomNotEmptyException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private static Map<String, Room> roomDB = new HashMap<>();

    // GET all rooms
    @GET
    public Collection<Room> getRooms() {
        return roomDB.values();
    }

    // POST create room
    @POST
    public Response createRoom(Room room) {
        roomDB.put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // GET single room
    @GET
    @Path("/{id}")
    public Response getRoom(@PathParam("id") String id) {

        Room room = roomDB.get(id);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }

        return Response.ok(room).build();
    }

    // DELETE room
    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {

        Room room = roomDB.get(id);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }

        // BUSINESS RULE
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room has active sensors and cannot be deleted");
        }

        roomDB.remove(id);

        return Response.ok(Map.of("message", "Room deleted successfully")).build();
    }

    // Utility method for SensorResource
    public static Map<String, Room> getRoomDB() {
        return roomDB;
    }
}