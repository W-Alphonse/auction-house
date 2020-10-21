package fr.spideo.auction.endpoint;

import fr.spideo.auction.Cnst;
import fr.spideo.auction.model.Auction;
import fr.spideo.auction.model.AuctionStatus;
import fr.spideo.auction.model.Bid;
import fr.spideo.auction.model.SrvResponse;
import fr.spideo.auction.service.BidService;
import fr.spideo.auction.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Bid rest service endpoint.
 * HTTP methods are used according to the standard conventions:
 * - POST to create
 * - GET to read
 * - DELETE to remove
 */

@Path("/bid")
public class BidEndpoint {
  private final static Logger LOG = LoggerFactory.getLogger(BidEndpoint.class);
  //
  private BidService bidService;

  public BidEndpoint(SessionManager sessionManager) {
    this.bidService = new BidService(sessionManager);
  }

  @POST
  @Path("/create")
  @Consumes({ MediaType.APPLICATION_JSON})
  @Produces({ MediaType.APPLICATION_JSON})
  public SrvResponse create(Bid bid) {
    long startTimeInMs = System.currentTimeMillis();
    SrvResponse resp = new SrvResponse();
    try {
      bidService.addBid(bid);
      resp.setStatus(SrvResponse.StatusCode.OK.name());
    } catch (Exception ex) {
      resp.fillErrorInformation(String.format("ERROR while creating Bid [AuctionName: %s, UserName: %s] - Rest call /bid/create",
                    bid.getAuction() == null ? Cnst.NOT_MENTIONED : StringUtils.defaultString(bid.getAuction().getName(), Cnst.NOT_MENTIONED),
                    StringUtils.defaultString(bid.getUserName(), Cnst.NOT_MENTIONED)), ex);
    } finally {
      resp.setDurationInMsAndLog (startTimeInMs, "/bid/create");
    }
    return resp;
  }

  @GET
  @Path("/list/{userName}")
  @Consumes({ MediaType.APPLICATION_JSON})
  @Produces({ MediaType.APPLICATION_JSON})
  public SrvResponse list(@PathParam("userName") String userName) {
    long startTimeInMs = System.currentTimeMillis();
    SrvResponse resp = new SrvResponse();
    try {
      List<Bid> bids = bidService.getBidsOfUser( userName);
      resp.addPayloadElmt("bids", new ArrayList(bids) );
      resp.setStatus(SrvResponse.StatusCode.OK.name());
    } catch (Exception ex) {
      resp.fillErrorInformation(String.format("ERROR while listing Bid [UserName: %s] - Rest call /bid/list",userName), ex);
    } finally {
      resp.setDurationInMsAndLog (startTimeInMs, "/bid/list");
    }
    return resp;
  }

  @GET
  @Path("/list-winner")
  @Consumes({ MediaType.APPLICATION_JSON})
  @Produces({ MediaType.APPLICATION_JSON})
  public SrvResponse listWinners() {
    long startTimeInMs = System.currentTimeMillis();
    SrvResponse resp = new SrvResponse();
    try {
      List<Bid> bids = bidService.getBidsWinners();
      resp.addPayloadElmt("bids", new ArrayList(bids) );
      resp.setStatus(SrvResponse.StatusCode.OK.name());
    } catch (Exception ex) {
      resp.fillErrorInformation(String.format("ERROR while listing Bids Winners - Rest call /bid/listWinners"), ex);
    } finally {
      resp.setDurationInMsAndLog (startTimeInMs, "/bid/listWinners");
    }
    return resp;
  }

}
