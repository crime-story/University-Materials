import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProductsService } from 'src/app/services/products.service';

@Component({
  selector: 'app-add-edit-product',
  templateUrl: './add-edit-product.component.html',
  styleUrls: ['./add-edit-product.component.scss']
})
export class AddEditProductComponent implements OnInit {

  public productForm: FormGroup = new FormGroup(
    {
      id: new FormControl(0),
      name: new FormControl(''),
      weight: new FormControl(0),
      dateOfPreparation: new FormControl(''),
      dateOfExpiration: new FormControl(''),
      description: new FormControl(''),
    }
  );

  public title;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private productsService: ProductsService,
    public dialogRef: MatDialogRef<AddEditProductComponent>,
  ) {
    console.log(this.data);
    if (data.product) {
      this.title = 'Edit Product';
      this.productForm.patchValue(this.data.product);
    } else {
      this.title = 'Add Product'
    }
   }

  // getters
  get id(): AbstractControl | null {
    return this.productForm.get('id');
  }

  get name(): AbstractControl | null {
    return this.productForm.get('name');
  }

  get weight(): AbstractControl | null {
    return this.productForm.get('weight');
  }

  get dateOfPreparation(): AbstractControl | null {
    return this.productForm.get('dateOfPreparation');
  }

  get dateOfExpiration(): AbstractControl | null {
    return this.productForm.get('dateOfExpiration');
  }

  get description(): AbstractControl | null {
    return this.productForm.get('description');
  }

  ngOnInit(): void {
  }

  public addProduct(): void {
    this.productsService.addProduct(this.productForm.value).subscribe(
      (result : any) => {
        console.log(result);
        this.dialogRef.close(result);
      },
      (error : any) => {
        console.log(error);
      }
    );
  }

  public editProduct(): void {
    this.productsService.editProduct(this.productForm.value).subscribe(
      (result : any) => {
        console.log(result);
        this.dialogRef.close(result);
      },
      (error : any) => {
        console.log(error);
      }
    );
  }

}
