package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.request.DishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.EnableDishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.UpdateDishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.DishListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.DishResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.PaginationListResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.foodquart.microservicefoodcourt.infrastructure.documentation.APIDishDocumentationConstant.*;
import static com.foodquart.microservicefoodcourt.infrastructure.documentation.ResponseCode.*;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishRestController {
    private final IDishHandler dishHandler;

    @PostMapping("/")
    @Operation(summary = CREATE_DISH_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description = CREATE_DISH_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_400, description = CREATE_DISH_INVALID_INPUT_DESCRIPTION)
    @ApiResponse(responseCode = CODE_403, description = CREATE_DISH_UNAUTHORIZED_DESCRIPTION)
    public ResponseEntity<DishResponseDto> createDish(
            @Valid @RequestBody DishRequestDto dishRequestDto) {
        return ResponseEntity.ok(dishHandler.createDish(dishRequestDto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = UPDATE_DISH_SUMMARY)
    @ApiResponse(responseCode = CODE_204, description = UPDATE_DISH_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_400, description = UPDATE_DISH_INVALID_DATA_DESCRIPTION)
    @ApiResponse(responseCode = CODE_403, description = UPDATE_DISH_UNAUTHORIZED_DESCRIPTION)
    public ResponseEntity<DishResponseDto> updateDish(
            @PathVariable("id") Long dishId,
            @Valid @RequestBody UpdateDishRequestDto updateDishRequestDto) {
        return ResponseEntity.ok(dishHandler.updateDish(dishId, updateDishRequestDto));
    }

    @PatchMapping("/status/{id}")
    @Operation(summary = ENABLE_DISABLE_DISH_SUMMARY)
    @ApiResponse(responseCode = CODE_204, description = ENABLE_DISABLE_DISH_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_400, description = ENABLE_DISABLE_DISH_INVALID_INPUT_DESCRIPTION)
    @ApiResponse(responseCode = CODE_403, description = ENABLE_DISABLE_DISH_UNAUTHORIZED_DESCRIPTION)
    public ResponseEntity<DishResponseDto> enableOrDisableDish(
            @PathVariable("id") Long dishId,
            @Valid @RequestBody EnableDishRequestDto enableDishRequestDto) {
        return ResponseEntity.ok(dishHandler.enableOrDisableDish(dishId, enableDishRequestDto));
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = GET_DISHES_BY_RESTAURANT_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = GET_DISHES_BY_RESTAURANT_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_404, description = GET_DISHES_BY_RESTAURANT_NOT_FOUND_DESCRIPTION)
    public ResponseEntity<PaginationListResponseDto<DishListResponseDto>> getDishesByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationListResponseDto<DishListResponseDto> response =
                dishHandler.getDishesByRestaurant(restaurantId, category, page, size);

        if (response.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
}
