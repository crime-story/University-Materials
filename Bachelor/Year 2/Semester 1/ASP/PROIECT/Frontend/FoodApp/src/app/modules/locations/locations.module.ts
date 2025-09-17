import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LocationsRoutingModule } from './locations-routing.module';
import { LocationsComponent } from './locations/locations.component';
import { MaterialModule } from "../material/material.module";
import { ChildComponent } from './child/child.component';
import { LocationComponent } from './location/location.component';
import { MarksPipe } from 'src/app/marks.pipe';


@NgModule({
  declarations: [
    LocationsComponent,
    ChildComponent,
    LocationComponent,
    MarksPipe
  ],
  imports: [
    CommonModule,
    LocationsRoutingModule,
    MaterialModule,
  ],
  exports: [
    MarksPipe,
  ]
})
export class LocationsModule { }
