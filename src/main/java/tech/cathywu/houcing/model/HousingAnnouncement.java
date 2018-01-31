package tech.cathywu.houcing.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "housing_announcement")
public class HousingAnnouncement extends BaseDocument {

    private String batchNumber;
    private String batchSubNumber;
    private String announceDate;
}
