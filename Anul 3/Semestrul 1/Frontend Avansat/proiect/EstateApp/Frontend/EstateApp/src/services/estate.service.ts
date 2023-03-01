import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
@Injectable({
    providedIn : "root"
})

export class EstateService{

    constructor(private httpClient : HttpClient){}

    PostEstate(username: string, surface: string, price: string, numberOfRooms: string,
        status: string, description: string, sector: string, neighborhood: string, address: string){
            return this.httpClient.post('https://localhost:7173/api/Estate', {
                username,
                surface,
                price,
                numberOfRooms,
                status,
                description,
                sector,
                neighborhood,
                address
            })
    }
    GetEstatesByUsername(username: string){
        return this.httpClient.get(`https://localhost:7173/username/${username}`)
    }
    GetEstateByID(estateID: number){
        return this.httpClient.get(`https://localhost:7173/api/Estate/${estateID}`)
    }
    GetEstatesByPage(pageNumber: number, pageSize = 20){
        return this.httpClient.get(`https://localhost:7173/api/Estate/pageNumber/${pageNumber}/pageSize/${pageSize}`)
    }
    EditEstate(estate:any){
        let username = estate.username;
        let surface = estate.surface;
        let price = estate.price;
        let numberOfRooms = estate.numberOfRooms;
        let status = estate.status;
        let description = estate.description;
        let sector = estate.sector;
        let neighborhood = estate.neighborhood;
        let address = estate.address
        return this.httpClient.put(`https://localhost:7173/api/Estate/${estate.estateID}`, {
            username,
            surface,
            price,
            numberOfRooms,
            status,
            description,
            sector,
            neighborhood,
            address
        })
    }
    DeleteService(estateID: number){
        return this.httpClient.delete(`https://localhost:7173/api/Estate/${estateID}`)
    }
}