/**
 * Created by asdha on 3/15/2017.
 */
import { Component } from '@angular/core';
import { CheckInBookModel } from '../../model/check-in-book';
import { SearchResult } from '../../model/search-result';
import { LibraryService } from '../../services/library.services';
import { Router } from '@angular/router';

@Component({

  templateUrl: './searchCheckInBook.component.html',
  styleUrls: ['./searchCheckInBook.component.css'],
  providers: [LibraryService]
})


export class SearchCheckInBookComponent {

  checkInBook = new CheckInBookModel('', '', '');
  searchResult: SearchResult[];
  constructor(
    private route: Router,
    private libraryService: LibraryService) { };


  searchToCheckIn(): void {
    this.libraryService.searchToCheckIn(this.checkInBook).subscribe(result => this.searchResult = result);
  }

  onSubmit(isbn: string, cardId: string): void {
    this.route.navigate(['addCheckIn', isbn, cardId]);
  }



}
