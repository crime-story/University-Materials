using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Entities;

namespace proiectASP.Models
{
    public class RestaurantModel
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public string LocationId { get; set; }
        public Location Location { get; set; }
    }
}
