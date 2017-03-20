/**
 * Created by asdha on 3/15/2017.
 */
import {Component, OnInit} from '@angular/core';
import {BookLoan} from "../Model/BookLoan";
import {LibraryService} from "../services/library.services";
import {RestResponse} from "../Model/RestResponse";
import {ActivatedRoute, Params} from "@angular/router";
import 'rxjs/add/operator/switchMap';
@Component({
  selector: 'checkout-book',
  templateUrl: 'app/components/checkOut-book.html',
  styleUrls: [ 'app/components/borrower.component.css' ],
  providers:[LibraryService]
})
export class CheckoutBook implements OnInit {

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

    this.libraryService.addLoan(this.bookLoan).then(result => this.restResponse = result);

  }


}
