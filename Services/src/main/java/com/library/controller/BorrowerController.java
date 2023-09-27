package com.library.controller;

import com.library.requests.BookLoanRequest;
import com.library.requests.CheckInBookRequest;
import com.library.requests.CreateBorrowerRequest;
import com.library.services.BorrowerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BorrowerController {

    final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @PostMapping(value = "/borrower")
    public ResponseEntity<?> addBorrower(@Valid @RequestBody CreateBorrowerRequest createBorrowerRequest) {
        return ResponseEntity.ok(borrowerService.addBorrower(createBorrowerRequest));
    }

    @PostMapping(value = "/borrower/checkout")
    public ResponseEntity<?> checkoutBook(@Valid @RequestBody BookLoanRequest bookLoanRequest) {
        return ResponseEntity.ok(borrowerService.addBookLoan(bookLoanRequest));
    }



    @PostMapping(value = "/borrower/checkIn")
    public ResponseEntity<?> checkInBook(@Valid @RequestBody CheckInBookRequest book) {
        return ResponseEntity.ok(borrowerService.checkInBook(book));
    }



}
