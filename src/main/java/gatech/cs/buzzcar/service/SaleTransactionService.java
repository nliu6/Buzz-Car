package gatech.cs.buzzcar.service;

import gatech.cs.buzzcar.entity.dto.SaleTransactionDto;

public interface SaleTransactionService {
    int saveSaleTransaction(SaleTransactionDto dto);
}
