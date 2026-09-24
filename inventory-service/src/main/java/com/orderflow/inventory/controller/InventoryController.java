package com.orderflow.inventory.controller;

import com.orderflow.common.dto.ApiResponse;
import com.orderflow.inventory.dto.AddStockRequest;
import com.orderflow.inventory.dto.InventoryResponse;
import com.orderflow.inventory.service.InventoryService;
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

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(
        name = "Inventory Management",
        description = "Endpoints for managing product stock and reservations"
)
public class InventoryController {

    private final InventoryService inventoryService;


    @Operation(
            summary = "Add Stock",
            description = "Adds or updates product stock in inventory"
    )
    @ApiResponses(value = {

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Stock updated successfully"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid payload or negative stock quantity",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Validation failed: Quantity must be greater than 0",
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
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<InventoryResponse>> addStock(
            @Valid @RequestBody AddStockRequest request) {

        InventoryResponse response = inventoryService.addStock(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Stock updated successfully",
                        response
                ));
    }


    @Operation(
            summary = "Check Product Stock",
            description = "Retrieves current stock for a specific product ID"
    )
    @ApiResponses(value = {

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Stock details retrieved successfully"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found in inventory",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Product not found in inventory: 101",
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
    @GetMapping("/check/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getStock(
            @Parameter(
                    description = "ID of product to check stock for",
                    example = "101",
                    required = true
            )
            @PathVariable String productId) {

        InventoryResponse response =
                inventoryService.getInventoryByProductId(productId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Stock retrieved successfully",
                        response
                )
        );
    }


    @Operation(
            summary = "Get All Inventory",
            description = "Lists stock records for all registered products"
    )
    @ApiResponses(value = {

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Inventory list retrieved successfully"
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
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getAllInventory() {

        List<InventoryResponse> response =
                inventoryService.getAllInventory();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "All inventory retrieved successfully",
                        response
                )
        );
    }
}