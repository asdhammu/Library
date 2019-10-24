/**
 * Created by asdha on 3/16/2017.
 */
import {Component, OnInit} from "@angular/core";
import {LibraryService} from "../../services/library.services";
import {RestResponse} from "../../model/RestResponse";
import {FineResponse} from "../../model/FineResponse";
import {Router} from "@angular/router";

@Component({

  selector:"fine",
  templateUrl: './fine.component.html',
  styleUrls: [ 'app/operation/borrower/borrower.component.css' ]
})

export class FineComponent{

  selectedValue:string = "false";
  restResponse: RestResponse;
  showForm:boolean;
  fineResponse: FineResponse[];
  restResponse1:RestResponse;
  constructor(
    public router:Router,
    public libraryService: LibraryService
  ){}


  addOrUpdateFines():void{
    this.showForm = false;
    this.fineResponse = null;
    this.libraryService.addOrUpdateFine().then(response => this.restResponse = response);
  }

  showFines():void{
      this.showForm=true;
      this.restResponse = null;
      this.libraryService.showFines().then(response => this.fineResponse=response);
  }
  pay(cardId:number){
      this.router.navigate(['/pay',cardId]);
  }

}
