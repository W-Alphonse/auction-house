package fr.spideo.auction.endpoint;

import fr.spideo.auction.Cnst;
import fr.spideo.auction.model.Auction;
import fr.spideo.auction.model.AuctionStatus;
import fr.spideo.auction.model.SrvResponse;
import fr.spideo.auction.service.AuctionService;
import fr.spideo.auction.session.SessionManager;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Auction rest service endpoint.
 * HTTP methods are used according to the standard conventions:
 * - POST to create
 * - GET to read
 * - DELETE to remove
 */

@Path("/auction")
public class AuctionEndpoint {
  private final static Logger LOG = LoggerFactory.getLogger(AuctionEndpoint.class);
  //
  private AuctionService auctionService ;

  public AuctionEndpoint(SessionManager sessionManager) {
    this.auctionService = new AuctionService(sessionManager);
  }

  @POST
  @Path("/create")
  @Consumes({ MediaType.APPLICATION_JSON})
  @Produces({ MediaType.APPLICATION_JSON})
  public SrvResponse create(Auction auction) {
    long startTimeInMs = System.currentTimeMillis();
    SrvResponse resp = new SrvResponse();
    try {
      auctionService.addAuction(auction);
      resp.setStatus(SrvResponse.StatusCode.OK.name());
    } catch (Exception ex) {
      resp.fillErrorInformation(String.format("ERROR while creating Auction [AuctionName: %s, HouseName %s] - Rest call /auction/create",
            StringUtils.defaultString(auction.getName(), Cnst.NOT_MENTIONED) , auction.getHouse() != null ? auction.getHouse().getName() : ""), ex);
    } finally {
      resp.setDurationInMsAndLog (startTimeInMs, "/auction/create");
    }
    return resp;
  }

  @GET
  @Path("/list/{houseName}")
  @Consumes({ MediaType.APPLICATION_JSON})
  @Produces({ MediaType.APPLICATION_JSON})
  public SrvResponse list(@PathParam("houseName") String houseName) {
    long startTimeInMs = System.currentTimeMillis();
    SrvResponse resp = new SrvResponse();
    try {
      Collection<Auction> auctions = auctionService.getHouseAuctions(houseName);
      resp.addPayloadElmt("auctions", ObjectUtils.defaultIfNull(new ArrayList(auctions), new ArrayList()) );
      resp.setStatus(SrvResponse.StatusCode.OK.name());
    } catch (Exception ex) {
      resp.fillErrorInformation(String.format("ERROR while retrieving all house auctions of '%s'  - Rest call /auction/list", houseName), ex);
    } finally {
      resp.setDurationInMsAndLog (startTimeInMs, "/auction/list");
    }
    return resp;
  }

  @DELETE
  @Path("/delete/{auctionName}")
  @Consumes({ MediaType.APPLICATION_JSON})
  @Produces({ MediaType.APPLICATION_JSON})
  public SrvResponse delete(@PathParam("auctionName") String auctionName) {
    long startTimeInMs = System.currentTimeMillis();
    SrvResponse resp = new SrvResponse();
    try {
      boolean removed = auctionService.removeAuction(auctionName);
      resp.setStatusExtendedInfo(String.format(removed ? "Auction '%s' removed" : "Auction '%s' not removed because it does not exist",
      StringUtils.defaultString(auctionName, Cnst.NOT_MENTIONED)));
      resp.setStatus(SrvResponse.StatusCode.OK.name());
    } catch (Exception ex) {
      resp.fillErrorInformation(String.format("ERROR while removing house auction '%s' - Rest call /auction/delete", auctionName), ex);
    } finally {
      resp.setDurationInMsAndLog (startTimeInMs, "/auction/delete");
    }
    return resp;
  }


  @GET
  @Path("/list-status/{auctionStatus}")
  @Consumes({ MediaType.APPLICATION_JSON})
  @Produces({ MediaType.APPLICATION_JSON})
  public SrvResponse listStatusOf(@PathParam("auctionStatus") String auctionStatus) {
    long startTimeInMs = System.currentTimeMillis();
    SrvResponse resp = new SrvResponse();
    try {
      List<Auction> auctions = auctionService.getAuctionsOfStatus( AuctionStatus.valueOf(auctionStatus));
      resp.addPayloadElmt("auctions", new ArrayList(auctions) );
      resp.setStatus(SrvResponse.StatusCode.OK.name());
    } catch (Exception ex) {
      resp.fillErrorInformation(String.format("ERROR while retrieving all %s auctions - Rest call /auction/list-status", AuctionStatus.valueOf(auctionStatus)), ex);
    } finally {
      resp.setDurationInMsAndLog (startTimeInMs, "/auction/list-status");
    }
    return resp;
  }

}
