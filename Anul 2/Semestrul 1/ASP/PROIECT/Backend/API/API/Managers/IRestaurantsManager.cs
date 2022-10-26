using API.Entities;
using API.Models;

namespace API.Managers
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
