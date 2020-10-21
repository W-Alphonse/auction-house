package fr.spideo.auction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.spideo.auction.transformer.SpideoOffsetDateTimeDeserializer;
import fr.spideo.auction.transformer.SpideoOffsetDateTimeSerializer;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@JsonSerialize
@JsonInclude(value= JsonInclude.Include.NON_EMPTY, content= JsonInclude.Include.NON_NULL)
public class Auction {
  private final static Logger LOG = LoggerFactory.getLogger(Auction.class);
  public final static String ENTITY_NAME = "Auction";

  @NotEmpty(message = "'Auction Name' is mandatory")
  private String name;
  private String description;
  private AuctionStatus status;
  //
  @JsonDeserialize(using = SpideoOffsetDateTimeDeserializer.class)
  @JsonSerialize(using = SpideoOffsetDateTimeSerializer.class)
  private OffsetDateTime startTime;
  //
  @FutureOrPresent(message = "'End Auction Date' should be > now")
  @NotNull(message = "End Auction Date' should be > now")
  @JsonDeserialize(using = SpideoOffsetDateTimeDeserializer.class)
  @JsonSerialize(using = SpideoOffsetDateTimeSerializer.class)
  private OffsetDateTime endTime;
  //
  @Positive(message = "'Start Auction Price' should be > 0")
  private Double startPrice;
  private Double currentPrice;
  //
  private House house;
  private List<Bid> bids;

  public Auction() {
  }

  public Auction(House house, String name) {
    this.house = house;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public AuctionStatus getStatus() {
    return getEndTime() != null && OffsetDateTime.now().isAfter(getEndTime()) ? AuctionStatus.TERMINATED :
           ObjectUtils.isNotEmpty(status)  ? status :
           getStartTime() != null && OffsetDateTime.now().isAfter(getStartTime()) ? AuctionStatus.RUNNING :
           AuctionStatus.NOT_STARTED;
  }

  public void setStatus(AuctionStatus status) {
    this.status = status;
  }

  @JsonIgnore
  public boolean isRunning() {
    boolean isRunning = AuctionStatus.RUNNING.equals(getStatus()); // Hook for testing purpose ;-)
    return isRunning;
  }

  @JsonIgnore
  public boolean isTerminated() {
    boolean isTerminated = AuctionStatus.TERMINATED.equals(getStatus()); // Hook for testing purpose ;-)
    return isTerminated;
  }

  public OffsetDateTime getStartTime() {
    return startTime;
  }

  public OffsetDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(OffsetDateTime endTime) {
    this.endTime = endTime;
  }

  public Double getStartPrice() {
    return startPrice;
  }

  public void setStartPrice(Double startPrice) {
    this.startPrice = startPrice;
  }

  public Double getCurrentPrice() {
    return currentPrice != null ? currentPrice : getStartPrice();
  }

  public void setCurrentPrice(Double currentPrice) {
    this.currentPrice = currentPrice;
  }

  public House getHouse() {
    return house;
  }

  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Auction auction = (Auction) o;
    return name.equals(auction.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  public String toString() {
    return String.format("[AuctionName: %s, HouseName:%s]", StringUtils.defaultString(name),
                          house == null ? "": StringUtils.defaultString(house.getName()));
  }

}
