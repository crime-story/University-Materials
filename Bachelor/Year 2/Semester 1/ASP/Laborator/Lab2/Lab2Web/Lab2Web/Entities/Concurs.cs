using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2Web.Entities
{
    public class Concurs
    {
        public string Id { get; set; }
        public string Name { get; set; }

        public ICollection<AuthorConcurs> AuthorConcurs { get; set; }
    }
}
