import {NgModule} from "@angular/core";
import {CheckInBookComponent} from "./check-in/CheckInBook.component";
import {CheckOutBookComponent} from "./check-out/checkOutBook.component";
import {SharedModule} from "../shared/shared.module";

@NgModule({
  imports: [SharedModule],
  declarations: [CheckInBookComponent, CheckOutBookComponent]
})

export class UserModule {}
