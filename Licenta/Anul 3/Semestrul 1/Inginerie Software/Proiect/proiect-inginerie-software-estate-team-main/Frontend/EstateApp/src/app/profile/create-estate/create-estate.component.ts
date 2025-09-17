import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EstateService } from 'src/services/estate.service';
import jwt_decode from 'jwt-decode';
import { AuthToken } from 'src/interfaces/AuthToken';
import { EstateImageService } from 'src/services/estateImages.service';

@Component({
  selector: 'app-create-estate',
  templateUrl: './create-estate.component.html',
  styleUrls: ['./create-estate.component.scss']
})
export class CreateEstateComponent implements OnInit {

  postEstateForm! : FormGroup;
  images : any = [];

  constructor(
    private formBuilder: FormBuilder,
    private estateService: EstateService,
    private estateImageService: EstateImageService
  ) {}

  ngOnInit(): void {
    this.postEstateForm = this.formBuilder.group({
      surface: ['', [Validators.required]],
      rooms: ['', [Validators.required]],
      price: ['', [Validators.required]],
      status: ['', [Validators.required]],
      sector: ['', [Validators.required]],
      neighborhood: ['', [Validators.required]],
      address: ['', [Validators.required]],
      description: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    var token = localStorage.getItem('token');
    if(token){
      var decode = jwt_decode<AuthToken>(token.toString())
      let user = decode["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"]
      this.estateService.PostEstate(user, this.postEstateForm.value['surface'],
      this.postEstateForm.value['price'], this.postEstateForm.value['rooms'],
      this.postEstateForm.value['status'], this.postEstateForm.value['description'],
      this.postEstateForm.value['sector'], this.postEstateForm.value['neighborhood'],
      this.postEstateForm.value['address']).subscribe(
        (rez : any) => {
          for(let i = 0; i < this.images.length; i++){
            var src = this.images[i]
            if(src){
              this.estateImageService.PostEstateImage(rez.estateID,src).subscribe(rez2 => {
                if(i == this.images.length - 1){
                  window.location.href = `http://localhost:4200/estate/${rez.estateID}`
                }
              });
            }
          }
        }
      );
    }
  }

  uploadImage($event : any): void{
    var file = new FileReader();
    file.readAsDataURL($event.target.files[0])
    file.onload = (event:any) => {
      var url = event.target.result;
      if(url){
        this.images.push(url);
      }
    }
  }
  deleteImage(image : any){
    this.images = this.images.filter( (obj: any) => obj != image);
  }
}
