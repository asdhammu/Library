/**
 * Created by asdha on 3/18/2017.
 */

import {Component, OnInit} from "@angular/core";
import {LibraryService} from "../../services/library.services";
import {ActivatedRoute, Params} from "@angular/router";
import {RestResponse} from "../../model/RestResponse";
import {FineResponse} from "../../model/FineResponse";

@Component({
  selector:"payment",
  templateUrl: './pay.component.html',
  styleUrls : ['./pay.component.css' ]
})


export class PayComponent implements OnInit{

  cardId:number;
  selectedValue:string;
  loanId:number;
  fine:FineResponse[];
  restResponse:RestResponse;
  constructor(
    public route:ActivatedRoute,
    public libraryService:LibraryService){}

  ngOnInit(){
    this.route.params.subscribe((params:Params)=> this.cardId = params['cardId']);
    this.libraryService.getFineForCardId(this.cardId, "false").subscribe(response=> this.fine = response);
  }

  payFine(fine:number){
    this.libraryService.payFine(fine).subscribe(response => this.restResponse = response);
  }

  onChange(s:string) {
    this.restResponse = null;
    this.libraryService.getFineForCardId(this.cardId,this.selectedValue).subscribe(response => this.fine = response);
  }

}
