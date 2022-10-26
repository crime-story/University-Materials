using API.Entities;

namespace API.Repositories
{
    public class ProductsRepository : IProductsRepository
    {
        private readonly AppDbContext db;

        public ProductsRepository(AppDbContext db)
        {
            this.db = db;
        }
        public IQueryable<Product> GetProductsIQueryable()
        {
            var products = db.Products;

            return products;
        }

        public void Create(Product product)
        {
            db.Products.Add(product);

            db.SaveChanges();
        }

        public void Update(Product product)
        {
            db.Products.Update(product);

            db.SaveChanges();
        }

        public void Delete(Product product)
        {
            db.Products.Remove(product);

            db.SaveChanges();
        }
    }
}
