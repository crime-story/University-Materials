import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddEditLocationComponent } from './add-edit-location/add-edit-location.component';
import { MaterialModule } from '../material/material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { HoverBtnDirective } from 'src/app/hover-btn.directive';
import { AddEditProductComponent } from './add-edit-product/add-edit-product.component';
import { MailFormatPipe } from 'src/app/mail-format.pipe';



@NgModule({
  declarations: [AddEditLocationComponent, HoverBtnDirective, AddEditProductComponent],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule
  ],
  entryComponents: [
    AddEditLocationComponent
  ],
  exports: [
    HoverBtnDirective
  ]
})
export class SharedModule { }
