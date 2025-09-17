import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EstateService } from 'src/services/estate.service';

@Component({
  selector: 'app-estate-page',
  templateUrl: './estate-page.component.html',
  styleUrls: ['./estate-page.component.scss']
})
export class EstatePageComponent implements OnInit{

  estateID: number = 0;
  estateData: any;

  constructor(
    private route: ActivatedRoute,
    private estateService: EstateService
  ){}

  ngOnInit(): void {
    this.route.params.forEach(
      param => {
        this.estateID = param['id']
    })
    this.estateService.GetEstateByID(this.estateID).subscribe(
      async (rez: any) => {
        this.estateData = rez
      }
    )
  }



  changeImage(event : any){
    var image = document.getElementById('firstImage')
    if(image){
      image.setAttribute('src', event);
    }
  }
}
