import { Component, OnInit } from '@angular/core';
import jwt_decode from 'jwt-decode';
import { Observable } from 'rxjs';
import { AuthToken } from 'src/interfaces/AuthToken';
import { EstateService } from 'src/services/estate.service';

@Component({
  selector: 'app-view-your-estates',
  templateUrl: './view-your-estates.component.html',
  styleUrls: ['./view-your-estates.component.scss']
})
export class ViewYourEstatesComponent implements OnInit{

  user: any;
  estates: any;

  constructor(
    private estateService: EstateService,
  ){}

  ngOnInit(): void {
    var token = localStorage.getItem('token');
    if(token){
      var decode = jwt_decode<AuthToken>(token.toString())
      this.user = decode["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"]
      if(this.user){
        this.estates = this.estateService.GetEstatesByUsername(this.user)
      }
    }
  }
  returnStatus(status: string){
    if(status == 'enable'){
      return 'diseable'
    }
    else{
      return 'enable'
    }
  }
  redirectTo(estateId:any){
    window.location.href =  `http://localhost:4200/estate/${estateId}`
  }
  editStatus(estate:any){
    if(estate.status == 'enable'){
      estate.status = 'diseable'
    }
    else{
      estate.status = 'enable'
    }
    this.estateService.EditEstate(estate).subscribe();
  }
  deleteEstate(estateID: number){
    this.estateService.DeleteService(estateID).subscribe(
      rez => window.location.reload()
    )
  }
}
