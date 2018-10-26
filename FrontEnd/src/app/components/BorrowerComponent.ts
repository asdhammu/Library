import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {Borrower} from '../Model/Borrower';
import {LibraryService} from '../services/library.services';
import {RestResponse} from '../Model/RestResponse';
@Component({
	selector: 'borrower-component',
	templateUrl: 'app/components/borrower.component.html',
	styleUrls: [ 'app/components/borrower.component.css' ],
	providers: [LibraryService]
})

export class BorrowerComponent{


	borrower = new Borrower("", "","","");

    restResponse: RestResponse;    
    constructor(
    	private libraryService: LibraryService){}

    addBorrower():void{
    	this.libraryService.addBorrower(this.borrower).then(result => this.restResponse = result);
     }
}
