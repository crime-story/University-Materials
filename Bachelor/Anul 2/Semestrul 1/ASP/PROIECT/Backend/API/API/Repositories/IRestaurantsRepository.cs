using API.Entities;

namespace API.Repositories
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
