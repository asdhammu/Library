/**
 * Created by asdha on 3/15/2017.
 */
import {Component} from '@angular/core';
import {CheckInBookModel} from "../Model/CheckInBookModel";
import {SearchResult} from "../Model/SearchResult";
import {LibraryService} from "../services/library.services";

@Component({

  templateUrl:'app/components/checkIn-book.html',

})


export class CheckInBook{

  checkInBook = new CheckInBookModel("","","");

  constructor(private libraryService:LibraryService){};
  searchResult: SearchResult[];

  searchToCheckIn():void{
    this.libraryService.searchToCheckIn(this.checkInBook).then(result=> this.searchResult = result);
  }



}
