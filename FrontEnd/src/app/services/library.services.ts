import {Injectable} from '@angular/core';
import {Borrower} from '../model/borrower';
import {BookLoan} from '../model/book-loan';
import {CheckInBook} from '../model/check-in-book';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {Fine} from '../model/fine';
import {Book} from "../model/book";
import {PaginatedBook} from "../model/paginated-book";


@Injectable()
export class LibraryService {

  private apiURL = 'http://localhost:8080/';

  constructor(private http: HttpClient) {
  }

  addBorrower(borrower: Borrower): Observable<any> {
    return this.http.post(this.apiURL + 'borrower', borrower);
  }

  search(searchQuery: string, page: number, size: number): Observable<PaginatedBook> {
    return this.http.get<PaginatedBook>(this.apiURL + 'search?q=' + searchQuery + '&page=' + page + '&size=' + size)
      .pipe(map(paginatedBook => {
        return this.mapPaginatedBookData(paginatedBook);
      }));
  }

  private mapPaginatedBookData(paginatedBook: PaginatedBook) {
    let books = paginatedBook.books.map(b => {
      const book = new Book();
      book.authors = b.authors;
      book.isbn = b.isbn;
      book.concatenatedAuthors = b.authors.map(z => z.name).join(", ")
      book.cover = b.cover;
      book.title = b.title;
      book.pages = b.pages;
      book.available = b.available;
      if(b.borrower){
        const borrower = new Borrower();
        borrower.cardId = b.borrower.cardId;
        borrower.name = b.borrower.name;
        book.borrower = borrower;
      }
      return book;
    });
    let page = paginatedBook.pagination;
    const paginated = new PaginatedBook();
    paginated.pagination = page;
    paginated.books = books;
    return paginated;
  }

  addLoan(bookLoan: BookLoan): Observable<any> {
    return this.http.post(this.apiURL + 'checkoutBook', bookLoan);
  }

  calculateFines(): Observable<any> {
    return this.http.post(this.apiURL + 'addOrUpdateFine', {});
  }

  searchToCheckIn(checkIn: CheckInBook, page: number, size: number): Observable<PaginatedBook> {
    return this.http.get<PaginatedBook>(this.apiURL + 'searchCheckedInBooks?name=' +
      checkIn.name + '&cardId=' + checkIn.cardId + '&isbn=' + checkIn.isbn + '&page=' + page + '&size=' + size)
      .pipe(map(paginatedBook => {
        return this.mapPaginatedBookData(paginatedBook);
      }));
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
