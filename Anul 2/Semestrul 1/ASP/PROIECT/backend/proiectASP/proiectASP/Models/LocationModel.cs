using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Entities;

namespace proiectASP.Models
{
    public class LocationModel
    {
        public string Id { get; set; }
        public string Country { get; set; }
        public string City { get; set; }
        public string PostalCode { get; set; }
        public string Street { get; set; }
    }
}
