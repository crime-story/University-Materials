using API.Entities;

namespace API.Repositories
{
    public interface ILocationsRepository
    {
        IQueryable<Location> GetLocationsIQueryable();
        IQueryable<Location> GetLocationsWithRestaurants();
        void Create(Location location);
        void Update(Location location);
        void Delete(Location location);
    }
}
