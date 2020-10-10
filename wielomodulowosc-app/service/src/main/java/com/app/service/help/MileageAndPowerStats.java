package com.app.service.help;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MileageAndPowerStats {
    private final Stats<BigDecimal> priceStats;
    private final Stats<Double> mileageStats;
}
