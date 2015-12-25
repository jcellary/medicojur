package com.medicojur.web.filters;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

//TODO finish filter configuration - currently not active
public class CORSFilter implements ContainerResponseFilter {

  @Override
  public ContainerResponse filter(ContainerRequest creq, ContainerResponse cresp) {

    //When released to proper server needs to be set to a specific value instead of *
    cresp.getHttpHeaders().putSingle(
        "Access-Control-Allow-Origin", "*");
    cresp.getHttpHeaders().putSingle(
        "Access-Control-Allow-Credentials", "true");
    cresp.getHttpHeaders().putSingle(
        "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
    cresp.getHttpHeaders().putSingle(
        "Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

    return cresp;
  }
}