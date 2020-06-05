import { Component } from '@angular/core';
import { SearchResult } from '../model/search-result';
import { LibraryService } from '../services/library.services';
import { Router } from '@angular/router';

@Component({
  selector: '<search-component>',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  providers: [LibraryService]
})

export class SearchComponent {

  query: string;

  searchResult: SearchResult[];

  constructor(
    private router: Router,
    private libraryService: LibraryService) { }

  search(): void {
    this.libraryService.search(this.query).subscribe(result => this.searchResult = result);
  }


  onSubmit(isbn: string): void {
    this.router.navigate(['/addLoan', isbn]);
  }

}
