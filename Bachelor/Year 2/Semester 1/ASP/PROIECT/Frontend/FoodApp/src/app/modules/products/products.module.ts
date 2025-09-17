import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductsComponent } from './products/products.component';
import { MaterialModule } from '../material/material.module';
// import { MailFormatPipe } from 'src/app/mail-format.pipe';
import { ChildComponent } from './child/child.component';
import { ProductComponent } from './product/product.component';


@NgModule({
  declarations: [
    ProductsComponent,
    ChildComponent,
    ProductComponent
  ],
  imports: [
    CommonModule,
    ProductsRoutingModule,
    MaterialModule
  ],
})
export class ProductsModule { }
