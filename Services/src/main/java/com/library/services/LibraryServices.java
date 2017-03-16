package com.library.services;

import java.util.List;

import com.library.model.Borrower;
import com.library.model.Fine;
import com.library.rest.BookLoanRequest;
import com.library.rest.CheckInBook;
import com.library.rest.RestResponse;
import com.library.rest.SearchQuery;
import com.library.rest.SearchResult;

public interface LibraryServices {

	public RestResponse addBorrower(Borrower borrower);

	public void addFine(Fine fine);

	public void updateFine(Fine fine);

	public RestResponse addBookLoan(BookLoanRequest bookLoanRequest);

	public List<SearchResult> search(SearchQuery query);

	public List<SearchResult> checkInBookResult(CheckInBook book);

}
