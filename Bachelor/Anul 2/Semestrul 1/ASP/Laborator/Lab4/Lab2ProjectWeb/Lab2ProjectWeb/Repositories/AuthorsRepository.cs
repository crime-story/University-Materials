using Lab2ProjectWeb.Entities;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2ProjectWeb.Repositories
{
    public class AuthorsRepository : IAuthorsRepository
    {
        private Lab2ProjectWebContext db;

        public AuthorsRepository(Lab2ProjectWebContext db)
        {
            this.db = db;
        }

        public IQueryable<Author> GetAuthorsIQueryable()
        {
            var authors = db.Authors;

            return authors;
        }

        public IQueryable<Author> GetAuthorsWithBooks()
        {
            var authors = GetAuthorsIQueryable().Include(x => x.Books);

            return authors;
        }

        public IQueryable<Author> GetAuthorsWithBooksAndAddress()
        {
            var authors = GetAuthorsWithBooks().Include(x => x.Address);

            return authors;
        }

        public void Create(Author author)
        {
            db.Authors.Add(author);

            db.SaveChanges();
        }

        public void Update(Author author)
        {
            db.Authors.Update(author);

            db.SaveChanges();
        }

        public void Delete(Author author)
        {
            db.Authors.Remove(author);

            db.SaveChanges();
        }
    }
}
