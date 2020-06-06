package com.library.controller;

import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.modal.*;
import com.library.services.LibraryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LibraryController {

    @Autowired
    LibraryServices libraryServices;

    @RequestMapping(value = "/addBorrower", method = RequestMethod.POST)
    public ResponseEntity<?> addBorrower(@RequestBody BorrowerData borrowerData) {

        Borrower borrower = new Borrower();
        borrower.setbName(borrowerData.getName());
        borrower.setAddress(borrowerData.getAddress());
        borrower.setPhone(borrowerData.getPhone());
        borrower.setSsn(borrowerData.getSsn());
        return ResponseEntity.ok(libraryServices.addBorrower(borrower));
    }

    @RequestMapping(value = "/checkoutBook", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> checkoutBook(@RequestBody BookLoanRequest bookLoanRequest) {

        return new ResponseEntity<>(libraryServices.addBookLoan(bookLoanRequest), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(name = "q") String query, @RequestParam(name = "p") int page, @RequestParam(name = "s") int size) {
        return ResponseEntity.ok(libraryServices.searchBooks(query, page, size));
    }

    @RequestMapping(value = "/searchCheckedInBooks", method = RequestMethod.POST)
    public ResponseEntity<?> searchToCheckIn(@RequestBody CheckInBook book) {
        return ResponseEntity.ok(libraryServices.searchCheckedInBooks(book));
    }

    @RequestMapping(value = "/checkInBook", method = RequestMethod.POST)
    public ResponseEntity<?> checkInBook(@RequestBody CheckInBook book) {
        return ResponseEntity.ok(libraryServices.checkInBook(book));
    }

    @RequestMapping(value = "/addOrUpdateFine", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> addOrUpdateFine() {
        return new ResponseEntity<>(libraryServices.addFine(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllFines", method = RequestMethod.GET)
    public ResponseEntity<List<FineResponse>> getAllFines() {
        return new ResponseEntity<>(libraryServices.getAllFines(), HttpStatus.OK);
    }

    @RequestMapping(value = "/payFine", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> payFine(@RequestBody SearchQuery paid) {
        return new ResponseEntity<RestResponse>(libraryServices.payFine(Integer.parseInt(paid.getQuery())), HttpStatus.OK);
    }

    @RequestMapping(value = "/getFineForCardId", method = RequestMethod.GET)
    public ResponseEntity<List<Fine>> getFineForCardId(@RequestBody SearchQuery query) {
        return new ResponseEntity<>(libraryServices.getFineForCardId(query), HttpStatus.OK);
    }

}
