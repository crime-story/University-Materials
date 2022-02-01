using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using proiectASP.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Managers;
using proiectASP.Models;

namespace proiectASP.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LocationController : ControllerBase
    {
        private readonly ILocationsManager manager;

        public LocationController(ILocationsManager locationsManager)
        {
            this.manager = locationsManager;
        }

        [HttpGet("getAll")] 
        public async Task<IActionResult> GetLocations()
        {
            var locations = manager.GetLocations();

            return Ok(locations);
        }

        [HttpGet("getIds")]
        public async Task<IActionResult> GetIds()
        {
            var idList = manager.GetLocationsIdsList();

            return Ok(idList);
        }

        [HttpGet("GetWithRestaurants")]
        public async Task<IActionResult> GetLocationsWithRestaurants()
        {
            var locationsWithRestaurants = manager.GetLocationsWithRestaurants();

            return Ok(locationsWithRestaurants);
        }

        
        [HttpGet("byCity/{city}")]
        public async Task<IActionResult> GetLocationsByCity([FromRoute] string city)
        {
            var locationsByCity = manager.GetLocationByCity(city);

            return Ok(locationsByCity);
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody] LocationModel locationModel)
        {
            manager.Create(locationModel);

            return Ok();
        }

        [HttpPut]
        public async Task<IActionResult> Update([FromBody] LocationModel locationModel)
        {
            manager.Update(locationModel);

            return Ok();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete([FromRoute] string id)
        {
            manager.Delete(id);

            return Ok();
        }
    }
}
