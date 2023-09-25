import { NgModule } from '@angular/core';
import { SearchCheckInBookComponent } from './check-in/searchCheckInBook.component';
import { SearchComponent } from './search.component';
import { SharedModule } from '../shared/shared.module';
import { SearchResultComponent } from './search-result/search-result.component';
import {MatListModule} from "@angular/material/list";
import {MatCardModule} from "@angular/material/card";
import {NgOptimizedImage} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {MatPaginatorModule} from "@angular/material/paginator";


@NgModule({
  imports: [
    SharedModule,
    MatListModule,
    MatCardModule,
    NgOptimizedImage,
    MatButtonModule,
    MatInputModule,
    MatPaginatorModule
  ],
  declarations: [
    SearchCheckInBookComponent,
    SearchComponent,
    SearchResultComponent
  ]
})

export class SearchModule { }
