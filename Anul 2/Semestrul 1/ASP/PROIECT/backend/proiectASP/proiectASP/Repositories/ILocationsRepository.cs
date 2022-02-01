using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Entities;

namespace proiectASP.Repositories
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
