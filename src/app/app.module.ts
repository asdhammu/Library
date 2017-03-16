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

const appRoutes: Routes = [
  { path: 'addBorrower', component:  BorrowerComponent },
  { path:'search', component: SearchComponent},
  { path: 'addLoan/:isbn', component: CheckoutBook },
  {path:'checkIn', component:CheckInBook}/*,
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
  declarations: [ AppComponent, BorrowerComponent,  SearchComponent, CheckoutBook, CheckInBook],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
