using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2Web.Entities
{
    public class AuthorConcurs
    {
        public string AuthorId { get; set; }
        public Author Author { get; set; }
        public string ConcursId { get; set; }
        public Concurs Concurs { get; set; }

    }
}
