import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../interfaces/product';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  public url = 'https://localhost:7225/api/products';
  constructor(
    public http: HttpClient,

  ) { }

  public getAll(): Observable<any> {
    return this.http.get(`${this.url}/getAll`);
  }

  public getProductById(id: any): Observable<any> {
    return this.http.get(`${this.url}/${id}`);
  }

  public addProduct(product: Product): Observable<any> {
    return this.http.post(`${this.url}/`, product);
  }

  public editProduct(product: Product): Observable<any> {
    return this.http.put(`${this.url}/`, product);
  }

  public deleteProduct(id: any): Observable<any> {
    return this.http.delete(`${this.url}/${id}`); 
  }
}
