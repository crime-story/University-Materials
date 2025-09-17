using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TourOfHeroes.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class HeroesController : ControllerBase
    {
        public static List<Hero> Heroes = new List<Hero>
        {
            new Hero { Id = 11, Name = "Dr. Nice"},
            new Hero { Id = 12, Name = "Narco"},
            new Hero { Id = 13, Name = "Bombasto"},
            new Hero { Id = 14, Name = "Celeritas"},
            new Hero { Id = 15, Name = "Magneta"},
            new Hero { Id = 16, Name = "RubberMan"},
            new Hero { Id = 17, Name = "Dynama"},
            new Hero { Id = 18, Name = "Dr IQ"},
            new Hero { Id = 19, Name = "Magma"},
            new Hero { Id = 20, Name = "Tornado"},
        };

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById([FromRoute] int id)
        {
            var hero = Heroes.FirstOrDefault(x => x.Id == id);

            return Ok(hero);
        }

        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(Heroes);
        }

        [HttpPost]
        public async Task<IActionResult> Add([FromBody] Hero hero)
        {
            Heroes.Add(hero);

            return Ok(Heroes);
        }

        [HttpPut]
        public async Task<IActionResult> Update([FromBody] Hero hero)
        {
            var heroToUpdate = Heroes.FirstOrDefault(x => x.Id == hero.Id);
            heroToUpdate.Name = hero.Name;

            return Ok(Heroes);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete([FromRoute]int id)
        {
            var heroToRemove = Heroes.FirstOrDefault(x => x.Id == id);
            Heroes.Remove(heroToRemove);

            return Ok(Heroes);
        }
    }
}
