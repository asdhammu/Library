import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { Borrower } from '../../model/borrower';
import { LibraryService } from '../../services/library.services';
import { RestResponse } from '../../model/rest-response';
@Component({
  selector: 'borrower-component',
  templateUrl: './borrower.component.html',
  styleUrls: ['./borrower.component.css']
})

export class BorrowerComponent {


  borrower = new Borrower('', '', '', '');

  restResponse: RestResponse;
  constructor(
    private libraryService: LibraryService) { }

  addBorrower(): void {
    this.libraryService.addBorrower(this.borrower).subscribe(result => this.restResponse = result);
  }
}
