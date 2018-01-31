package tech.cathywu.houcing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "building")
public class Building extends BaseDocument {

    private String buildingNumber;
    private int floorCount;
    private List<House> houses;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class House {
        private String number;
        private BigDecimal area;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
}
