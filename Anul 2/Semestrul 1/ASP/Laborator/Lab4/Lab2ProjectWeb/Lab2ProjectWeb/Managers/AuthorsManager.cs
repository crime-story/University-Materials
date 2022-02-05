using Lab2ProjectWeb.Entities;
using Lab2ProjectWeb.Models;
using Lab2ProjectWeb.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
//Managers => Repository
//Managers get data from Repository And manipulate it for our business logic
namespace Lab2ProjectWeb.Managers
{
    public class AuthorsManager : IAuthorsManager
    {
        private readonly IAuthorsRepository authorsRepository;

        public AuthorsManager(IAuthorsRepository authorsRepository)
        {
            this.authorsRepository = authorsRepository;
        }

        public void Create(string name)
        {
            var newAuthor = new Author
            {
                Id = "7",
                Name = name
            };

            authorsRepository.Create(newAuthor);
        }

        public void Create(AuthorModel model)
        {
            var newAuthor = new Author
            {
                Id = model.Id,
                Name = model.Name
            };

            authorsRepository.Create(newAuthor);
        }

        public void Update(AuthorModel model)
        {
            var author = GetAuthorById(model.Id);

            author.Name = model.Name;
            authorsRepository.Update(author);
        }

        public void Delete(string id)
        {
            var author = GetAuthorById(id);

            authorsRepository.Delete(author);
        }

        public Author GetAuthorById(string id)
        {
            var author = authorsRepository.GetAuthorsIQueryable()
                .FirstOrDefault(x => x.Id == id);

            return author;
        }

        public List<Author> GetAuthors()
        {
            return authorsRepository.GetAuthorsIQueryable().ToList();
        }

        public List<Author> GetAuthorsFiltered()
        {
            var authors = authorsRepository.GetAuthorsWithBooksAndAddress();

            var filtered = authors.Where(x => x.Books.Count > 0)
                .ToList();

            return filtered;
        }

        public List<string> GetAuthorsIdsList()
        {
            var authors = authorsRepository.GetAuthorsIQueryable();
            var idList = authors.Select(x => x.Id)
                .ToList();

            return idList;
        }

        public List<Author> GetAuthorsWithBooks()
        {
            var authorsWithBooks = authorsRepository.GetAuthorsWithBooks();

            return authorsWithBooks.ToList();
        }

        public List<AuthorFirstBookModel> GetOrderedAuthors()
        {
            var orderedAuthors = authorsRepository.GetAuthorsWithBooksAndAddress()
                .Where(x => x.Books.Count > 0)
                .Select(x => new AuthorFirstBookModel { Id = x.Id, FirstBookName = x.Books.FirstOrDefault().Name })
                .OrderBy(x => x.FirstBookName)
                .ToList();

            return orderedAuthors;
        }
    }
}
