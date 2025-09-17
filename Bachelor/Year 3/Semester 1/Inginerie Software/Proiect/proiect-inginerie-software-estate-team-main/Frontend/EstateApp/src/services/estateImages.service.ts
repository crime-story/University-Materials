import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
@Injectable({
    providedIn : "root"
})

export class EstateImageService{

    constructor(private httpClient : HttpClient){}

    PostEstateImage(estateID: any, URL: string){
            return this.httpClient.post('https://localhost:7173/api/EstateImage', {
                estateID,
                URL
            })
    }
}