package org.techie.inventoryservice.service;

import lombok.extern.slf4j.Slf4j;
import org.techie.inventoryservice.dto.InventoryResponse;
import org.techie.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCOde) throws InterruptedException {

        return inventoryRepository.findBySkuCode(skuCOde).isPresent();
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStockForSkuCodes(List<String> skuCOde) throws InterruptedException {
//        log.info("Wait started...");
//        Thread.sleep(10000);
//        log.info("Wait ended...");
        return inventoryRepository.findBySkuCodeIn(skuCOde).stream()
            .map(inventory ->
                InventoryResponse.builder()
                    .skuCode(inventory.getSkuCode())
                    .isInStock(inventory.getQty() > 0)
                    .build()
            ).toList();
    }
}
