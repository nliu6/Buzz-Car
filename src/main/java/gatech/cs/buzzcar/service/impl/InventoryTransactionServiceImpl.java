package gatech.cs.buzzcar.service.impl;

import gatech.cs.buzzcar.service.InventoryTransactionService;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryTransactionServiceImpl implements InventoryTransactionService {



    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public int updateSaleStatus(String vin, String saleStatus) {
        String updateSql = "update inventory_transaction set sale_status=? where vin=?";
        return jdbcTemplate.update(updateSql, saleStatus, vin);
    }
}
