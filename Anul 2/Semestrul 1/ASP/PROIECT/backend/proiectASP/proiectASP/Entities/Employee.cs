using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace proiectASP.Entities
{
    public class Employee
    {
        public string Id { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public string PhoneNumber { get; set; }
        public int YearsOfExperience { get; set; }
        public DateTime HireDateTime { get; set; }
        public DateTime DateOfBirth { get; set; }
        public string RestaurantId { get; set; }
        public Restaurant Restaurant { get; set; }
    }
}
