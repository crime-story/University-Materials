using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using proiectASP.Managers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using proiectASP.Models;

namespace proiectASP.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RestaurantController : ControllerBase
    {
        private readonly IRestaurantsManager manager;
        public RestaurantController(IRestaurantsManager restaurantManager)
        {
            this.manager = restaurantManager;
        }

        [HttpGet("getAll")]
        public async Task<IActionResult> GetRestaurants()
        {
            var restaurants = manager.GetRestaurants();

            return Ok(restaurants);
        }

        [HttpGet("GetWithEmployees")]
        [Authorize(Policy = "BasicUser")]
        public async Task<IActionResult> GetRestaurantsWithEmployees()
        {
            var restaurantsWithEmployees = manager.GetRestaurantsWithEmployees();

            return Ok(restaurantsWithEmployees);
        }
        
        [HttpGet("{id}")]
        [Authorize(Policy = "BasicUser")]
        public async Task<IActionResult> GetRestaurantsById([FromRoute] string id)
        {
            var restaurant = manager.GetRestaurantById(id);

            return Ok(restaurant);
        }

        [HttpGet("filter")]
        [Authorize(Policy = "BasicUser")]
        public async Task<IActionResult> GetRestaurantsByNumberOfEmployees([FromRoute] int minEmployees, int maxEmployees)
        {
            var restaurantsByMenuPrice = manager.GetRestaurantByNumberOfEmployees(minEmployees, maxEmployees);

            return Ok(restaurantsByMenuPrice);
        }

        [HttpPost]
        [Authorize(Policy = "Admin")]
        public async Task<IActionResult> Create([FromBody] RestaurantModel restaurantModel)
        {
            manager.Create(restaurantModel);

            return Ok();
        }

        [HttpPut]
        [Authorize(Policy = "Admin")]
        public async Task<IActionResult> Update([FromBody] RestaurantModel restaurantModel)
        {
            manager.Update(restaurantModel);

            return Ok();
        }

        [HttpDelete("{id}")]
        [Authorize(Policy = "Admin")]
        public async Task<IActionResult> Delete([FromRoute] string id)
        {
            manager.Delete(id);

            return Ok();
        }
    }
}
