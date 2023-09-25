package com.library.services;

import com.library.dto.*;
import com.library.requests.BookLoanRequest;
import com.library.requests.CheckInBookRequest;
import com.library.requests.CreateBorrowerRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LibraryServices {

    BorrowerDTO addBorrower(CreateBorrowerRequest createBorrowerRequest);

    ResponseDTO calculateFines();

    ResponseDTO payFine(int cardId);

    ResponseDTO addBookLoan(BookLoanRequest bookLoanRequest);

    ResponseDTO checkInBook(CheckInBookRequest book);

    List<FineDTO> getAllFines();

    List<FineDTO> getFineForCardId(int cardId);

    BookPaginatedDTO searchBooks(String query, Pageable pageable);

    BookPaginatedDTO searchBooksForBorrower(String borrowerName, int cardId, String isbn, Pageable pageable);

}
