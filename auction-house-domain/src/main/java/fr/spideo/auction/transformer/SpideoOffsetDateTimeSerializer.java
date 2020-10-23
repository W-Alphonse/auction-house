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
    gen.writeObject(dateTimeFormatter.format(value));
  }

}
