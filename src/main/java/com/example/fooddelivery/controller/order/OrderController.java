package com.example.fooddelivery.controller.order;

import com.example.fooddelivery.dto.order.OrderCreateDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.order.OrderStatusUpdateDto;
import com.example.fooddelivery.enums.OrderStatus;
import com.example.fooddelivery.service.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 1. Създаване на поръчка
     * @param dto
     * @param clientId
     * @return
     */
    @PostMapping("/create/{clientId}")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderCreateDto dto,
                                                @PathVariable Long clientId) {
        return ResponseEntity.ok(orderService.createOrder(dto, clientId));
    }

    /**
     * 2. Взимане на поръчка от доставчик
     * @param orderId
     * @param supplierId
     * @return
     */
    @PutMapping("/{orderId}/assign/{supplierId}")
    public ResponseEntity<Void> assignOrder(@PathVariable Long orderId,
                                            @PathVariable Long supplierId) {
        orderService.assignOrderToSupplier(orderId, supplierId);

        return ResponseEntity.ok().build();
    }

    /**
     * 3. Обновяване на статус (служител)
     * @param dto
     * @param employeeId
     * @return
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(@RequestBody OrderStatusUpdateDto dto,
                                             @RequestParam Long employeeId) {
        orderService.updateOrderStatus(dto.getOrderId(), employeeId);
        return ResponseEntity.ok().build();
    }

    /**
     * 4. Завършване на поръчка (доставчик)
     * @param orderId
     * @param supplierId
     * @return
     */
    @PutMapping("/{orderId}/finish/{supplierId}")
    public ResponseEntity<OrderDto> finishOrder(@PathVariable Long orderId,
                                                @PathVariable Long supplierId) {
        return ResponseEntity.ok(orderService.finishOrder(orderId, supplierId));
    }

    /**
     * 5. Отказ от страна на клиента
     * @param orderId
     * @param clientId
     * @return
     */
    @PutMapping("/{orderId}/cancel/{clientId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId,
                                            @PathVariable Long clientId) {
        orderService.cancelOrderByClient(orderId, clientId);
        return ResponseEntity.ok().build();
    }

    /**
     * 6. Заявки по клиент
     * @param clientId
     * @return
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(orderService.getOrdersByClient(clientId));
    }

    /**
     * 7. Заявки по доставчик
     * @param supplierId
     * @return
     */
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(orderService.getOrdersBySupplier(supplierId));
    }

    /**
     * 8. Заявки по статус
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(OrderStatus.valueOf(status)));
    }

    /**
     * 9. Свободни заявки (за доставчици)
     * @return
     */
    @GetMapping("/available")
    public ResponseEntity<List<OrderResponseDto>> getAvailableOrders() {
        return ResponseEntity.ok(orderService.getAvailableOrdersForSuppliers());
    }

    /**
     * 10. Обща сума за период
     * @param from
     * @param to
     * @param employeeId
     * @return
     */
    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getRevenue(@RequestParam("from") LocalDateTime from,
                                                 @RequestParam("to") LocalDateTime to,
                                                 @RequestParam("employeeId") Long employeeId) {
        return ResponseEntity.ok(orderService.getTotalRevenueBetween(from, to, employeeId));
    }
}