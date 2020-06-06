import { Component } from '@angular/core';
import { LibraryService } from '../services/library.services';
import { Router } from '@angular/router';
import { Book } from '../model/Book';

@Component({
  selector: '<search-component>',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  providers: [LibraryService]
})

export class SearchComponent {

  query: string;

  books: Book[];

  constructor(
    private router: Router,
    private libraryService: LibraryService) { }

  search(): void {
    this.libraryService.search(this.query).subscribe(x => {
      this.books = x;
    });
  }


  onSubmit(isbn: string): void {
    this.router.navigate(['/addLoan', isbn]);
  }

}
