using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using proiectASP.Contexts;
using proiectASP.Entities;

namespace proiectASP.Repositories
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
