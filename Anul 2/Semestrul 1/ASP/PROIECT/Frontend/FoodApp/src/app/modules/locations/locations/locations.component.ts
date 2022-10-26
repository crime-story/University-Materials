import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { DataService } from 'src/app/services/data.service';
import { LocationsService } from 'src/app/services/locations.service';
import { AddEditLocationComponent } from '../../shared/add-edit-location/add-edit-location.component';
import { Location } from '../../../interfaces/location';

@Component({
  selector: 'app-locations',
  templateUrl: './locations.component.html',
  styleUrls: ['./locations.component.scss'],
})
export class LocationsComponent implements OnInit, OnDestroy {
  public subscription!: Subscription;
  public loggedUser!: { username: string; password: string; };
  public parentMessage = 'message from parent';
  public locations = [];
  public displayedColumns = ['id', 'country', 'city', 'street', 'info', 'edit', 'delete'];

  constructor(
    private router: Router, 
    private dataService: DataService,
    private locationsService: LocationsService,
    public dialog: MatDialog,
    ) { }

  ngOnInit() {
    this.subscription = this.dataService.currentUser.subscribe(

      (user) => (this.loggedUser = user)
    );
    this.locationsService.getAll().subscribe(
      (result) => {
        console.log(result);
        this.locations = result;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public logout(): void {
    localStorage.setItem('Role', 'BasicUser');
    this.router.navigate(['/auth']); // redirecting to login page :)
  }

  public receiveMessage(event: any): void {
    console.log(event);
  }

  public deleteLocation(id : any): void {
    this.locationsService.deleteLocation(id).subscribe(
      (result) => {
        console.log(result);
        let newLocations = this.locations.filter((x: any) => x.id !== id);
        this.locations = newLocations;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  public openModal(location?: undefined): void {
    const data = {
      location
    };
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '550px';
    dialogConfig.height = '700px';
    dialogConfig.data = data;
    const dialogRef = this.dialog.open(AddEditLocationComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      this.locationsService.getAll().subscribe(
        (result) => {
          console.log(result);
          this.locations = result;
        },
        (error) => {
          console.error(error);
        }
      );
    });
  }

  public addNewLocation(): void {
    console.log("addnewLocation");
    this.openModal();
  }

  public editLocation(location: any): void {
    this.openModal(location);
  }

  public goToLocationInformations(city : any): void {
    let ceva = this.router.navigate(['/location', city]);
  }

}