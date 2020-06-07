import { Component, OnInit } from '@angular/core';
import { LibraryService } from 'src/app/services/library.services';
import { Fine } from 'src/app/model/fine';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-fine-detail',
  templateUrl: './fine-detail.component.html',
  styleUrls: ['./fine-detail.component.css']
})
export class FineDetailComponent implements OnInit {

  fines: Fine[];
  success: boolean;
  error: string;
  constructor(private libraryService: LibraryService,
    private route: Router) { }

  ngOnInit() {

    this.libraryService.showFines().subscribe(x => {
      this.success = true;
      this.fines = x;
    },
      e => {
        this.success = false;
        this.error = e.error.message;
      });
  }

  payFine(cardId: number, amount: number) {
    this.route.navigate(['fine/pay', cardId], {queryParams: {amount: amount}});
  }

}
