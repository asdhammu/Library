package com.library.controller;

import com.library.entity.Borrower;
import com.library.modal.*;
import com.library.services.LibraryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LibraryController {

    @Autowired
    LibraryServices libraryServices;

    @PostMapping(value = "/borrower")
    public ResponseEntity<?> addBorrower(@RequestBody BorrowerData borrowerData) {

        Borrower borrower = new Borrower();
        borrower.setbName(borrowerData.getName());
        borrower.setAddress(borrowerData.getAddress());
        borrower.setPhone(borrowerData.getPhone());
        borrower.setSsn(borrowerData.getSsn());
        return ResponseEntity.ok(libraryServices.addBorrower(borrower));
    }

    @PostMapping(value = "/checkoutBook")
    public ResponseEntity<?> checkoutBook(@RequestBody BookLoanRequest bookLoanRequest) {
        return ResponseEntity.ok(libraryServices.addBookLoan(bookLoanRequest));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(name = "q") String query, @RequestParam(name = "p") int page, @RequestParam(name = "s") int size) {
        return ResponseEntity.ok(libraryServices.searchBooks(query, page, size));
    }

    @GetMapping(value = "/searchCheckedInBooks")
    public ResponseEntity<?> searchToCheckIn(String name, int cardId, String isbn) {
        return ResponseEntity.ok(libraryServices.searchBooksForBorrower(name, cardId, isbn));
    }

    @PostMapping(value = "/checkInBook")
    public ResponseEntity<?> checkInBook(@RequestBody CheckInBook book) {
        return ResponseEntity.ok(libraryServices.checkInBook(book));
    }

    @PostMapping(value = "/addOrUpdateFine")
    public ResponseEntity<?> addOrUpdateFine() {
        return ResponseEntity.ok(libraryServices.calculateFines());
    }

    @GetMapping(value = "/fines")
    public ResponseEntity<?> getAllFines() {
        return ResponseEntity.ok(libraryServices.getAllFines());
    }

    @PostMapping(value = "/payFine")
    public ResponseEntity<?> payFine(@RequestBody FineRequest fineRequest) {
        return ResponseEntity.ok(libraryServices.payFine(fineRequest.getCardId()));
    }

    @GetMapping(value = "/getFineForCardId")
    public ResponseEntity<?> getFineForCardId(@RequestParam("cardId") int cardId) {
        return ResponseEntity.ok(libraryServices.getFineForCardId(cardId));
    }

}
