using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2Web.Entities
{
    public class Book
    {
        public string Id { get; set; }
        public string Name { get; set; }

        public string AuthorId { get; set; }
        public Author Author { get; set; }
    }
}
