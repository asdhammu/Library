import { Component } from '@angular/core';

@Component({
  selector: 'my-app',
  template: `<h1>{{name}}</h1>
  			<nav>
      <a routerLink="/addBorrower" routerLinkActive="active">Add Borrower</a>
      <a routerLink="/search" routerLinkActive="active">Search</a>
      <a routerLink="/checkIn" routerLinkActive="active">Check In</a>
      <a routerLink="/fine" routerLinkActive="active">Fines</a>
      <!--<a routerLink="/login" routerLinkActive="active">Login</a>
      <a [routerLink]="[{ outlets: { popup: ['compose'] } }]">Contact</a>-->
    </nav>
    <router-outlet></router-outlet>`
  ,
})
export class AppComponent  { name = 'Library Management System'; }
