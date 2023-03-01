import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { subscriptionLogsToBeFn } from "rxjs/internal/testing/TestScheduler";
@Injectable({
    providedIn : "root"
})

export class UserDetailsService{
    constructor(private httpClient : HttpClient){}

    CreateUserDetails(fullname : string, username : string, age : number, residence : string, description : string, profileURL : string){
        return this.httpClient.post(`https://localhost:7173/api/UserDetails`,{
            fullname,
            username,
            age,
            residence,
            profileURL,
            description
          });
    }
    DeleteUserDetails(username: string){
        return this.httpClient.delete(`https://localhost:7173/api/UserDetails/${username}`)
    }
    GetUserDetails(username: string){
        return this.httpClient.get(`https://localhost:7173/api/UserDetails/${username}`)
    }
    UpdateUserDetails(username: string, age: number,fullName: string, residence: string, description: string, profileURL: string){
        return this.httpClient.put(`https://localhost:7173/api/UserDetails/${username}`, {
            fullName,
            age,
            residence,
            profileURL,
            description
        })
    }
}