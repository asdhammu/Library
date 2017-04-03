package com.library.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.library.model.Borrower;
import com.library.model.Fine;
import com.library.services.LibraryServices;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RestController
public class RestAPI {

	@Autowired
	LibraryServices libraryServices;

	@RequestMapping(value = "/addBorrower", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> addBorrower(@RequestBody BorrowerData borrowerData) {

		Borrower borrower = new Borrower();
		borrower.setbName(borrowerData.getName());
		borrower.setAddress(borrowerData.getAddress());
		borrower.setPhone(borrowerData.getPhone());
		borrower.setSsn(borrowerData.getSsn());

		return new ResponseEntity<RestResponse>(libraryServices.addBorrower(borrower), HttpStatus.OK);
	}

	@RequestMapping(value = "/checkoutBook", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> checkoutBook(@RequestBody BookLoanRequest bookLoanRequest) {

		return new ResponseEntity<RestResponse>(libraryServices.addBookLoan(bookLoanRequest), HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<List<SearchResult>> search(@RequestBody SearchQuery query) {

		return new ResponseEntity<List<SearchResult>>(libraryServices.search(query), HttpStatus.OK);
	}

	@RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getString() {

		return new ResponseEntity<String>("Hello", HttpStatus.OK);

	}

	@RequestMapping(value = "/searchCheckedInBooks", method = RequestMethod.POST)
	public ResponseEntity<List<SearchResult>> searchToCheckIn(@RequestBody CheckInBook book) {

		return new ResponseEntity<List<SearchResult>>(libraryServices.searchCheckedInBooks(book), HttpStatus.OK);

	}

	@RequestMapping(value = "/checkInBook", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> checkInBook(@RequestBody CheckInBook book) {

		return new ResponseEntity<RestResponse>(libraryServices.checkInBook(book), HttpStatus.OK);

	}
	
	@RequestMapping(value="/addOrUpdateFine",method=RequestMethod.GET)
	public ResponseEntity<RestResponse> addOrUpdateFine(){
		
		return new ResponseEntity<RestResponse>(libraryServices.addFine(),HttpStatus.OK);
	}
	
	@RequestMapping(value="/getAllFines",method = RequestMethod.GET)
	public ResponseEntity<List<FineResponse>> getAllFines(){		
		return new ResponseEntity<List<FineResponse>>(libraryServices.getAllFines(),HttpStatus.OK);
	}
	
	@RequestMapping(value="/payFine",method = RequestMethod.POST)
	public ResponseEntity<RestResponse> payFine(@RequestBody SearchQuery paid){		
		return new ResponseEntity<RestResponse>(libraryServices.payFine(Integer.parseInt(paid.getQuery())),HttpStatus.OK);
	}
	
	@RequestMapping(value="/getFineForCardId",method = RequestMethod.POST)
	public ResponseEntity<List<Fine>> getFineForCardId(@RequestBody SearchQuery query){		
		return new ResponseEntity<List<Fine>>(libraryServices.getFineForCardId(query),HttpStatus.OK);
	}
	
	public LibraryServices getLibraryServices() {
		return libraryServices;
	}

	public void setLibraryServices(LibraryServices libraryServices) {
		this.libraryServices = libraryServices;
	}

}
