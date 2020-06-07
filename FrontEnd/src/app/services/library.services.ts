import { Injectable } from '@angular/core';
import { Borrower } from '../model/borrower';
import { BookLoan } from '../model/book-loan';
import { CheckInBook } from '../model/check-in-book';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { Fine } from '../model/fine';


@Injectable()
export class LibraryService {

  private apiURL = 'http://localhost:8080/';

  constructor(private http: HttpClient) {
  }

  addBorrower(borrower: Borrower): Observable<any> {
    return this.http.post(this.apiURL + 'borrower', borrower);
  }

  search(searchQuery: string): Observable<any> {
    return this.http.get(this.apiURL + 'search?q=' + searchQuery + '&p=0&s=10');
  }

  addLoan(bookLoan: BookLoan): Observable<any> {
    return this.http.post(this.apiURL + 'checkoutBook', bookLoan);
  }

  calculateFines(): Observable<any> {
    return this.http.post(this.apiURL + 'addOrUpdateFine', {});
  }

  searchToCheckIn(checkIn: CheckInBook): Observable<any> {
    return this.http.get(this.apiURL + 'searchCheckedInBooks?name=' +
      checkIn.name + '&cardId=' + checkIn.cardId + '&isbn=' + checkIn.isbn);

  }

  checkIn(checkInBook: CheckInBook): Observable<any> {
    return this.http.post(this.apiURL + 'checkInBook', checkInBook);
  }

  showFines(): Observable<any> {
    return this.http.get(this.apiURL + 'fines');
  }

  payFine(fine: Fine): Observable<any> {
    return this.http.post(this.apiURL + 'payFine', fine);
  }

  getFineForCardId(cardId: number): Observable<any> {
    return this.http.get(this.apiURL + 'getFineForCardId?cardId=' + cardId);
  }

}
