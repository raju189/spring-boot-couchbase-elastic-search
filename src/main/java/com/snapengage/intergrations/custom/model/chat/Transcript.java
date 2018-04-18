package com.snapengage.intergrations.custom.model.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Transcript {
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM d, yyyy h:mm:ss a")
    private Date date;
    private Long dateSeconds;
    private Long dateMilliseconds;
    private String alias;
    private String message;
}
