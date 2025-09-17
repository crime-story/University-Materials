using Lab2ProjectWeb.Entities;
using Lab2ProjectWeb.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2ProjectWeb.Managers
{
    public interface IAuthorsManager
    {
        List<Author> GetAuthors();
        List<string> GetAuthorsIdsList();
        List<Author> GetAuthorsWithBooks();
        List<Author> GetAuthorsFiltered();
        List<AuthorFirstBookModel> GetOrderedAuthors();
        Author GetAuthorById(string id);
        void Create(string name);
        void Create(AuthorModel model);
        void Update(AuthorModel model);
        void Delete(string id);
    }
}
