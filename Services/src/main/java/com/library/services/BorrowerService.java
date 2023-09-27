package com.library.services;

import com.library.dto.BorrowerDTO;
import com.library.dto.ResponseDTO;
import com.library.requests.BookLoanRequest;
import com.library.requests.CheckInBookRequest;
import com.library.requests.CreateBorrowerRequest;

public interface BorrowerService {

    BorrowerDTO addBorrower(CreateBorrowerRequest createBorrowerRequest);
    ResponseDTO addBookLoan(BookLoanRequest bookLoanRequest);
    ResponseDTO checkInBook(CheckInBookRequest book);
}
