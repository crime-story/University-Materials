using Lab2ProjectWeb.Entities;
using Lab2ProjectWeb.Managers;
using Lab2ProjectWeb.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2ProjectWeb.Controllers
{
    //Controller => Manager  => Repository
    [Route("api/[controller]")]
    [ApiController]
    public class AuthorController : ControllerBase
    {
        private readonly IAuthorsManager manager;

        public AuthorController(IAuthorsManager authorsManager)
        {
            this.manager = authorsManager;
        }

        [HttpGet]
        public async Task<IActionResult> GetAuthors()
        {
            var authors = manager.GetAuthors();

            return Ok(authors);
        }

        //eager-loading
        [HttpGet("select-id")]
        public async Task<IActionResult> GetIds()
        {
            var idList = manager.GetAuthorsIdsList();

            return Ok(idList);
        }

        [HttpGet("join-eager")]
        public async Task<IActionResult> JoinEager()
        {
            var authorsWithBooks = manager.GetAuthorsWithBooks();

            return Ok(authorsWithBooks);
        }

        [HttpGet("filter")]
        public async Task<IActionResult> Filtered()
        {
            var authorsFiltered = manager.GetAuthorsFiltered();

            return Ok(authorsFiltered);
        }

        [HttpGet("order-by-asc")]
        public async Task<IActionResult> OrderByAsc()
        {
            var orderedAuthors = manager.GetOrderedAuthors();

            return Ok(orderedAuthors);
        }

        [HttpGet("byId/{id}")]
        public async Task<IActionResult> GetById([FromRoute] string id)
        {
            var author = manager.GetAuthorById(id);

            return Ok(author);
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody] string name)
        {
            manager.Create(name);

            return Ok();
        }

        [HttpPost("withobj")]
        public async Task<IActionResult> Create([FromBody] AuthorModel authorModel)
        {
            manager.Create(authorModel);

            return Ok();
        }

        [HttpPut]
        public async Task<IActionResult> Update([FromBody] AuthorModel authorModel)
        {
            manager.Update(authorModel);

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
