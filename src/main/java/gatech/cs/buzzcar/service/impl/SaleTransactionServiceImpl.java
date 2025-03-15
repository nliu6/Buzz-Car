package gatech.cs.buzzcar.service.impl;

import gatech.cs.buzzcar.entity.dto.SaleTransactionDto;
import gatech.cs.buzzcar.enums.SaleStatusEnum;
import gatech.cs.buzzcar.service.InventoryTransactionService;
import gatech.cs.buzzcar.service.SaleTransactionService;
import gatech.cs.buzzcar.utils.UserUtils;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SaleTransactionServiceImpl implements SaleTransactionService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private InventoryTransactionService inventoryTransactionService;
    @Override
    public int saveSaleTransaction(SaleTransactionDto dto) {
        dto.setSalesperson(UserUtils.getUsername());
        String sql = "insert into `sale_transaction`(`salesperson`,`buyer_id`,`vin`,`sale_date`,`sale_price`,`original_purchase_price`) values(?,?,?,?,?,?)";
        int row = jdbcTemplate.update(sql, dto.getSalesperson(), dto.getBuyerId(), dto.getVin(),dto.getSaleDate(), dto.getSalePrice(), dto.getOriginalPurchasePrice());
        if(row>0){
            // update sale status
            row = inventoryTransactionService.updateSaleStatus(dto.getVin(), SaleStatusEnum.sold.value());
        }
        return row;
    }
}
