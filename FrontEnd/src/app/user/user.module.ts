import {NgModule} from "@angular/core";
import {CheckInBookComponent} from "./check-in/CheckInBook.component";
import {CheckOutBookComponent} from "./check-out/checkOutBook.component";

@NgModule({
  exports: [CheckInBookComponent, CheckOutBookComponent]
})

export class UserModule {}
