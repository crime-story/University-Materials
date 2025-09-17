import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoginserverService {
  public url="https://localhost:44334/api/authentication"
  constructor(
    private http:HttpClient
    ) { }

    public login (user : any):Observable<any>{
      return this.http.post(`${this.url}/login`, user);
    }
    public signup (user : any):Observable<any>{
      return this.http.post(`${this.url}/sign-up`, user);
    }

    // for user Roles
    // must add in getters something like {headers : this.loginserverService.generateHeader()});
    public generateHeader(): HttpHeaders{
      const token = localStorage.getItem('Role');
      var headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
      return headers;
    }
}
