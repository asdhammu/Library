/**
 * Created by asdha on 3/15/2017.
 */
import { Component } from '@angular/core';
import { CheckInBookModel } from '../../model/check-in-book';
import { LibraryService } from '../../services/library.services';
import { Router } from '@angular/router';
import { Book } from 'src/app/model/Book';

@Component({

  templateUrl: './searchCheckInBook.component.html',
  styleUrls: ['./searchCheckInBook.component.css'],
  providers: [LibraryService]
})


export class SearchCheckInBookComponent {

  checkInBook = new CheckInBookModel('', '', '');
  books: Book[];
  constructor(
    private route: Router,
    private libraryService: LibraryService) { };


  searchToCheckIn(): void {
    this.libraryService.searchToCheckIn(this.checkInBook).subscribe(x => {
      this.books = x;
    });
  }

  onSubmit(isbn: string, cardId: string): void {
    this.route.navigate(['addCheckIn', isbn, cardId]);
  }



}
