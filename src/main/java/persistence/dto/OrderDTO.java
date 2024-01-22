package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class OrderDTO {
    private int pk;
    private int storeFk;
    private String memberId;
    private String orderMenu;
    private String orderOptions;
    private int orderPrice;
    private String orderStatus;
    private LocalDateTime orderDateTime;
    private String response;
}
