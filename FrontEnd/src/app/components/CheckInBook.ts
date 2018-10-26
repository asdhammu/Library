/**
 * Created by asdha on 3/15/2017.
 */
import {Component, OnInit} from '@angular/core';
import {LibraryService} from "../services/library.services";
import {RestResponse} from "../Model/RestResponse";
import {ActivatedRoute, Params} from "@angular/router";
import 'rxjs/add/operator/switchMap';
import {CheckInBookModel} from "../Model/CheckInBookModel";
@Component({
  selector: 'checkIn-book',
  templateUrl: 'app/components/checkIn-book.html',
  styleUrls: [ 'app/components/borrower.component.css' ],
  providers:[LibraryService]
})
export class CheckInBook implements OnInit {


  checkInBookModel = new CheckInBookModel("","","");

  restResponse: RestResponse;

  constructor(private route: ActivatedRoute,
              private libraryService: LibraryService) {
  }

  isbn: string;
  cardId:string;
  ngOnInit(): void {

    this.route.params.subscribe((params: Params) => this.isbn = params['isbn']);
    this.route.params.subscribe((params: Params) => this.cardId = params['borrowerId']);

  }

  checkInBook(): void {
    this.libraryService.checkIn(this.isbn,this.cardId).then(result => this.restResponse = result);   
  }

}
