import {NgModule} from "@angular/core";
import {SearchCheckInBookComponent} from "./check-in/searchCheckInBook.component";
import {SearchComponent} from "./search.component";
import {SharedModule} from "../shared/shared.module";


@NgModule({
  imports :[
    SharedModule
  ],
  declarations: [
    SearchCheckInBookComponent,
    SearchComponent
  ]
})

export class SearchModule{}
