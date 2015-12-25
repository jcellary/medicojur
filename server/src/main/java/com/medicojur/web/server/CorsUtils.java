package com.medicojur.web.server;

import javax.ws.rs.core.Response;

//TOOD Refactor and move to a global jersey filter instead of manual invocation
public class CorsUtils {
  public static Response.ResponseBuilder addCorsHeaders(Response.ResponseBuilder response) {
    return response
        //TODO define proper domain instead of *
        .header("Access-Control-Allow-Origin", "*")
        .header("Access-Control-Allow-Credentials", "true")
        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD")
        .header("Access-Control-Allow-Headers",
                "Content-Type, Accept, X-Requested-With, Authorization");
  }
}
