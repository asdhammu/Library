/**
 * Created by asdha on 3/15/2017.
 */
import {Component} from '@angular/core';
import {CheckInBookModel} from "../Model/CheckInBookModel";
import {SearchResult} from "../Model/SearchResult";
import {LibraryService} from "../services/library.services";
import {Router} from "@angular/router";

@Component({

  templateUrl:'app/components/search.checkIn-book.html',
  styleUrls: [ 'app/components/borrower.component.css' ],
  providers:[LibraryService]
})


export class SearchCheckInBook{

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
