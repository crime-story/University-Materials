namespace API.Entities
{
    public class Product
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public float Weight { get; set; }
        public DateTime DateOfPreparation { get; set; }
        public DateTime DateOfExpiration { get; set; }
        public string Description { get; set; }
        public ICollection<Menu> Menus { get; set; }
        public ICollection<OrderContent> OrderContents { get; set; }
    }
}
