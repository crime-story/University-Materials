using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2Web.Entities
{
    public class Author
    {
        public string Id { get; set; }
        public string Name { get; set; }

        public virtual AuthorAddress AuthorAddress { get; set; }
        public virtual ICollection<Book> Books { get; set; }
        public virtual ICollection<AuthorConcurs> AuthorConcurs { get; set; }
   
    }
}
