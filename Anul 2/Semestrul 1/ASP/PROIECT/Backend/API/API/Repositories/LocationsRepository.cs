using API.Entities;
using Microsoft.EntityFrameworkCore;

namespace API.Repositories
{
    public class LocationsRepository : ILocationsRepository
    {
        private readonly AppDbContext db;

        public LocationsRepository(AppDbContext db)
        {
            this.db = db;
        }

        public IQueryable<Location> GetLocationsIQueryable()
        {
            var locations = db.Locations;

            return locations;
        }

        public IQueryable<Location> GetLocationsWithRestaurants()
        {
            var locations = GetLocationsIQueryable().Include(x => x.Restaurant);

            return locations;
        }

        public void Create(Location location)
        {
            db.Locations.Add(location);

            db.SaveChanges();
        }

        public void Update(Location location)
        {
            db.Locations.Update(location);

            db.SaveChanges();
        }

        public void Delete(Location location)
        {
            db.Locations.Remove(location);

            db.SaveChanges();
        }
    }
}
