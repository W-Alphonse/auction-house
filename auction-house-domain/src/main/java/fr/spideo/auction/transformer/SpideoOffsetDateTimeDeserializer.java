package fr.spideo.auction.transformer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.spideo.auction.Cnst;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class SpideoOffsetDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {
  // 2020-10-20T20:32:06+0800 - yyyy-MM-dd'T'HH:mm:ssZ	“2018-07-14T14:31:30+0530”
  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Cnst.DT_FMT_yyyy_MM_dd_T_HH_mm_ss_Z);

  public SpideoOffsetDateTimeDeserializer() {
    this(null);
  }

  public SpideoOffsetDateTimeDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public OffsetDateTime deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException, JsonProcessingException {
    String date = jsonparser.getText();
    return OffsetDateTime.parse(date.subSequence(0, date.length()), dateTimeFormatter /*DateTimeFormatter.ISO_OFFSET_DATE_TIME*/);
/*
    String date = jsonparser.getText();
    try {
      // 2020-10-20T20:32:06.928930411+08:00
      return OffsetDateTime.parse(date.subSequence(0, date.length()), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
//      ZonedDateTime asZonedDt = ZonedDateTime.ofInstant(instUtc, ZoneId.of("Asia/Singapore"));
//      System.out.println("Time in asZonedDt: " + asZonedDt.toString());
//      System.out.println("Time in asZonedDt thru Mapper: " + mapper.writeValueAsString(asZonedDt));
//      //
//      OffsetDateTime asOffsetDt = OffsetDateTime.ofInstant(instUtc, ZoneOffset.of("+08:00"));
//      System.out.println("Time in asOffsetDt: " + asOffsetDt.toString());
//      System.out.println("Time in asOffsetDt thru Mapper: " + mapper.writeValueAsString(asOffsetDt));
//
//      return formatter.parse(date);
    } catch (ParseException ex) {
      throw new SpideoException(String.format("Exception while parsing '%s'", date),ex);
    }
 */

  }

}
