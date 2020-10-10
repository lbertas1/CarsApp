package com.app.service.help;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
public class Stats<T> {
    private T min;
    private T max;
    private T avg;
}
