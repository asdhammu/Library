import {Component, ViewChild} from '@angular/core';
import {SearchResult} from "../Model/SearchResult";
import {LibraryService} from "../services/library.services";
import {Router} from "@angular/router";
@Component({
	selector: '<search-component>',

	template: `
		<h3> Search Library Catalog</h3>

		<form (ngSubmit)="search()">
			<div class="form-group">
          <input type="text" size="75" class="form-control" placeholder="author,isbn,bookname" [(ngModel)]="query" name="Search">
				<button type="submit" class="btn btn-primary btn-block">Search</button>	
			</div>
		</form>
		
		<div *ngIf="searchResult">
		   Total Result : {{searchResult.length}}
		   <div [ngSwitch]="searchResult.length>0">
       <b *ngSwitchCase="true">
       <!--{{searchResult.book}}-->
       <ul class="heroes">
        <li *ngFor="let b of searchResult">
              <div >
              <div [ngSwitch]="b.cover.length>0">
                <b *ngSwitchCase="true"><span><img src="{{b.cover}}"/></span></b>
                <b *ngSwitchDefault><span><img src="no-image-found.jpg"/></span></b>
              </div>
             <span class="badge">{{b.title}}</span>
             <span class="badge">ISBN: {{b.isbn}}</span>
             <span class="badge"> Author:
             <span *ngFor="let a of b.author">
                 {{a.name}},
             </span>
             </span>
                                    
             <span [ngSwitch]="b.available">
                <b *ngSwitchCase="true"><span class="avail">Available</span></b>
                <b *ngSwitchDefault><span><span class="notAvail">Not Available</span></span></b>
             </span>
             </div>
             
             <span><button type="submit" (click)="onSubmit(b.isbn)">Checkout</button></span>
             
        </li>
             
             
      </ul>
              
      </b>
      <b *ngSwitchDefault>No Results found.</b>
      </div>
    </div>
		
	 `,

	styleUrls: [ 'app/components/borrower.component.css' ],

  providers:[LibraryService]
})

export class SearchComponent{

  query: string;

	searchResult: SearchResult[];

	constructor(
    private router : Router,
	  private libraryService:LibraryService){}

  search(): void{
      this.libraryService.search(this.query).then(result=>this.searchResult = result);
     // console.log(this.searchResult);

	}


	onSubmit(isbn:string):void{
      this.router.navigate(['/addLoan',isbn]);

  }

}
