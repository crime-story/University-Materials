import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Product } from 'src/app/interfaces/product';
import { ProductsService } from 'src/app/services/products.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  public subscription!: Subscription;
  public id: any;
  public product: Product = {
    name: 'Default name',
    weight: 0,
    dateOfPreparation: 'Default dateOfPreparation',
    dateOfExpiration: 'Default dateOfExpiration',
    description: 'Default description',
    id: 'Default ID',
  };

  constructor(
    private route: ActivatedRoute,
    private ProductsService: ProductsService,
  ) { }

  ngOnInit() {
    this.subscription = this.route.params.subscribe(params => {
      this.id = params['id'];
      console.log(this.id);      
      if (this.id) {
        this.getProduct();
      }
    });
  }

  public getProduct(): void {
    this.ProductsService.getProductById(this.id).subscribe(
      (result: Product) => {
        console.log(result);
        this.product = result;
      },
      (error: any) => {
        console.error(error);
      }
    )
  }

}
