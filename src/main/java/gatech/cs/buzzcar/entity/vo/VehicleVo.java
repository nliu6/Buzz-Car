package gatech.cs.buzzcar.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleVo {

    private String vin;

    private String manufacturerName;

    private String vehicleType;

    private String modelName;

    private int modelYear;

    private String fuelType;

    private Integer mileage;

    private BigDecimal salePrice;

    private String colors;
}
