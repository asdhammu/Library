import { Injectable } from '@angular/core';
import { Borrower } from '../model/borrower';
import { BookLoan } from '../model/book-loan';
import { CheckInBookModel } from '../model/check-in-book';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';


@Injectable()
export class LibraryService {

  private apiURL = 'http://localhost:8080/LibraryManagement/';

  constructor(private http: HttpClient) {
  }

  addBorrower(borrower: Borrower): Observable<any> {
    let payload = {
      'name': borrower.name,
      'ssn': borrower.ssn,
      'address': borrower.address,
      'phone': borrower.phone
    };

    return this.http.post(this.apiURL + 'addBorrower', JSON.stringify(payload));
  }

  search(searchQuery: string): Observable<any> {
    return this.http.get(this.apiURL + 'search?q=' + searchQuery);
  }

  addLoan(bookLoan: BookLoan): Observable<any> {
    let data = { 'isbn': bookLoan.isbn, 'borrowerId': bookLoan.borrowerId };
    return this.http.post(this.apiURL + 'checkoutBook', JSON.stringify(data));
  }

  addOrUpdateFine(): Observable<any> {
    return this.http.get(this.apiURL + 'addOrUpdateFine');
  }

  searchToCheckIn(checkIn: CheckInBookModel): Observable<any> {

    let data = { 'isbn': checkIn.isbn, 'name': checkIn.name, 'cardId': checkIn.cardId };
    return this.http.post(this.apiURL + 'searchCheckedInBooks', JSON.stringify(data));

  }


  checkIn(isbn: string, cardId: string): Observable<any> {
    let data = { 'isbn': isbn, 'cardId': cardId };
    return this.http.post(this.apiURL + 'checkInBook', JSON.stringify(data));
  }

  showFines(): Observable<any> {
    return this.http.get(this.apiURL + 'getAllFines');
  }

  payFine(cardId: number): Observable<any> {
    let data = { 'query': cardId };
    return this.http.post(this.apiURL + 'payFine', JSON.stringify(data));

  }

  getFineForCardId(cardId: number, paid: string): Observable<any> {
    let data = { 'query': cardId, 'paid': paid };
    return this.http.post(this.apiURL + 'getFineForCardId', JSON.stringify(data))
  }

}
