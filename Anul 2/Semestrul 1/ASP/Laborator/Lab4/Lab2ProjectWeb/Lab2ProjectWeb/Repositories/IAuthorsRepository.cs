using Lab2ProjectWeb.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2ProjectWeb.Repositories
{
    public interface IAuthorsRepository
    {
        IQueryable<Author> GetAuthorsIQueryable();
        IQueryable<Author> GetAuthorsWithBooks();
        IQueryable<Author> GetAuthorsWithBooksAndAddress();
        void Create(Author author);
        void Update(Author author);
        void Delete(Author author);
    }
}
