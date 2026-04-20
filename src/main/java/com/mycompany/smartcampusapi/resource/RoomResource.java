package com.mycompany.smartcampusapi.resource;

import com.mycompany.smartcampusapi.model.Room;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private static Map<String, Room> roomDB = new HashMap<>();

    @GET
    public Collection<Room> getRooms() {
        return roomDB.values();
    }

    @POST
    public Response createRoom(Room room) {
        roomDB.put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    @GET
    @Path("/{id}")
    public Response getRoom(@PathParam("id") String id) {
        Room room = roomDB.get(id);
        if (room == null) return Response.status(404).build();
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {
        Room room = roomDB.get(id);

        if (room == null) return Response.status(404).build();

        if (!room.getSensorIds().isEmpty()) {
            return Response.status(409).entity("Room has sensors").build();
        }

        roomDB.remove(id);
        return Response.ok("Deleted").build();
    }

    public static Map<String, Room> getRoomDB() {
        return roomDB;
    }
}