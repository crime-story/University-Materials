namespace API.Entities
{
    public class Driver
    {
        public string Id { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public string PhoneNumber { get; set; }
        public float YearsOfExperience { get; set; }
        public DateTime HireDateTime { get; set; }
        public DateTime DateOfBirth { get; set; }
        public ICollection<Order> Orders { get; set; }
        public ICollection<Vehicle> Vehicles { get; set; }
    }
}
