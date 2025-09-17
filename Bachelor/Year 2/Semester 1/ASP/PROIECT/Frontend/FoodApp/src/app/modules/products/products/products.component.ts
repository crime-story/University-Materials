import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { DataService } from 'src/app/services/data.service';
import { ProductsService } from 'src/app/services/products.service';
import { AddEditProductComponent } from '../../shared/add-edit-product/add-edit-product.component';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  public subscription!: Subscription;
  public loggedUser!: { username: string; password: string; };
  public parentMessage = 'message from parent';
  public products = [];
  public displayedColumns = ['id', 'name', 'weight', 'dateOfPreparation', 'dateOfExpiration', 'description', 'info', 'edit', 'delete'];

  constructor(
    private router: Router, 
    private dataService: DataService,
    private ProductsService: ProductsService,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.subscription = this.dataService.currentUser.subscribe(

      (user: { username: string; password: string; }) => (this.loggedUser = user)
    );
    this.ProductsService.getAll().subscribe(
      (result: any) => {
        console.log(result);
        this.products = result;
      },
      (error: any) => {
        console.error(error);
      }
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public logout(): void {
    localStorage.setItem('Role', 'BasicUser');
    this.router.navigate(['/login']);
  }

  public receiveMessage(event: any): void {
    console.log(event);
  }

  public deleteProduct(id : any): void {
    this.ProductsService.deleteProduct(id).subscribe(
      (result) => {
        console.log(result);
        let newProducts = this.products.filter((x: any) => x.id !== id);
        this.products = newProducts;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  public openModal(product?: undefined): void {
    const data = {
      product
    };
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '650px';
    dialogConfig.height = '650px';
    dialogConfig.data = data;
    const dialogRef = this.dialog.open(AddEditProductComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      this.ProductsService.getAll().subscribe(
        (result) => {
          console.log(result);
          this.products = result;
        },
        (error) => {
          console.error(error);
        }
      );
    });
  }

  public addNewProduct(): void {
    this.openModal();
  }

  public goToProductInformations(id : any): void {
    this.router.navigate(['/products', id]);
  }

  public editProduct(product: any): void {
    this.openModal(product);
  }

}
