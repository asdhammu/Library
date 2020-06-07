import { NgModule } from '@angular/core';
import { SearchCheckInBookComponent } from './check-in/searchCheckInBook.component';
import { SearchComponent } from './search.component';
import { SharedModule } from '../shared/shared.module';
import { SearchResultComponent } from './search-result/search-result.component';


@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    SearchCheckInBookComponent,
    SearchComponent,
    SearchResultComponent
  ]
})

export class SearchModule { }
