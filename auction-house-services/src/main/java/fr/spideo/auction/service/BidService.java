package fr.spideo.auction.service;

import fr.spideo.auction.exception.SpideoException;
import fr.spideo.auction.model.Auction;
import fr.spideo.auction.model.Bid;
import fr.spideo.auction.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BidService {
  private final static Logger LOG = LoggerFactory.getLogger(BidService.class);
  //
  private SessionManager session;

  public BidService(SessionManager session) {
    this.session = session;
  }

  public Bid addBid(Bid bid) {
    Auction auction =  bid.getAuction() == null ? null : session.getAuction(bid.getAuction().getName());
    if( auction != null) {
      bid.setAuction(auction);
      bid.checkPriceValidity();
      checkBidIsStillRunning(bid);
      session.addBid(bid);
      return bid;
    } else  {
      session.checkAddBid(bid);
    }
    return null;
  }

  private void checkBidIsStillRunning(Bid bid) {
    if( ! bid.getAuction().isRunning() ) {
        String msgErr = String.format("AUCTION_NOT_BIDDABLE - Related Auction '%s' status[%s] is not biddable",
                        bid.getAuction().toString(), bid.getAuction().getStatus().name() );
        LOG.error(msgErr);
        throw new SpideoException(msgErr);
    }
  }

  public List<Bid> getBidsOfUser(String userName)  {
    return session.getBidsStore().entrySet()
    .stream().flatMap(auctionMapEntry -> auctionMapEntry.getValue().values().stream()).collect(Collectors.toList())
    .stream().filter(bid -> bid.getUserName().equals(userName)).collect(Collectors.toList());
  }

  public List<Bid> getBidsWinners()  {
    return session.getBidsStore().entrySet()
          .stream().filter(auctionMapEntry -> auctionMapEntry.getKey().isTerminated()).collect(Collectors.toList())
          .stream().map(auctionMapEntry -> auctionMapEntry.getValue().values().stream().max(Comparator.comparing(bid -> bid.getPrice())))
          //.forEach(b -> System.out.println());
          .filter(optional -> optional.isPresent())
          .map(optinal-> optinal.get())
          .collect(Collectors.toList());
  }
}
