package tech.cathywu.houcing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "housing_announcement")
public class HousingAnnouncement extends BaseDocument {

    private String batchNumber;
    private String batchSubNumber;
    private String announceDate;

    @Transient
    private String location;

    public HousingAnnouncement(String id) {
        this.setId(id);
    }

    @JsonIgnore
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }
}
