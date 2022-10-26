using API.Entities;
using Microsoft.EntityFrameworkCore;

namespace API.Repositories
{
    public class RestaurantsRepository : IRestaurantsRepository
    {
        private readonly AppDbContext db;

        public RestaurantsRepository(AppDbContext db)
        {
            this.db = db;
        }

        public IQueryable<Restaurant> GetRestaurantsIQueryable()
        {
            var restaurants = db.Restaurants;

            return restaurants;
        }

        public IQueryable<Restaurant> GetRestaurantsWithEmployees()
        {
            var restaurants = GetRestaurantsIQueryable().Include(x => x.Employees);

            return restaurants;
        }

        public void Create(Restaurant restaurant)
        {
            db.Restaurants.Add(restaurant);

            db.SaveChanges();
        }

        public void Update(Restaurant restaurant)
        {
            db.Restaurants.Update(restaurant);

            db.SaveChanges();
        }

        public void Delete(Restaurant restaurant)
        {
            db.Restaurants.Remove(restaurant);

            db.SaveChanges();
        }
    }
}
