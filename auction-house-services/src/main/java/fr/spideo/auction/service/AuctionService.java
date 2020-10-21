package fr.spideo.auction.service;

import fr.spideo.auction.model.Auction;
import fr.spideo.auction.model.AuctionStatus;
import fr.spideo.auction.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;

public class AuctionService {
  private final static Logger LOG = LoggerFactory.getLogger(AuctionService.class);

  private SessionManager session;

  public AuctionService(SessionManager session) {
    this.session = session;
  }

  public Auction addAuction(Auction auction ) {
    if (auction.getStartTime() == null) {
      auction.setStartTime(OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.of("+02:00")));
    }
    session.addAuction(auction);
    return auction;
  }

  public Collection<Auction> getHouseAuctions(String houseName) {
    return session.getHouseAuctions(houseName);
  }

  public boolean removeAuction(String auctionName) {
    return session.removeAuction(auctionName) ;
  }

  public List<Auction> getAuctionsOfStatus(AuctionStatus auctionStatus) {
    return session.getAuctionsOfStatus(auctionStatus);
  }

//  public List<Auction> generateAuctions(int count, String prefix, int zoneOffset) {
//    String uuid = UUID.randomUUID().toString();
//    OffsetDateTime startTime = OffsetDateTime.now(ZoneOffset.of(String.format("%s%02d:00", zoneOffset > 0 ? "+" : "-", zoneOffset)));
//    //
//    return IntStream
//            .range(0, count)
//            // .mapToObj(i -> createAuction(String.format("%s_%s", uuid.substring(0, 8), Integer.toString(i)),
//            .mapToObj(i -> createAuction(String.format("Auction_%d", i),
//                    String.format("%s_%s_%s", prefix, uuid.substring(0, 24), Integer.toString(i)),
//                    new Double(500000.0), startTime, startTime.plusDays(7))
//            )
//            .collect(Collectors.toList());
//  }
}
