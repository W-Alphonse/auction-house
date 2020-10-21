package fr.spideo.auction.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("/ping")
public class PingEndpoint {
  private final static Logger LOG = LoggerFactory.getLogger(PingEndpoint.class);

  @GET
  @Path("/echo{message : .*}")
  // @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
  // @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
  public String echo(@PathParam("message") String message) {
    return message.startsWith("/") ? message.substring(1): message;
  }


}
