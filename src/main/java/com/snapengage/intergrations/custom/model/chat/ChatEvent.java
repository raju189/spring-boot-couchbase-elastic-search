package com.snapengage.intergrations.custom.model.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatEvent {
    private static final int DOCUMENT_VERSION = 1;

    @Id
    private UUID id;
    private String widgetId;
    private String url;
    private String snapshotImageUrl;
    private String type;
    private String requestedBy;

    private RequesterDetails requesterDetails;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM d, yyyy h:mm:ss a")
    private Date createdAtDate;
    private Long createdAtSeconds;
    private Long createdAtMilliseconds;
    private Boolean proactiveChat;
    private String pageUrl;
    private String referrerUrl;
    private String entryUrl;
    private String ipAddress;
    private String userAgent;
    private String browser;
    private String os;
    private String countryCode;
    private String country;
    private String region;
    private String city;
    private Double latitude;
    private Double longitude;
    private Long sourceId;
    private Long chatWaittime;
    private Long chatDuration;
    private String languageCode;

    private List<Transcript> transcripts;
    private List<Plugin> plugins;
    private List<JavascriptVariable> javascriptVariables;
    private List<OperatorVariable> operatorVariables;
}
