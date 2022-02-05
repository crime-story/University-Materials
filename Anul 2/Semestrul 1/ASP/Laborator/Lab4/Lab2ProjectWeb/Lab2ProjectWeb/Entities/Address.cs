using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2ProjectWeb.Entities
{
    public class Address
    {
        public string Id { get; set; }
        public string City { get; set; }
        public string Country { get; set; }
        public string Street { get; set; }

        public string AuthorId { get; set; }
        public virtual Author Author { get; set; }
    }
}
