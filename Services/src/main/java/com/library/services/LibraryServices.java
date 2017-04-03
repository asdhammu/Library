package com.library.services;

import java.util.List;

import com.library.model.Borrower;
import com.library.model.Fine;
import com.library.rest.BookLoanRequest;
import com.library.rest.CheckInBook;
import com.library.rest.FineResponse;
import com.library.rest.RestResponse;
import com.library.rest.SearchQuery;
import com.library.rest.SearchResult;

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
