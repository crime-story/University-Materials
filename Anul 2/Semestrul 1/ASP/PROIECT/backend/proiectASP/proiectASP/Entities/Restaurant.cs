﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace proiectASP.Entities
{
    public class Restaurant
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public string LocationId { get; set; }
        public Location Location { get; set; }
        public ICollection<Employee> Employees { get; set; }
        public ICollection<Menu> Menus { get; set; }
    }
}
