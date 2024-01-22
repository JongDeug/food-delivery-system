package persistence.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OptionDTO {
    private int pk;
    private int storeFk;
    private String optionName;
    private int optionPrice;
    private String response;
}
