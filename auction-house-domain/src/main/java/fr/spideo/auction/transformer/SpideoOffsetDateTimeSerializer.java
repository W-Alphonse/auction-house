package fr.spideo.auction.transformer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.spideo.auction.Cnst;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class SpideoOffsetDateTimeSerializer extends StdSerializer<OffsetDateTime> {
  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Cnst.DT_FMT_yyyy_MM_dd_T_HH_mm_ss_Z);

  public SpideoOffsetDateTimeSerializer() {
    this(null);
  }

  public SpideoOffsetDateTimeSerializer(Class<OffsetDateTime> vc) {
    super(vc);
  }

  public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    // Date pattern to format as String: 2018-07-14T14:31:30+0530
//    String fmtDate = String.format("%04d-%02d-%02dT%02d:%02d:%02d+%02d%02d",
//                                  value.getYear(), value.getMonthValue(), value.getDayOfMonth(),
//                                  value.getHour(), value.getMinute(), value.getSecond(),
//                                  value.getOffset().toString());
//    dateTimeFormatter.format(value);
    gen.writeObject(dateTimeFormatter.format(value));
  }

}
