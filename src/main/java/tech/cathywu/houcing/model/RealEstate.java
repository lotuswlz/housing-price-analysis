package tech.cathywu.houcing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import tech.cathywu.houcing.annotation.CascadeCreate;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "real_estate")
public class RealEstate extends BaseDocument {

    private String name;
    private String company;
    private String address;
    private BigDecimal area;
    private String buildingNumberSnapshot;
    private int buildingCount;

    @DBRef
    private HousingAnnouncement announcement;

    @DBRef
    @CascadeCreate
    private List<Building> buildings;

    @JsonProperty("announcementId")
    public String getAnnouncementId() {
        return announcement.getId();
    }

    @JsonIgnore
    public HousingAnnouncement getAnnouncement() {
        return announcement;
    }

    @JsonProperty("announcement")
    public void setAnnouncement(HousingAnnouncement announcement) {
        this.announcement = announcement;
    }
}
