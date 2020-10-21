package fr.spideo.auction.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@JsonSerialize
@JsonInclude(value= JsonInclude.Include.NON_EMPTY, content= JsonInclude.Include.NON_NULL)
public class House {
  private final static Logger LOG = LoggerFactory.getLogger(House.class);
  public final static String ENTITY_NAME = "HOUSE";
  //
  @NotEmpty(message = "'House Name' is mandatory")
  // @XmlElementWrapper(name="name")
  private String name;

  public House(){
  }

  public House(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    House house = (House) o;
    return name.equals(house.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

//  public String toString() {
//    return ToStringBuilder.reflectionToString(this);
//  }
  public String toString() {
    return String.format("[name: %s]", StringUtils.defaultString(name));
  }
}
