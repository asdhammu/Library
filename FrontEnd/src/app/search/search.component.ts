import {Component, OnInit} from '@angular/core';
import {LibraryService} from '../services/library.services';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PaginatedBook} from "../model/paginated-book";

@Component({
  selector: '<search-component>',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  providers: [LibraryService]
})

export class SearchComponent implements OnInit{

  query: string;
  searchForm: FormGroup;
  paginatedBook: PaginatedBook;

  constructor(
    private router: Router,
    private libraryService: LibraryService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      "query": ["", Validators.required]
    })
  }

  search(): void {
    this.query = this.searchForm.value.query;
    this.libraryService.search(this.query, 0, 20).subscribe(x => {
      this.paginatedBook = x;
    });
  }

  checkoutBook(event: any) {
    this.router.navigate(['/addLoan', event]);
  }
}
