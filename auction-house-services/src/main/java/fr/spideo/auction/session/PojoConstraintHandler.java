package fr.spideo.auction.session;

import fr.spideo.auction.exception.SpideoException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class PojoConstraintHandler {
  private final static Logger LOG = LoggerFactory.getLogger(PojoConstraintHandler.class);
  private Validator validator;

  public PojoConstraintHandler() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  public void validate(Object pojo) {
    Set<ConstraintViolation<Object>> violations = validator.validate(pojo);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder(256);
      for (ConstraintViolation<Object> violation : violations) {
        String msgErr = String.format("INCONSISTENCY_ERR : %s / '%s'", violation.getMessage(), StringUtils.substring(pojo.toString(), 0, 512));
        LOG.error(msgErr);
        sb.append(msgErr).append('\n');
      }
      throw new SpideoException(sb.toString());
    }
  }

}
