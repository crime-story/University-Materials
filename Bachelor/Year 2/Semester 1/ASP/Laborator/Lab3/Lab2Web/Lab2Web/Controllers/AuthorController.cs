using Lab2Web.Entities;
using Lab2Web.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2Web.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthorController : ControllerBase
    {
        [HttpGet]
        public async Task<IActionResult> GetAuthors()
        {
            var db = new Lab2Context();

            var authors = db.Authors.ToList();

            return Ok(authors);
            /* select * from Authors*/
        }

        [HttpGet("select")]
        //Eager Loading 
        public async Task<IActionResult> GetAuthorsIdsEager()
        {
            var db = new Lab2Context();

            var authorsIds = db.Authors.Select(x => x.Id).ToList();

            var authorCopy1 = authorsIds;
            var authorCopy2 = authorsIds;
            var authorCopy3 = authorsIds;

            return Ok(authorsIds);
            /*select id from Authors*/
        }

        [HttpGet("select-lazy")]
        //Lazy Loading 
        //Install Microsoft.EntityFrameworkCore.Proxies
        public async Task<IActionResult> GetAuthorsIdsLazy()
        {
            var db = new Lab2Context();

            var authorsIds = db.Authors.Select(x => x.Id).AsQueryable();

            return Ok(authorsIds);
            /*select id from Authors*/
            //it will query the db only on return because that is the place we actually need the data
        }

        [HttpGet("lazy-join")]
        //Lazy Loading 
        //Add in connection string :     ;MultipleActiveResultSets=true
        public async Task<IActionResult> JoinLazy()
        {
            var db = new Lab2Context();

            var authors = db.Authors.AsQueryable();

            foreach(var x in authors)
            {
                var y = x.Books;
            }
            //check console, it will query the database every time x.Books is called

            return Ok(authors);
        }

        [HttpGet("eager-join")]
        //Eager Loading
        //Comment or Uncomment from OnConfiguring in Context  .UseLazyLoadingProxies()
        public async Task<IActionResult> JoinEager()
        {
            var db = new Lab2Context();

            var authors = db.Authors
                .Include(x => x.Books)
                .Include(x => x.AuthorAddress)
                .ToList();

            foreach (var x in authors)
            {
                var y = x.Books;
            }
            //Check console , it will only query the db once 

            return Ok(authors);
        }

        [HttpGet("filter")]
        //Eager Loading
        //Comment or Uncomment from OnConfiguring in Context  .UseLazyLoadingProxies() 
        public async Task<IActionResult> Filter()
        {
            var db = new Lab2Context();

            var authors = db.Authors
                .Include(x => x.Books)
                .Include(x => x.AuthorAddress)
                .Where(x => x.Books.Count > 0)
                .Select(x => new { Id = x.Id, FirstBookName = x.Books.FirstOrDefault().Name})
                .ToList();

            return Ok(authors);
        }

        [HttpGet("orderby")]
        public async Task<IActionResult> OrderBy()
        {
            var db = new Lab2Context();

            var authors = db.Authors
                .Include(x => x.Books)
                .Include(x => x.AuthorAddress)
                .Where(x => x.Books.Count > 0)
                .Select(x => new { Id = x.Id, FirstBookName = x.Books.FirstOrDefault().Name })
                .ToList()
                .OrderByDescending(x => x.FirstBookName) //Descending OR OrderBy => Ascending
                .ToList();

            return Ok(authors);
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody] string name)
        {
            var db = new Lab2Context();

            var newAuthor = new Author
            {
                Id = "5",
                Name = name
            };

           // db.Add(newAuthor);
            await db.Authors.AddAsync(newAuthor);

            await db.SaveChangesAsync();

            return Ok();
        }

        [HttpPost("withObj")]
        public async Task<IActionResult> Create([FromBody] AuthorModel authorCreationModel)
        {
            var db = new Lab2Context();

            var newAuthor = new Author
            {
                Id = authorCreationModel.Id,
                Name = authorCreationModel.Name
            };

            // db.Add(newAuthor);
            await db.Authors.AddAsync(newAuthor);

            await db.SaveChangesAsync();

            return Ok();
        }

        [HttpPut("withObj")]
        public async Task<IActionResult> Update([FromBody] AuthorModel authorCreationModel)
        {
            var db = new Lab2Context();

            var author = db.Authors.FirstOrDefault(x => x.Id == authorCreationModel.Id);

            author.Name = authorCreationModel.Name;

            db.Authors.Update(author);

            await db.SaveChangesAsync();

            return Ok();
        }
    }
}
