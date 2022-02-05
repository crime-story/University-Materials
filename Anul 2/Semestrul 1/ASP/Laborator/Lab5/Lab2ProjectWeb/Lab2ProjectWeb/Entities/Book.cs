using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2ProjectWeb.Entities
{
    public class Book
    {
        public string Id { get; set; }
        public string Name { get; set; }

        public string AuthorId { get; set; }
        public virtual Author Author { get; set; }
    }
}
