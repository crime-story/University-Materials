using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace proiectASP.Entities
{
    public class Menu
    {
        public string RestaurantId { get; set; }
        public Restaurant Restaurant { get; set; }
        public string ProductId { get; set; }
        public Product Product { get; set; }
        public float Price { get; set; }
    }
}
