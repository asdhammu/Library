import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {RouterModule} from '@angular/router';
import {appRoutes} from './app.routes';
import {LibraryService} from './services/library.services';
import {SearchModule} from './search/search.module';
import {OperationModule} from './operation/operation.module';
import {UserModule} from './user/user.module';
import {HttpClientModule} from '@angular/common/http';
import {SharedModule} from './shared/shared.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatCardModule} from "@angular/material/card";
import {MatCommonModule} from "@angular/material/core";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    HttpClientModule,
    SearchModule,
    OperationModule,
    UserModule,
    SharedModule,
    BrowserAnimationsModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatCardModule,
    MatCommonModule,
    MatButtonModule
  ],
  declarations: [AppComponent],
  providers: [LibraryService],
  bootstrap: [AppComponent]
})
export class AppModule { }
