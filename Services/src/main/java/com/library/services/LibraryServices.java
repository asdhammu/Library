package com.library.services;

import java.util.List;

import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.modal.BookLoanRequest;
import com.library.modal.CheckInBook;
import com.library.modal.FineResponse;
import com.library.modal.RestResponse;
import com.library.modal.SearchQuery;
import com.library.modal.SearchResult;

public interface LibraryServices {

	public RestResponse addBorrower(Borrower borrower);

	public RestResponse addFine();

	public RestResponse payFine(int cardId);

	public RestResponse addBookLoan(BookLoanRequest bookLoanRequest);

	public List<SearchResult> search(SearchQuery query);

	public List<SearchResult> searchCheckedInBooks(CheckInBook book);
	
	public RestResponse checkInBook(CheckInBook book);

	public List<FineResponse> getAllFines();
	
	public List<Fine> getFineForCardId(SearchQuery query);
}
