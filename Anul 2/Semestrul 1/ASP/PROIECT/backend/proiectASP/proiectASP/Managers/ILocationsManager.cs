using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Entities;
using proiectASP.Models;

namespace proiectASP.Managers
{
    public interface ILocationsManager
    {
        List<Location> GetLocations();
        List<string> GetLocationsIdsList();
        List<Location> GetLocationsWithRestaurants();
        List<Location> GetLocationByCity(string city);
        Location GetLocationById(string id);
        void Create(LocationModel model);
        void Update(LocationModel model);
        void Delete(string id);
    }
}
