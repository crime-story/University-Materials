using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Entities;
using proiectASP.Models;

namespace proiectASP.Managers
{
    public interface IRestaurantsManager
    {
        List<Restaurant> GetRestaurants();
        List<Restaurant> GetRestaurantsWithEmployees();
        Restaurant GetRestaurantById(string id);
        List<Restaurant> GetRestaurantByNumberOfEmployees(int minEmployees, int maxEmployees);
        void Create(RestaurantModel model);
        void Update(RestaurantModel model);
        void Delete(string id);
    }
}
