package fr.spideo.auction.session;

import fr.spideo.auction.model.Auction;
import fr.spideo.auction.model.AuctionStatus;
import fr.spideo.auction.model.House;
import org.apache.commons.lang3.StringUtils;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * AuctionStore extends a Map.
 * It provides several like-key-access types to retrieve the desired Auction.
 * Possible like-key-acces type :
 * - The primary key access type provides the set of Auctions by specifying House as input
 *   The current instance Parent Map ensures the primary key role.
 * - The secondary key access type provides the set of Auctions targeting the same House as the Auction-Name specified in input.
 *   It is the set of Auctions linked to the same house.
 *   The instance member 'auctionsTargetingSameHouseAs' ensures the secondary role.
 * Notice that both keys reference the same Auctions instances, there is no duplication.
 *
 * @param <K> : House
 * @param <V> : Map<auction-name,Auction>
 */
public class AuctionStore<K extends House /*House*/, V extends Map<String,Auction>/*Map<auction-name,Auction>*/> extends ConcurrentHashMap<K, V> {

  private Map<String/*auction-name*/, Map<String/*same-house-auctions-name*/,Auction>> auctionsTargetingSameHouseAs = new ConcurrentHashMap<String, Map<String,Auction>>();

  /**
   * Puts an Auction to the Store
   * @param auction
   */
  void putAuction(Auction auction) {
    Map<String, Auction> houseAuctions = getOrDefault(auction.getHouse(), (V)new HashMap<String, Auction>());
    houseAuctions.put(auction.getName(), auction);
    /* 1 - Updating the primary-key-access */
    put((K)auction.getHouse(), (V)houseAuctions);
    /* 2 - Updating the secondary-key-access */
    auctionsTargetingSameHouseAs.put(auction.getName(), houseAuctions);
  }

  /**
   * Gets all the Auctions related to a House
   * Accessing the store through its first-key-access
   * @param house
   * @return
   */
  Map<String/*auction-name*/,Auction> getAllAuctions(House house) {
    return get(house);
  }

  Map<K/*House*/, V /*Map<String,Auction>*/> getPrimaryIndex() {
    return this;
  }

  Map<String/*auction-name*/, Map<String/*auctions-name*/,Auction>> getSecondaryIndex() {
    return auctionsTargetingSameHouseAs;
  }

  Auction getAuction(String auctionName) {
    return StringUtils.isBlank(auctionName) || getSecondaryIndex().get(auctionName) == null ?
           null : getSecondaryIndex().get(auctionName).get(auctionName);
  }

  /**
   * Remove the Auction and update the primary and secondary keys
   * @param auctionName
   * @return True if the Auction is removed; False otherwise (may be the Auction is not stored)
   */
  boolean removeAuction(String auctionName) {
    Map<String/*auctionName*/, Auction> auctions = auctionsTargetingSameHouseAs.get(auctionName);
    if( auctions == null || auctions.isEmpty()) {
      return false;
    } else {
      /* 1 - Remove the auction from the StoreContent */
      Auction removedAuction = auctions.remove(auctionName);
      /* 2 - Remove the auction from the Secondary Index */
      getSecondaryIndex().remove(auctionName);
      /* 3 - Check whether there is still other auction(s) targeting the same House.
             If not, then remove the empty Map */
      if( removedAuction != null && get(removedAuction.getHouse()) != null && get(removedAuction.getHouse()).isEmpty()) {
        remove(removedAuction.getHouse());
      }
      return removedAuction != null;
    }
  }
  public List<Auction> getAuctionsOfStatus(AuctionStatus auctionStatus) {
    return entrySet() /* EntrySet of : <House , Map<String,Auction>> */
          .stream().flatMap(entry -> entry.getValue().values().stream()).collect(Collectors.toList())
          .stream().filter(auction -> auction.getStatus().equals(auctionStatus)).collect(Collectors.toList());
  }

}
