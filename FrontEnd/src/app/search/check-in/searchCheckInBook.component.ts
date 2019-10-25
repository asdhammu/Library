/**
 * Created by asdha on 3/15/2017.
 */
import {Component} from '@angular/core';
import {CheckInBookModel} from "../../model/CheckInBookModel";
import {SearchResult} from "../../model/SearchResult";
import {LibraryService} from "../../services/library.services";
import {Router} from "@angular/router";

@Component({

  templateUrl:'./searchCheckInBook.component.html',
  styleUrls: [ './searchCheckInBook.component.css' ],
  providers:[LibraryService]
})


export class SearchCheckInBookComponent{

  checkInBook = new CheckInBookModel("","","");

  constructor(
    private route:Router,
    private libraryService:LibraryService){};
  searchResult: SearchResult[];

  searchToCheckIn():void{
    this.libraryService.searchToCheckIn(this.checkInBook).subscribe(result=> this.searchResult = result);
  }

  onSubmit(isbn:string,cardId:string):void{
        this.route.navigate(['addCheckIn',isbn,cardId]);
  }



}
