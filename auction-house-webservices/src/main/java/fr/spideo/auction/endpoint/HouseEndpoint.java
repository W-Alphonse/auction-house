package fr.spideo.auction.endpoint;

import fr.spideo.auction.Cnst;
import fr.spideo.auction.model.House;
import fr.spideo.auction.model.SrvResponse;
import fr.spideo.auction.service.HouseService;
import fr.spideo.auction.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;

/**
 * House Auction rest service endpoint.
 * HTTP methods are used according to the standard conventions:
 * - POST to create
 * - GET to read
 * - DELETE to remove
 */

@Path("/house")
public class HouseEndpoint {
  private final static Logger LOG = LoggerFactory.getLogger(HouseEndpoint.class);
  //
  private HouseService houseService;

  public HouseEndpoint(SessionManager sessionManager) {
    this.houseService = new HouseService(sessionManager);
  }


  @POST
  @Path("/create")
  @Consumes({ MediaType.APPLICATION_JSON})
  @Produces({ MediaType.APPLICATION_JSON})
  public SrvResponse create(House house) {
    long startTimeInMs = System.currentTimeMillis();
    SrvResponse resp = new SrvResponse();
    try {
      houseService.addHouse(house);
      resp.setStatus(SrvResponse.StatusCode.OK.name());
    } catch (Exception ex) {
      resp.fillErrorInformation(String.format("ERROR while creating House '%s' - Rest call /house/create",
                                StringUtils.defaultString(house.getName(), Cnst.NOT_MENTIONED)), ex);
    } finally {
      resp.setDurationInMsAndLog (startTimeInMs, "/house/create");
    }
    return resp;
  }

  @GET
  @Path("/listAll")
  @Consumes({ MediaType.APPLICATION_JSON})
  @Produces({ MediaType.APPLICATION_JSON})
  public SrvResponse listAll() {
    long startTimeInMs = System.currentTimeMillis();
    SrvResponse resp = new SrvResponse();
    try {
      Collection<House> houses = houseService.getAllHouses();
      resp.addPayloadElmt("houses", new ArrayList(houses) );
      resp.setStatus(SrvResponse.StatusCode.OK.name());
    } catch (Exception ex) {
      resp.fillErrorInformation("ERROR while retrieving all houses auction - Rest call /house/listAll", ex);
    } finally {
      resp.setDurationInMsAndLog (startTimeInMs, "/house/listAll");
    }
    return resp;
  }

  @DELETE
  @Path("/delete/{name}")
  @Consumes({ MediaType.APPLICATION_JSON})
  @Produces({ MediaType.APPLICATION_JSON})
  public SrvResponse delete(@PathParam("name") String name) {
    long startTimeInMs = System.currentTimeMillis();
    SrvResponse resp = new SrvResponse();
    try {
      boolean removed = houseService.removeHouse(name);
      resp.setStatusExtendedInfo(String.format(removed ? "House '%s' removed" : "House '%s' not removed because it does not exist",
                                              StringUtils.defaultString(name, Cnst.NOT_MENTIONED)));
      resp.setStatus(SrvResponse.StatusCode.OK.name());
    } catch (Exception ex) {
      resp.fillErrorInformation(String.format("ERROR while removing house auction '%s' - Rest call /house/delete", name), ex);
    } finally {
      resp.setDurationInMsAndLog (startTimeInMs, "/house/delete");
    }
    return resp;
  }


}
