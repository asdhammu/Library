import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {ReactiveFormsModule,FormsModule} from '@angular/forms';
import { AppComponent }  from './app.component';
import {BorrowerComponent} from './components/BorrowerComponent';
import {SearchComponent} from './components/SearchComponent';
import { RouterModule, Routes } from '@angular/router';
import { HttpModule, JsonpModule } from '@angular/http';
import {CheckoutBook} from "./components/CheckOutBook";

import {CheckInBook} from "./components/CheckInBook";
import {SearchCheckInBook} from "./components/SearchCheckInBook";
import {FineComponent} from "./components/FineComponent";
import {PayComponent} from "./components/PayComponent";

const appRoutes: Routes = [
  { path: 'addBorrower', component:  BorrowerComponent },
  { path:'search', component: SearchComponent},
  { path: 'addLoan/:isbn', component: CheckoutBook },
  {path:'checkIn', component:SearchCheckInBook},
  {path:'addCheckIn/:isbn/:borrowerId', component:CheckInBook},
  {path:'fine', component: FineComponent},
  {path:'pay/:cardId',component:PayComponent}
  /*,
  {
    path: 'heroes',
    component: HeroListComponent,
    data: { title: 'Heroes List' }
  },
  { path: '',
    redirectTo: '/heroes',
    pathMatch: 'full'
  },
  { path: '**', component: PageNotFoundComponent }*/
];



@NgModule({

  imports:[
  	RouterModule.forRoot(appRoutes),
  	BrowserModule,
  	FormsModule,
  	ReactiveFormsModule,
  	HttpModule,
    JsonpModule
  ],
  declarations: [ AppComponent, BorrowerComponent,  SearchComponent, CheckoutBook, CheckInBook, SearchCheckInBook, FineComponent,PayComponent],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
