using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Entities;

namespace proiectASP.Repositories
{
    public interface IRestaurantsRepository
    {
        IQueryable<Restaurant> GetRestaurantsIQueryable();
        IQueryable<Restaurant> GetRestaurantsWithEmployees();
        void Create(Restaurant restaurant);
        void Update(Restaurant restaurant);
        void Delete(Restaurant restaurant);
    }
}
