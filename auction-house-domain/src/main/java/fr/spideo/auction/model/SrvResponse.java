package fr.spideo.auction.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.spideo.auction.exception.SpideoException;
import fr.spideo.auction.transformer.SpideoOffsetDateTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@JsonSerialize
@JsonInclude(value= JsonInclude.Include.NON_EMPTY, content= JsonInclude.Include.NON_NULL)
public class SrvResponse {
  private final static Logger LOG = LoggerFactory.getLogger(SrvResponse.class);
  //
  @JsonSerialize(using = SpideoOffsetDateTimeSerializer.class)  private OffsetDateTime requestTime;
  @JsonIgnore private long requestTimeInMs;
  private long durationInMs;
  private String status;
  private String statusExtendedInfo;
  //
  private String descriptiveError;
  private List<String> technicalErrors = new ArrayList<String>();
  //
  private Map<String, List<Object>> payload = new HashMap<String, List<Object>>();
  public enum StatusCode {
          OK,   // => The expected service was accomplished
          KO}   // => The expected service was not accomplished because of a technical error

  public SrvResponse() {
    requestTime = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.of("+02:00"));
    requestTimeInMs = requestTime.toEpochSecond() * 1000;
  }

  public long getDurationInMs() {
    return durationInMs;
  }

  public void setDurationInMs(long durationInMs) {
    this.durationInMs = durationInMs;
  }

  public void setDurationInMsAndLog(long startTimeInMs, String restPath) {
    durationInMs = System.currentTimeMillis() - startTimeInMs;
    setDurationInMs(durationInMs);
    LOG.info(String.format("%s execution time: %d ms", restPath, durationInMs));
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatusExtendedInfo() {
    return statusExtendedInfo;
  }

  public void setStatusExtendedInfo(String statusExtendedInfo) {
    this.statusExtendedInfo = statusExtendedInfo;
  }

  public String getDescriptiveError() {
    return descriptiveError;
  }

  public void setDescriptiveError(String descriptiveError) {
    this.descriptiveError = descriptiveError;
  }

  public void addTechnicalError(String err) {
    technicalErrors.add(err);
  }

  public List<String> getTechnicalErrors() {
    return technicalErrors;
  }

  public void setTechnicalErrors(List<String> technicalErrors) {
    this.technicalErrors = technicalErrors;
  }

  public OffsetDateTime getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(OffsetDateTime requestTime) {
    this.requestTime = requestTime;
  }

  public long getRequestTimeInMs() {
    return requestTimeInMs;
  }

  public void setRequestTimeInMs(long requestTimeInMs) {
    this.requestTimeInMs = requestTimeInMs;
  }

  public Map<String, List<Object>> getPayload() {
    return payload;
  }

  public void setPayload(Map<String, List<Object>> payload) {
    this.payload = payload;
  }

  public void addPayloadElmt(String key, Object value) {
    List<Object> values = payload.getOrDefault(key, new ArrayList<Object>());
    if (value instanceof Collection) {
      values.addAll((Collection)value);
    } else {
      values.add(value);
    }
    payload.put(key, values);
  }

  public void fillErrorInformation(String descriptiveError, Exception ex) {
    setStatus(SrvResponse.StatusCode.KO.name());
    setDescriptiveError(ex instanceof SpideoException ? String.format("%s >>> %s", ex.getMessage(), descriptiveError) : descriptiveError) ;
    if (ex != null) {
      addTechnicalError(ExceptionUtils.getStackTrace(ex));
    }
    LOG.error(String.format("%s\n%s", getDescriptiveError(), StringUtils.join(getTechnicalErrors(), '\n') ));
  }

}
