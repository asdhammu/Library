/**
 * Created by asdha on 3/15/2017.
 */
import {Component, OnInit} from '@angular/core';
import {CheckInBook} from '../../model/check-in-book';
import {LibraryService} from '../../services/library.services';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup} from '@angular/forms';
import {PaginatedBook} from "../../model/paginated-book";

@Component({

  templateUrl: './searchCheckInBook.component.html',
  styleUrls: ['./searchCheckInBook.component.css'],
  providers: [LibraryService]
})


export class SearchCheckInBookComponent implements OnInit {

  searchCheckInForm: FormGroup;
  paginatedBook: PaginatedBook;
  success: boolean;
  submitted: boolean;
  error: string;
  checkInBook: CheckInBook;
  constructor(
    private route: Router,
    private libraryService: LibraryService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {

    this.searchCheckInForm = this.formBuilder.group({
      isbn: [''],
      cardId: ['']
    });
  }

  searchToCheckIn() {
    this.submitted = false;
    if (!this.searchCheckInForm.valid) {
      return;
    }

    const checkIn = new CheckInBook();
    checkIn.isbn = this.searchCheckInForm.value.isbn;
    checkIn.cardId = this.searchCheckInForm.value.cardId;
    this.checkInBook = checkIn;
    this.libraryService.searchToCheckIn(this.checkInBook, 0, 20).subscribe(x => {
      this.submitted = true;
      this.paginatedBook = x;
    }, e => {
      this.submitted = true;
      this.success = false;
      this.error = e.error.message;
    });
  }

  checkIn(event: any) {
    this.route.navigate(['addCheckIn', event.isbn, event.cardId]);
  }
}
