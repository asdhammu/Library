package com.library.controller;

import com.library.requests.PayFineRequest;
import com.library.services.FineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FineController {

    private final FineService libraryService;

    public FineController(FineService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping(value = "/fine/calculate")
    public ResponseEntity<?> addOrUpdateFine() {
        return ResponseEntity.ok(libraryService.calculateFines());
    }

    @GetMapping(value = "/fines")
    public ResponseEntity<?> getAllFines() {
        return ResponseEntity.ok(libraryService.getAllFines());
    }

    @PostMapping(value = "/fine")
    public ResponseEntity<?> payFine(@RequestBody PayFineRequest payFineRequest) {
        return ResponseEntity.ok(libraryService.payFine(payFineRequest.getCardId()));
    }

    @GetMapping(value = "/fine")
    public ResponseEntity<?> getFineForCardId(@RequestParam("cardId") int cardId) {
        return ResponseEntity.ok(libraryService.getFineForCardId(cardId));
    }
}
