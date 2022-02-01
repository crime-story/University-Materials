using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace proiectASP.Models
{
    public class ProductModel
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public float Weight { get; set; }
        public DateTime DateOfPreparation { get; set; }
        public DateTime DateOfExpiration { get; set; }
        public string Description { get; set; }
    }
}
