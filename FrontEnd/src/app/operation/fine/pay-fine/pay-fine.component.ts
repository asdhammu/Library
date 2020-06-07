import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { LibraryService } from 'src/app/services/library.services';
import { Fine } from 'src/app/model/fine';

@Component({
  selector: 'app-pay-fine',
  templateUrl: './pay-fine.component.html',
  styleUrls: ['./pay-fine.component.css']
})
export class PayFineComponent implements OnInit {

  cardId: number;
  amount: number;
  submitted: boolean;
  success: boolean;
  msg: boolean;
  error: boolean;
  constructor(private route: ActivatedRoute,
    private libraryService: LibraryService) { }

  ngOnInit() {
    this.route.params.subscribe((params: Params) => this.cardId = params['cardId']);
    this.route.queryParams.subscribe(params => {
      this.amount = params['amount'];
    });
  }

  payFine(cardId: number) {
    this.submitted = false;
    const fineRequest = new Fine();
    fineRequest.cardId = cardId;
    this.libraryService.payFine(fineRequest).subscribe(x => {
      this.submitted =
        this.success = true;
      this.msg = x.message;
    }, e => {
      this.submitted = true;
      this.success = false;
      this.error = e.error.message;
    });
  }

}
