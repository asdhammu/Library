/**
 * Created by asdha on 3/16/2017.
 */
import {Component, OnInit} from "@angular/core";
import {LibraryService} from "../services/library.services";
import {RestResponse} from "../Model/RestResponse";
import {FineResponse} from "../Model/FineResponse";
import {Router} from "@angular/router";

@Component({

  selector:"fine",
  template:`
    
    <button (click)="addOrUpdateFines()">Update Fines</button>
    <button (click)="showFines()" >Show Fines</button>
    
    <div *ngIf="restResponse" class="form-group">
              <div [ngSwitch]=restResponse.success>
                <b *ngSwitchCase="true">{{restResponse.result}}</b>
                <b *ngSwitchDefault>{{restResponse.error}}</b>
              </div>  
              <!-- <div *ngIf={{restResponse.success}}>
              Compilation Result : {{restResponse.result}}
              <br/>
              {{restResponse.error}} -->
        </div>  
     <!--<div *ngIf="showForm" class="selectedtab">
      <select [(ngModel)]="selectedValue" (ngModelChange)="onChange($event)">
        <option selected value="true" (click)="fines()" >Paid</option>
        <option value="false" (click)="fines()">Not Paid</option>
      </select>-->
     
        
     <div *ngIf="fineResponse">
       <!-- <div *ngFor="let f of fineResponse">-->
          <table border="0">
            <thead>
            <tr>
              <th>Card Id</th>
              <th>Amount</th>
            </tr>
            </thead>
            <tbody>
            
            <tr *ngFor="let f of fineResponse">              
              <td>{{f.cardId}}</td>
              <td>$ {{f.amount}}</td>
              <td *ngIf="selectedValue=='false'"><button (click)="pay(f.cardId)">Pay</button></td>
              <td *ngIf="restResponse1">{{restResponse1.result}}</td>
             </tr>
             
            </tbody>
          </table>          
        <!--</div>-->
         
     </div>   
  `,
  styleUrls: [ 'app/components/borrower.component.css' ],
  providers:[LibraryService]



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
