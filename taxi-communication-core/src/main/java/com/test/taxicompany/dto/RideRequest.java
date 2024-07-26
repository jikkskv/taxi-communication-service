package com.test.taxicompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.taxicompany.location.CoOrdinate;

import java.time.LocalDateTime;

public record RideRequest(CoOrdinate source, CoOrdinate destination,
                          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime pickupTime) {
}
