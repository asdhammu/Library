package com.library.services;

import java.util.List;

import com.library.dto.Book;
import com.library.dto.Response;
import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.modal.BookLoanRequest;
import com.library.modal.CheckInBook;

public interface LibraryServices {

	com.library.dto.Borrower addBorrower(Borrower borrower);

	Response calculateFines();

	Response payFine(int cardId);

	Response addBookLoan(BookLoanRequest bookLoanRequest);
	
	Response checkInBook(CheckInBook book);

	List<com.library.dto.Fine> getAllFines();
	
	List<Fine> getFineForCardId(int cardId);

}
