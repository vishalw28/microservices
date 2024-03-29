package org.techie.orderservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.techie.orderservice.dto.InventoryResponse;
import org.techie.orderservice.event.OrderPlacedEvent;
import org.techie.orderservice.repository.OrderRepository;
import org.techie.orderservice.dto.OrderLineItemDto;
import org.techie.orderservice.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.techie.orderservice.model.Order;
import org.techie.orderservice.model.OrderLineItem;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemDtos().stream()
            .map(item -> mapToDto(item)).collect(Collectors.toList());
        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = order.getOrderLineItems().stream().map(OrderLineItem::getSkuCode).toList();


        //Before placing order check in the inventory service whether stock is present or not
        //Here rater trigger request for each sku_code... collect the skuCode & send the collection
       /* Boolean result = webClient.get().uri("localhost:8083/api/inventory/",
                uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
            .retrieve().bodyToMono(Boolean.class)
            .block(); //block will make the sync call*/

        InventoryResponse[] result = webClientBuilder.build().get().uri("http://inventory-service/api/inventory",
                uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
            .retrieve().bodyToMono(InventoryResponse[].class)
            .block();
        boolean allProductInStock = Arrays.stream(result).allMatch(InventoryResponse::isInStock);
        if(allProductInStock) {
            orderRepository.save(order);
            try {
                kafkaTemplate.send("notification_topic", new OrderPlacedEvent(order.getOrderNumber()));
            }catch (Exception e){
                e.printStackTrace();
            }
            return "Order placed successfully";
        }else {
            throw new IllegalArgumentException("Product is not in stock. Please try again later.");
        }
    }

    private OrderLineItem mapToDto(OrderLineItemDto dto) {
        OrderLineItem item = OrderLineItem.builder()
            .price(dto.getPrice())
            .skuCode(dto.getSkuCode())
            .qty(dto.getQty())
            .build();
        return item;
    }
}
