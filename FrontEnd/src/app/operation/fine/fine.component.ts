/**
 * Created by asdha on 3/16/2017.
 */
import { Component, OnInit } from '@angular/core';
import { LibraryService } from '../../services/library.services';
import { FineResponse } from '../../model/fine-response';
import { Router } from '@angular/router';

@Component({

  selector: 'fine',
  templateUrl: './fine.component.html',
  styleUrls: ['./fine.component.css']
})

export class FineComponent {

  selectedValue = 'false';
  // restResponse: RestResponse;
  showForm: boolean;
  fineResponse: FineResponse[];
  // restResponse1: RestResponse;
  error: string;
  isError: boolean;
  constructor(
    public router: Router,
    public libraryService: LibraryService
  ) { }


  addOrUpdateFines(): void {
    this.showForm = false;
    this.fineResponse = null;
    this.libraryService.addOrUpdateFine().subscribe(x => {
      console.log('Fines updated');
    });
  }

  showFines(): void {
    this.showForm = true;
    this.error = '';
    this.isError = false;
    this.libraryService.showFines().subscribe(response => this.fineResponse = response,
      () => {
        this.isError = true;
        this.error = 'Error has occurred. Please try after some time';
      });

  }
  pay(cardId: number) {
    this.router.navigate(['/pay', cardId]);
  }

}
