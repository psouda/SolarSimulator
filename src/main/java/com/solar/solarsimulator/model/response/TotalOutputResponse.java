package com.solar.solarsimulator.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.solar.solarsimulator.service.serializer.CustomDoubleSerializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalOutputResponse {

  @JsonProperty(value = "total-output-in-kwh")
  @JsonSerialize(using = CustomDoubleSerializer.class)
  double totalOutputInKwh;

}
