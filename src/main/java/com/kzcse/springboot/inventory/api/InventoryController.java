package com.kzcse.springboot.inventory.api;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.inventory.data.InventoryEntity;
import com.kzcse.springboot.inventory.data.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * APIs:
 * <ul>
 *   <li>Product list</li>
 *   <li>Product details by ID</li>
 * </ul>
 */


@RestController
@RequestMapping("/api/product/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<String> add(@RequestBody List<InventoryEntity> entities) {
        try {
            service.updateInventoryOrThrow(entities);
            return new APIResponseDecorator<String>().onSuccess("Success");
        } catch (Exception e) {
            return new APIResponseDecorator<String>().withException(e, "Failed", this.getClass().getSimpleName());

        }

    }

    /**
     * <ul>
     *   <li>API that return all  of products list</li>
     * </ul>
     */

    /**
     * <ul>
     *   <li>API that return details of product by id</li>
     * </ul>
     */

    @GetMapping("/all")
    public ResponseEntity<List<InventoryEntity>> getAllProducts() {
        try {
            return ResponseEntity.ok(service.getAllOrThrows());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }


}


