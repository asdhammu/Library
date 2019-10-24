import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AppComponent} from './app.component';
import {RouterModule} from '@angular/router';
import {HttpModule, JsonpModule} from '@angular/http';
import {appRoutes} from "./app.routes";
import {LibraryService} from "./services/library.services";
import {SearchModule} from "./search/search.module";
import {OperationModule} from "./operation/operation.module";
import {UserModule} from "./user/user.module";

@NgModule({

  imports:[
  	RouterModule.forRoot(appRoutes),
  	BrowserModule,
  	FormsModule,
  	ReactiveFormsModule,
  	HttpModule,
    JsonpModule,
    SearchModule,
    OperationModule,
    UserModule
  ],
  declarations: [ AppComponent],
  providers: [LibraryService],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
