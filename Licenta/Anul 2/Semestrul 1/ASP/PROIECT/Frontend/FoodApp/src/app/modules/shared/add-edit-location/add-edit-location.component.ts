import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LocationsService } from 'src/app/services/locations.service';

@Component({
  selector: 'app-add-edit-location',
  templateUrl: './add-edit-location.component.html',
  styleUrls: ['./add-edit-location.component.scss']
})
export class AddEditLocationComponent implements OnInit {

  public locationForm: FormGroup = new FormGroup(
    {
      id: new FormControl(0),
      country: new FormControl(''),
      city: new FormControl(''),
      street: new FormControl(''),
    }
  );

  public title;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private locationsService: LocationsService,
    public dialogRef: MatDialogRef<AddEditLocationComponent>,
  ) {
    console.log(this.data);
    if (data.location) {
      this.title = 'Edit Location';
      this.locationForm.patchValue(this.data.location);
    } else {
      this.title = 'Add Location'
    }
   }

  // getters
  get id(): AbstractControl | null {
    return this.locationForm.get('id');
  }

  get country(): AbstractControl | null {
    return this.locationForm.get('country');
  }

  get city(): AbstractControl | null {
    return this.locationForm.get('city');
  }

  get street(): AbstractControl | null {
    return this.locationForm.get('street');
  }

  ngOnInit(): void {
  }

  public addLocation(): void {
    this.locationsService.addLocation(this.locationForm.value).subscribe(
      (result) => {
        console.log(result);
        this.dialogRef.close(result);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  public editLocation(): void {
    this.locationsService.editLocation(this.locationForm.value).subscribe(
      (result) => {
        console.log(result);
        this.dialogRef.close(result);
      },
      (error) => {
        console.log(error);
      }
    );
  }
}
