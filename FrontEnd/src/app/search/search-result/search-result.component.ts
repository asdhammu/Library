import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PaginatedBook} from "../../model/paginated-book";
import {PageEvent} from "@angular/material/paginator";
import {LibraryService} from "../../services/library.services";
import {CheckInBook} from "../../model/check-in-book";

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit {
  @Input() paginatedBook: PaginatedBook;
  @Input() isCheckIn: boolean;
  @Input() checkInBook: CheckInBook;
  @Input() query: string;
  @Output() checkOut = new EventEmitter();
  @Output() checkIn = new EventEmitter();

  constructor(private libraryService: LibraryService) {
  }

  ngOnInit() {
  }

  checkInEmit(isbn, cardId) {
    const obj = new CheckInEvent();
    obj.cardId = cardId;
    obj.isbn = isbn;
    this.checkIn.emit(obj);
  }

  checkOutEmit(isbn) {
    this.checkOut.emit(isbn);
  }

  fetchNext($event: PageEvent) {
    if (!this.isCheckIn) {
      this.libraryService.search(this.query, $event.pageIndex, $event.pageSize).subscribe(x => {
        this.paginatedBook = x;
      })
    } else {
      this.libraryService.searchToCheckIn(this.checkInBook, $event.pageIndex, $event.pageSize).subscribe(x => {
        this.paginatedBook = x;
      })
    }

  }
}

class CheckInEvent {
  cardId: number;
  isbn: string;
}
