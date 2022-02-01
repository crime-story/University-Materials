using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Entities;
using proiectASP.Repositories;
using proiectASP.Models;

namespace proiectASP.Managers
{
    public class LocationsManager : ILocationsManager
    {
        private readonly ILocationsRepository locationsRepository;

        public LocationsManager(ILocationsRepository locationsRepository)
        {
            this.locationsRepository = locationsRepository;
        }

        public List<Location> GetLocations()
        {
            return locationsRepository.GetLocationsIQueryable().ToList();
        }

        public List<string> GetLocationsIdsList()
        {
            var locations = locationsRepository.GetLocationsIQueryable();
            var idList = locations.Select(x => x.Id).ToList();

            return idList;
        }

        public List<Location> GetLocationsWithRestaurants()
        {
            var locationsWithRestaurants = locationsRepository.GetLocationsWithRestaurants();
            return locationsWithRestaurants.ToList();
        }

        public List<Location> GetLocationByCity(string city)
        {
            var locationsByCity = locationsRepository.GetLocationsIQueryable()
                .Where(x => x.City == city).ToList()
                .OrderBy(x => x.Id)
                .ToList();

            return locationsByCity;
        }
        public void Create(LocationModel model)
        {
            var newLocation = new Location
            {
                Id = model.Id,
                Country = model.Country,
                City = model.City,
                PostalCode = model.PostalCode,
                Street = model.Street
            };

            locationsRepository.Create(newLocation);
        }

        public Location GetLocationById(string id)
        {
            var location = locationsRepository.GetLocationsIQueryable()
                .FirstOrDefault(x => x.Id == id);

            return location;
        }

        public void Update(LocationModel model)
        {
            var location = GetLocationById(model.Id);

            location.Country = model.Country;
            location.City = model.City;
            location.PostalCode = model.PostalCode;
            location.Street = model.Street;

            locationsRepository.Update(location);
        }

        public void Delete(string id)
        {
            var location = GetLocationById(id);

            locationsRepository.Delete(location);
        }

    }
}
