import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EstateService } from 'src/services/estate.service';

@Component({
  selector: 'app-estates-list',
  templateUrl: './estates-list.component.html',
  styleUrls: ['./estates-list.component.scss']
})
export class EstatesListComponent implements OnInit{

  pageNumber: number = 0;
  estates: any;

  constructor(
    private route: ActivatedRoute,
    private estateService: EstateService
  ){}

  ngOnInit(): void {
    this.route.params.forEach(
      param => {
        this.pageNumber = param['pageNumber']
      }
    )
    this.estates = this.estateService.GetEstatesByPage(this.pageNumber);
    console.log(this.estates)
  }

  redirectTo(id: number){
    window.location.href = `http://localhost:4200/estate/${id}`
  }
  redirectBack(){
    let newPage = this.pageNumber - 1;
    window.location.href = `http://localhost:4200/estates/${newPage}`
  }
  redirectFront(){
    let newPage = this.pageNumber - (-1);
    window.location.href = `http://localhost:4200/estates/${newPage}`
  }
}
