using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2Web.Entities
{
    public class AuthorAddress
    {
        public string Id { get; set; }
        public string AuthorId { get; set; }
        public string City { get; set; }
        public string Country { get; set; }
        public string Street { get; set; }

        public Author Author { get; set; }
    }
}
