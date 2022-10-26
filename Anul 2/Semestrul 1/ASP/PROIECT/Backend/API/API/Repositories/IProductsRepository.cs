using API.Entities;

namespace API.Repositories
{
    public interface IProductsRepository
    {
        IQueryable<Product> GetProductsIQueryable();
        void Create(Product product);
        void Update(Product product);
        void Delete(Product product);
    }
}
