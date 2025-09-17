using API.Entities;
using API.Models;

namespace API.Managers
{
    public interface IProductsManager
    {
        List<Product> GetProducts();
        Product GetProductById(string id);
        void Create(ProductModel model);
        void Update(ProductModel model);
        void Delete(string id);
    }
}
