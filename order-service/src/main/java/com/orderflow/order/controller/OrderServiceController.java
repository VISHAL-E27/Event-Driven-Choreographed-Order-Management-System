package com.orderflow.order.controller;

import com.orderflow.common.dto.ApiResponse;
import com.orderflow.order.dto.CreateOrderRequest;
import com.orderflow.order.dto.OrderResponse;
import com.orderflow.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderService")
@RequiredArgsConstructor
@Tag(
        name = "Order Management",
        description = "Endpoints for creating and retrieving orders"
)
public class OrderServiceController {

    private final OrderService orderService;

    @Operation(
            summary = "Create Order",
            description = "Places a new order and initiates the Choreography Saga event flow"
    )
    @ApiResponses(value = {

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Order created successfully"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload or missing fields",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Validation failed: Customer ID is required",
                                              "data": null,
                                              "timestamp": "2026-09-22T07:26:18.611Z"
                                            }
                                            """
                            )
                    )
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "An unexpected error occurred",
                                              "data": null,
                                              "timestamp": "2026-09-22T07:26:18.611Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/createOrder")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest createOrderRequest) {

        OrderResponse orderResponse =
                orderService.createOrder(createOrderRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Order created successfully",
                        orderResponse
                ));
    }


    @Operation(
            summary = "Get Order Details",
            description = "Fetches details of an order by Order ID"
    )
    @ApiResponses(value = {

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Order retrieved successfully"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Order not found with given ID",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Order not found with ID: 999",
                                              "data": null,
                                              "timestamp": "2026-09-22T07:26:18.611Z"
                                            }
                                            """
                            )
                    )
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "An unexpected error occurred",
                                              "data": null,
                                              "timestamp": "2026-09-22T07:26:18.611Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
            @Parameter(
                    description = "ID of the order to retrieve",
                    example = "1",
                    required = true
            )
            @PathVariable String orderId) {

        OrderResponse orderResponse =
                orderService.getOrderById(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order retrieved successfully",
                        orderResponse
                )
        );
    }
}