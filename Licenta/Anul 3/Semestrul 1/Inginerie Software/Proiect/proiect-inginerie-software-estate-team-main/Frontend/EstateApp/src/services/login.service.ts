import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
@Injectable({
    providedIn : "root"
})

export class LoginService{
    constructor(private httpClient : HttpClient){}

    Login(username : string, password : string){
        return this.httpClient.post(`https://localhost:7173/api/user/login`,{
            username,
            password,
          });
    }

    Auth(username : string, password : string, role : string){
        return this.httpClient.post(`https://localhost:7173/api/user/register`, {
            username,
            password,
            role
        });
    }
    SetRole(userName : string, role : string){
        const options = {responseType: 'text' as 'json'};
        return this.httpClient.post(`https://localhost:7173/api/users/assign-role?userName=${userName}&roleName=${role}`,"",options)
    }

    DeleteAccount(username: string, password: string){
        return this.httpClient.delete(`https://localhost:7173/api/user?username=${username}&password=${password}`)
    }
}