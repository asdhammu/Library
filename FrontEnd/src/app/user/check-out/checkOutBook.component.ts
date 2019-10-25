/**
 * Created by asdha on 3/15/2017.
 */
import {Component, OnInit} from '@angular/core';
import {BookLoan} from "../../model/BookLoan";
import {LibraryService} from "../../services/library.services";
import {RestResponse} from "../../model/RestResponse";
import {ActivatedRoute, Params} from "@angular/router";
import 'rxjs/add/operator/switchMap';
@Component({
  selector: 'checkout-book',
  templateUrl: './checkOutBook.component.html',
  styleUrls: [ './checkOutBook.component.css' ]
})
export class CheckOutBookComponent implements OnInit {

  bookLoan = new BookLoan("", "");

  restResponse: RestResponse;

  constructor(private route: ActivatedRoute,
              //private param: Params,
              private libraryService: LibraryService) {
  }

  isbn: string;

  ngOnInit(): void {

    this.route.params.subscribe((params: Params) => this.isbn = params['isbn']);
    console.log(this.isbn);

  }


  addLoan(): void {

    this.bookLoan.isbn  = this.isbn;

    this.libraryService.addLoan(this.bookLoan).subscribe(result => this.restResponse = result);

  }


}
