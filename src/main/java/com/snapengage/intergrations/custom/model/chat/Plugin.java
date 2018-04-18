package com.snapengage.intergrations.custom.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Plugin {
    private String name;
    private String value;
}
