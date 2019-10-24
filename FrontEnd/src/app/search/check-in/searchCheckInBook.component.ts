/**
 * Created by asdha on 3/15/2017.
 */
import {Component} from '@angular/core';
import {CheckInBookModel} from "../../model/CheckInBookModel";
import {SearchResult} from "../../model/SearchResult";
import {LibraryService} from "../../services/library.services";
import {Router} from "@angular/router";

@Component({

  templateUrl:'app/search/check-in/searchCheckInBook.component.html',
  styleUrls: [ 'app/operation/borrower/borrower.component.css' ],
  providers:[LibraryService]
})


export class SearchCheckInBookComponent{

  checkInBook = new CheckInBookModel("","","");

  constructor(
    private route:Router,
    private libraryService:LibraryService){};
  searchResult: SearchResult[];

  searchToCheckIn():void{
    this.libraryService.searchToCheckIn(this.checkInBook).then(result=> this.searchResult = result);
  }

  onSubmit(isbn:string,cardId:string):void{
        this.route.navigate(['addCheckIn',isbn,cardId]);
  }



}
