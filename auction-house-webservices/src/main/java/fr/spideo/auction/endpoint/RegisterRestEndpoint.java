package fr.spideo.auction.endpoint;

import fr.spideo.auction.session.SessionManager;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/rest")
public class RegisterRestEndpoint extends Application {
  private Set<Object> singletons = new HashSet<Object>();

  /**
   * Register all the Rest Endpoint
   */
  public RegisterRestEndpoint() {
    SessionManager sessionManager = new SessionManager();
    //
    singletons.add(new PingEndpoint());
    singletons.add(new HouseEndpoint(sessionManager));
    singletons.add(new AuctionEndpoint(sessionManager));
    singletons.add(new BidEndpoint(sessionManager));
  }

  @Override
  public Set<Object> getSingletons() {
    return singletons;
  }

}