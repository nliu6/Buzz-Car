package gatech.cs.buzzcar.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaleTransactionDto {

    private String salesperson;

    @NotBlank
    private String buyerId;

    @NotBlank
    private String vin;

    @NotNull
    private LocalDate saleDate;
    @NotNull
    private BigDecimal salePrice;
    @NotNull
    private BigDecimal originalPurchasePrice;
}
