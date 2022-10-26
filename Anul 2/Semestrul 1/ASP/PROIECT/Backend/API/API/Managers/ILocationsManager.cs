using API.Entities;
using API.Models;

namespace API.Managers
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
