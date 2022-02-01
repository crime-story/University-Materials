using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using proiectASP.Entities;
using proiectASP.Models;
using proiectASP.Repositories;

namespace proiectASP.Managers
{
    public class RestaurantsManager : IRestaurantsManager
    {
        private readonly IRestaurantsRepository restaurantsRepository;
        public RestaurantsManager(IRestaurantsRepository restaurantsRepository)
        {
            this.restaurantsRepository = restaurantsRepository;
        }

        public List<Restaurant> GetRestaurants()
        {
            return restaurantsRepository.GetRestaurantsIQueryable().ToList();
        }

        public List<Restaurant> GetRestaurantsWithEmployees()
        {
            var restaurantsWithRestaurants = restaurantsRepository.GetRestaurantsWithEmployees();
            return restaurantsWithRestaurants.ToList();
        }

        public Restaurant GetRestaurantById(string id)
        {
            var restaurant = restaurantsRepository.GetRestaurantsIQueryable()
                .FirstOrDefault(x => x.Id == id);

            return restaurant;
        }

        public List<Restaurant> GetRestaurantByNumberOfEmployees(int minEmployees, int maxEmployees)
        {
            var restaurants = restaurantsRepository.GetRestaurantsWithEmployees()
                .Where(x => x.Employees.Count >= minEmployees && x.Employees.Count <= maxEmployees)
                .OrderBy(x => x.Employees.Count)
                .ToList();

            return restaurants;
        }

        public void Create(RestaurantModel model)
        {
            var newRestaurant = new Restaurant
            {
                Id = model.Id,
                Name = model.Name,
                LocationId = model.LocationId
            };

            restaurantsRepository.Create(newRestaurant);
        }

        public void Update(RestaurantModel model)
        {
            var restaurant = GetRestaurantById(model.Id);
            restaurant.Name = model.Name;

            restaurantsRepository.Update(restaurant);
        }

        public void Delete(string id)
        {
            var restaurant = GetRestaurantById(id);

            restaurantsRepository.Delete(restaurant);
        }
    }
}
