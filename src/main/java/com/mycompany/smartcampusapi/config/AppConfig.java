package com.mycompany.smartcampusapi.config;


import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class AppConfig extends ResourceConfig {
    public AppConfig(){
        packages("com.mycompany.smartcampusapi");
    }
}
