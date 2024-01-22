package persistence.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {
    private int pk;
    private String storeName;
    private String storeIntro;
    private String storeAddress;
    private String storeTel;
    private LocalTime storeStartHours;
    private LocalTime storeEndHours;
    private int storeManagerFk;
    private String response;
}