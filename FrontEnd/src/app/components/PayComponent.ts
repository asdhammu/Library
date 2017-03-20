/**
 * Created by asdha on 3/18/2017.
 */

import {Component, OnInit} from "@angular/core";
import {LibraryService} from "../services/library.services";
import {ActivatedRoute, Params} from "@angular/router";
import {RestResponse} from "../Model/RestResponse";
import {FineResponse} from "../Model/FineResponse";

@Component({

  selector:"payment",
  template:`
        
      <div class="selectedtab">
      <select [(ngModel)]="selectedValue" (ngModelChange)="onChange($event)">
        <option selected value="true" >Paid</option>
        <option value="false">Not Paid</option>
      </select>
      </div>
      <div *ngIf="fine">
          
          <table>
            <tr>
              <th>Card ID</th>
              <th>Amount</th>
              <th>Paid</th> 
            </tr>
            <tr *ngFor="let f of fine">
              <td>{{f.fineId}}</td>
              <td>{{f.fineAmount}}</td>
              <td>{{f.paid}}</td>
              <td><button (click)="payFine(f.fineId)">Pay Fine</button></td>    
              
            </tr>
            
          </table>
          <div *ngIf="restResponse">{{restResponse.result}}</div>
</div>
      

            
    `,
  providers:[LibraryService]
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
    this.libraryService.getFineForCardId(this.cardId, "false").then(response=> this.fine = response);
  }

  payFine(fine:number){
    this.libraryService.payFine(fine).then(response => this.restResponse = response);
  }

  onChange(s:string) {
    this.restResponse = null;
    this.libraryService.getFineForCardId(this.cardId,this.selectedValue).then(response => this.fine = response);
  }

}
