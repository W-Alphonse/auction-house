package fr.spideo.auction.session;

import fr.spideo.auction.exception.SpideoException;
import fr.spideo.auction.model.Auction;
import fr.spideo.auction.model.AuctionStatus;
import fr.spideo.auction.model.Bid;
import fr.spideo.auction.model.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SessionManager manages all the the access to the application's session POJO entities.
 * It access to the store by entity type: PUT/GET/REMOVE
 * It ensures the primary key unicity
 * It avoids referencing a missing a like-foreign-key
 * It avoids removing a referenced foreign-key
 * It applies constraints validation defined by hibernate-validator
 * Its ensures consistency usually managed by a RDBMS
 */
public class SessionManager {
  private final static Logger LOG = LoggerFactory.getLogger(SessionManager.class);
  //
  private Map<String/*houseName*/, House> houses = new ConcurrentHashMap<String/*house-name*/, House>();
  private AuctionStore<House, Map<String/*auctionName*/,Auction>> auctionsStore = new AuctionStore<House, Map<String,Auction>>();
  private Map<Auction, Map<String/*userName*/,Bid>> bids = new ConcurrentHashMap<Auction, Map<String,Bid>>();
  //
  private PojoConstraintHandler constraintHandler;

  public SessionManager() {
    constraintHandler = new PojoConstraintHandler();
  }

  public Map<String, House> getHousesStore() {
    return houses;
  }
  public AuctionStore<House, Map<String, Auction>> getAuctionsStore() {
    return auctionsStore;
  }

  public Map<Auction, Map<String, Bid>> getBidsStore() {
    return bids;
  }

  /////////////
  //  HOUSE  //
  /////////////
  /**
   * Create an auction house with a given name
   * @param house
   * @throws SpideoException if trying to add a duplicated House
   */
  public void addHouse(House house) throws SpideoException {
    constraintHandler.validate(house);
    checkPrimaryKeyUnicity( houses/*.containsKey(house.getName())*/ , house.getName(), House.ENTITY_NAME);
    houses.put(house.getName(), house);
  }

  /**
   * Returns all Houses registered to the session
   * @return a collection of all auction houses created
   */
  public Collection<House> getAllHouses() {
    return houses.values();
  }

  public House getHouse(String houseName) {
    return houses.get(houseName);
  }

  /**
   * Deletes a specific House Auction
   * @param name
   * @return True if the House was in the session and therefore deleted, otherwise its returns False
   */
  public boolean removeHouse(String name) {
    if( auctionsStore.containsKey(new House(name)) ) {
      String msgErr = String.format("FKEY_CONSTRAINT_ERR - Forbidden to remove a HOUSE referenced by one or multiple Auction(s) '%s'",
                                    auctionsStore.get(new House(name)).values().iterator().next() );
      LOG.error(msgErr);
      throw new SpideoException(msgErr);
    }
    return houses.remove(name) != null;
  }


  ////////////////
  //   Auction  //
  ////////////////
  /**
   * Adds an Auction
   * @param auction
   * @throws SpideoException if referencing a missing House
   */
  public void addAuction(Auction auction) throws SpideoException {
    constraintHandler.validate(auction);
    checkPrimaryKeyUnicity(/*auctionsStore.containsAuctionName(auction.getName())*/ auctionsStore.getSecondaryIndex(), auction.getName(), Auction.ENTITY_NAME);
    checkForeignKeyExistence(houses, auction.getHouse() == null ? null: auction.getHouse().getName(), House.ENTITY_NAME, Auction.ENTITY_NAME);
    auctionsStore.putAuction(auction);
  }


  /**
   * @param houseName
   * @return all auctions for a given auction house
   */
  public Collection<Auction> getHouseAuctions(String houseName) {
    if (houses.containsKey(houseName)) {
      Map<String/*auction-name*/,Auction> auctions = auctionsStore.getAllAuctions(houses.get(houseName));
      return auctions == null ? null : auctions.values();
    } else {
      return null;
    }
  }

  /**
   * Delete a specific auction
   * @param auctionName
   * @return
   */
  public boolean removeAuction(String auctionName) {
    if( bids.containsKey(new Auction(null,auctionName)) ) {
      String msgErr = String.format("FKEY_CONSTRAINT_ERR - Forbidden to remove an AUCTION referenced by one or multiple Bid(s) '%s'",
                                      bids.get(new Auction(null,auctionName)).values().iterator().next());
      LOG.error(msgErr);
      throw new SpideoException(msgErr);
    }
    return auctionsStore.removeAuction(auctionName);
  }

  public List<Auction> getAuctionsOfStatus(AuctionStatus auctionStatus) {
    return auctionsStore.getAuctionsOfStatus(auctionStatus);
  }

  public Auction getAuction(String auctionname) {
    return auctionsStore.getAuction(auctionname);
  }

  /////////////
  //   Bid   //
  /////////////
  /**
   * Add a new Bid or Update the existing one (same user, same auction)
   * @param bid
   * @throws SpideoException if trying to add a duplicated House
   */
  public void addBid(Bid bid) throws SpideoException {
    checkAddBid(bid);
    Map<String/*user-name*/, Bid> userBids = bids.getOrDefault(bid.getAuction() /*UserName()*/, new HashMap<String/*user-name*/, Bid>());
    userBids.put(bid.getUserName(), bid);
    bids.put(bid.getAuction(), userBids);
  }

  public void checkAddBid(Bid bid) {
    constraintHandler.validate(bid);
    checkForeignKeyExistence(auctionsStore.getSecondaryIndex(), bid.getAuction().getName(), Auction.ENTITY_NAME, Bid.ENTITY_NAME);
  }

  ////////////////////////////////////////
  // Session Data Constraint Validators //
  ////////////////////////////////////////
  private void checkPrimaryKeyUnicity(/*boolean storeContainsKey,*/ Map entityStore, String entityIdToCheck, String entityName) {
    if ( /*storeContainsKey*/ entityStore.containsKey(entityIdToCheck)) {
      String msgErr = String.format("DUPLICATION_ERR - Duplication error while adding %s '%s'", entityName, entityIdToCheck);
      LOG.error(msgErr);
      throw new SpideoException(msgErr);
    }
  }

  private void checkForeignKeyExistence(Map fkEntityStore, Object entityFkIdToCheck, String fkEntityName, String referencingEntityName) {
    if( fkEntityStore == null || ! /*houses*/fkEntityStore.containsKey(entityFkIdToCheck) ) {
      String msgErr = String.format("FKEY_ERR - %s references a missing %s '%s'", referencingEntityName, fkEntityName, entityFkIdToCheck);
      LOG.error(msgErr);
      throw new SpideoException(msgErr);
    }
  }

}
