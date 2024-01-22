package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreStatisticDTO {
    private int dayOrderNumber = 0;
    private int weekOrderNumber = 0;
    private int monthOrderNumber = 0;
    private int yearOrderNumber = 0;

    private int daySales = 0;
    private int weekSales = 0;
    private int monthSales = 0;
    private int yearSales = 0;
}
