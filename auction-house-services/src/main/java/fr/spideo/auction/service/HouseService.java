package fr.spideo.auction.service;

import fr.spideo.auction.model.House;
import fr.spideo.auction.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HouseService {
  private final static Logger LOG = LoggerFactory.getLogger(HouseService.class);
  //
  private SessionManager session;

  public HouseService(SessionManager session) {
    this.session = session;
  }

//  public List<House> generateHouses(int count, String prefix, int zoneOffset) {
//    String uuid = UUID.randomUUID().toString();
//    //
//    return IntStream
//            .range(0, count)
//            .mapToObj(i -> addHouse(String.format("%s_%s", uuid.substring(0, 8), Integer.toString(i))))
//            .collect(Collectors.toList());
//  }

  public House addHouse(House house) {
    session.addHouse(house);
    return house;
  }

  public Collection<House> getAllHouses() {
    return session.getAllHouses();
  }

  public boolean removeHouse(String name) {
    return session.removeHouse(name) ;
  }


//  public static void main(String[] args) {
//    try {
//      Date nowUtc = new Date();
//      //    TimeZone parisTz = TimeZone.getTimeZone("Europe/Paris");
//      TimeZone parisTz = TimeZone.getTimeZone("Asia/Singapore");
//      Calendar nowparis = Calendar.getInstance(parisTz);
//
//      System.out.println("Time in Paris: " + nowparis.getTime());
//      System.out.println("Time in UTC: " + nowUtc);
//
//      //
//      ObjectMapper mapper = new ObjectMapper();
//      mapper.registerModule(new JavaTimeModule());
//      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//      Instant instUtc = Instant.now();
//      // System.out.println(String.format("instUtc:%s  -  asZonedDt:%d", instUtc.getEpochSecond(), asZonedDt.toEpochSecond()));
//      //
//      ZonedDateTime asZonedDt = ZonedDateTime.ofInstant(instUtc, ZoneId.of("Asia/Singapore"));
//      System.out.println("Time in asZonedDt: " + asZonedDt.toString());
//      System.out.println("Time in asZonedDt thru Mapper: " + mapper.writeValueAsString(asZonedDt));
//      //
//      OffsetDateTime asOffsetDt = OffsetDateTime.ofInstant(instUtc, ZoneOffset.of("+08:00"));
//      System.out.println("Time in asOffsetDt: " + asOffsetDt.toString());
//      System.out.println("Time in asOffsetDt thru Mapper: " + mapper.writeValueAsString(asOffsetDt));
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
//  }

}
