package com.library.services;

import java.util.List;

import com.library.dto.Book;
import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.modal.BookLoanRequest;
import com.library.modal.CheckInBook;
import com.library.modal.FineResponse;
import com.library.modal.RestResponse;
import com.library.modal.SearchQuery;
import com.library.modal.SearchResult;

public interface LibraryServices {

	com.library.dto.Borrower addBorrower(Borrower borrower);

	String calculateFines();

	String payFine(int cardId);

	String addBookLoan(BookLoanRequest bookLoanRequest);
	
	String checkInBook(CheckInBook book);

	List<FineResponse> getAllFines();
	
	List<Fine> getFineForCardId(int cardId);

	List<Book> searchBooks(String query, int page, int size);

	List<Book> searchBooksForBorrower(String borrowerName, int cardId, String isbn);

}
