import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { appRoutes } from './app.routes';
import { LibraryService } from './services/library.services';
import { SearchModule } from './search/search.module';
import { OperationModule } from './operation/operation.module';
import { UserModule } from './user/user.module';
import { HttpClientModule } from '@angular/common/http';
import { SharedModule } from './shared/shared.module';

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    HttpClientModule,
    SearchModule,
    OperationModule,
    UserModule,
    SharedModule
  ],
  declarations: [AppComponent],
  providers: [LibraryService],
  bootstrap: [AppComponent]
})
export class AppModule { }
