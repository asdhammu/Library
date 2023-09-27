import {Injectable} from '@angular/core';
import {Borrower} from '../model/borrower';
import {BookLoan} from '../model/book-loan';
import {CheckInBook} from '../model/check-in-book';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {Fine} from '../model/fine';
import {Book} from "../model/book";
import {PaginatedBook} from "../model/paginated-book";
import {environment} from "../../environments/environment";


@Injectable()
export class LibraryService {

  private apiURL = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  addBorrower(borrower: Borrower): Observable<any> {
    return this.http.post(this.apiURL + 'borrower', borrower);
  }

  search(searchQuery: string, page: number, size: number): Observable<PaginatedBook> {
    return this.http.get<PaginatedBook>(this.apiURL + 'search/books?q=' + searchQuery + '&page=' + page + '&size=' + size)
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
    return this.http.post(this.apiURL + 'borrower/checkout', bookLoan);
  }

  calculateFines(): Observable<any> {
    return this.http.post(this.apiURL + 'fine/calculate', {});
  }

  searchToCheckIn(checkIn: CheckInBook, page: number, size: number): Observable<PaginatedBook> {
    return this.http.get<PaginatedBook>(this.apiURL + 'search/borrower?name=' +
      checkIn.name + '&cardId=' + checkIn.cardId + '&isbn=' + checkIn.isbn + '&page=' + page + '&size=' + size)
      .pipe(map(paginatedBook => {
        return this.mapPaginatedBookData(paginatedBook);
      }));
  }

  checkIn(checkInBook: CheckInBook): Observable<any> {
    return this.http.post(this.apiURL + 'borrower/checkIn', checkInBook);
  }

  showFines(): Observable<any> {
    return this.http.get(this.apiURL + 'fines');
  }

  payFine(fine: Fine): Observable<any> {
    return this.http.post(this.apiURL + 'fine', fine);
  }

  getFineForCardId(cardId: number): Observable<any> {
    return this.http.get(this.apiURL + 'fine?cardId=' + cardId);
  }

}
