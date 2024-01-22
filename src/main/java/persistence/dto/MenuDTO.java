package persistence.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MenuDTO {
    private int pk;
    private int storeFk;
    private String menuCategory;
    private String menuName;
    private int menuPrice;
    private String menuAvailableOption;
    private int menuAmount;
    private String response;
}
