﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2ProjectWeb.Entities
{
    public class AuthorConcurs
    {
        public string AuthorId { get; set; }
        public string ConcursId { get; set; }

        public virtual Author Author { get; set; }
        public virtual Concurs Concurs { get; set; }
    }
}
