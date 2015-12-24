package com.medicojur.web.server;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import java.net.URI;

public class Utils {

  public static WebResource createWebResource(URI uri) {
    DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
    defaultClientConfig.getClasses().add(JacksonJsonProvider.class);
    Client client = Client.create(defaultClientConfig);
    return client.resource(uri);
  }
}
