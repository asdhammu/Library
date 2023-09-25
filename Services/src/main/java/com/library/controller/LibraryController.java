package com.library.controller;

import com.library.dto.BookPaginatedDTO;
import com.library.requests.BookLoanRequest;
import com.library.requests.CheckInBookRequest;
import com.library.requests.CreateBorrowerRequest;
import com.library.requests.PayFineRequest;
import com.library.services.LibraryServices;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LibraryController {

    final LibraryServices libraryServices;

    public LibraryController(LibraryServices libraryServices) {
        this.libraryServices = libraryServices;
    }

    @PostMapping(value = "/borrower")
    public ResponseEntity<?> addBorrower(@Valid @RequestBody CreateBorrowerRequest createBorrowerRequest) {
        return ResponseEntity.ok(libraryServices.addBorrower(createBorrowerRequest));
    }

    @PostMapping(value = "/checkoutBook")
    public ResponseEntity<?> checkoutBook(@Valid @RequestBody BookLoanRequest bookLoanRequest) {
        return ResponseEntity.ok(libraryServices.addBookLoan(bookLoanRequest));
    }

    @GetMapping("/search")
    public ResponseEntity<BookPaginatedDTO> search(@RequestParam(name = "q") String query, @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(libraryServices.searchBooks(query, pageable));
    }

    @GetMapping(value = "/searchCheckedInBooks")
    public ResponseEntity<?> searchToCheckIn(String name, int cardId, String isbn, @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(libraryServices.searchBooksForBorrower(name, cardId, isbn, pageable));
    }

    @PostMapping(value = "/checkInBook")
    public ResponseEntity<?> checkInBook(@Valid @RequestBody CheckInBookRequest book) {
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
    public ResponseEntity<?> payFine(@RequestBody PayFineRequest payFineRequest) {
        return ResponseEntity.ok(libraryServices.payFine(payFineRequest.getCardId()));
    }

    @GetMapping(value = "/getFineForCardId")
    public ResponseEntity<?> getFineForCardId(@RequestParam("cardId") int cardId) {
        return ResponseEntity.ok(libraryServices.getFineForCardId(cardId));
    }

}
