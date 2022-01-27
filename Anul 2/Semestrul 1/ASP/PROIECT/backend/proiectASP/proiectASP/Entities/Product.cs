using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace proiectASP.Entities
{
    public class Product
    {
        public string Id { get; set; }
        public string Weight { get; set; }
        public DateTime DateOfPreparation { get; set; }
        public DateTime DateOfExpiration { get; set; }
        public string Description { get; set; }
        public ICollection<Menu> Menus { get; set; }
    }
}
