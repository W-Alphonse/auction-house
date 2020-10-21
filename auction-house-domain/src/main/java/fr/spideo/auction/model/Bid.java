package fr.spideo.auction.model;

import fr.spideo.auction.exception.SpideoException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

public class Bid {
  private final static Logger LOG = LoggerFactory.getLogger(Bid.class);
  public final static String ENTITY_NAME = "BID";

  @NotNull(message = "'Bid User' is mandatory")
  private String userName;
  @NotNull(message = "Bid Price' is mandatory")
  private Double price;
  @NotNull(message = "'Auction Bid' is mandatory")
  private Auction auction;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Auction getAuction() {
    return auction;
  }

  public void setAuction(Auction auction) {
    this.auction = auction;
  }

  public void checkPriceValidity() {
    if( getPrice() != null && auction.getCurrentPrice() != null) {
      if (getPrice().doubleValue() < auction.getCurrentPrice().doubleValue()) {
        String msgErr = String.format("BID_PRICE_NOT_VALID - Proposed Bid price '%.3f' for Auction %s is lower than the Auction current price '%.3f'",
        getPrice().doubleValue() , auction.toString(), auction.getCurrentPrice().doubleValue());
        LOG.error(msgErr);
        throw new SpideoException(msgErr);
      }
    }
  }

  public String toString() {
    return String.format("[UserName:%s, AuctionName: %s]", StringUtils.defaultString(userName),
                          auction == null ? "": StringUtils.defaultString(auction.getName()));
  }
}
