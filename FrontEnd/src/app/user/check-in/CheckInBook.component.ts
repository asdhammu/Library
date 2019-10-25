/**
 * Created by asdha on 3/15/2017.
 */
import {Component, OnInit} from '@angular/core';
import {LibraryService} from "../../services/library.services";
import {RestResponse} from "../../model/RestResponse";
import {ActivatedRoute, Params} from "@angular/router";

@Component({
  selector: 'checkIn-book',
  templateUrl: './checkInBook.component.html',
  styleUrls: [ './checkInBook.component.css' ]
})
export class CheckInBookComponent implements OnInit {

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
    this.libraryService.checkIn(this.isbn,this.cardId).subscribe(result => this.restResponse = result);
  }

}
