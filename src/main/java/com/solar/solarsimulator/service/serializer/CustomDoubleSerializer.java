package com.solar.solarsimulator.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CustomDoubleSerializer extends JsonSerializer<Double> {
  private static final int DECIMAL_PRECISION = 4;

  @Override
  public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value != null) {
      BigDecimal bd = BigDecimal.valueOf(value);
      bd = bd.setScale(DECIMAL_PRECISION, RoundingMode.HALF_EVEN);
      gen.writeNumber(bd);
    }
  }
}
