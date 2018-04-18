package com.snapengage.intergrations.custom.model.chat;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class RequesterDetails {
    private String name;
    private List<String> emails;
    private String nameProfileLink;
    private List<String> phones;
    private String address;
    @JsonProperty("address_2")
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String companyName;
    private String companyProfileLink;
    private String employees;
    private String revenue;
    private String title;
    private String website;
    private List<String> socialProfileLinks;
    private String gender;
    private int age;
    private String influencerScore;
    private String notes;
    private String industry;
    private List<String> avatars;
    private List<String> otherData;
}
