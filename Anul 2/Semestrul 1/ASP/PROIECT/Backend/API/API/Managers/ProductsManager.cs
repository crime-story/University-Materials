using API.Entities;
using API.Models;
using API.Repositories;

namespace API.Managers
{
    public class ProductsManager : IProductsManager
    {
        private readonly IProductsRepository productsRepository;

        public ProductsManager(IProductsRepository productsRepository)
        {
            this.productsRepository = productsRepository;
        }

        public List<Product> GetProducts()
        {
            return productsRepository.GetProductsIQueryable().ToList();
        }

        public Product GetProductById(string id)
        {
            var product = productsRepository.GetProductsIQueryable()
                .FirstOrDefault(x => x.Id == id);

            return product;
        }

        public void Create(ProductModel model)
        {
            var newProduct = new Product
            {
                Id = model.Id,
                Name = model.Name,
                Weight = model.Weight,
                DateOfExpiration = model.DateOfExpiration,
                DateOfPreparation = model.DateOfPreparation,
                Description = model.Description
            };

            productsRepository.Create(newProduct);
        }

        public void Update(ProductModel model)
        {
            var product = GetProductById(model.Id);

            product.Name = model.Name;
            product.Weight = model.Weight;
            product.DateOfPreparation = model.DateOfPreparation;
            product.DateOfExpiration = model.DateOfExpiration;
            product.Description = model.Description;

            productsRepository.Update(product);
        }

        public void Delete(string id)
        {
            var product = GetProductById(id);

            productsRepository.Delete(product);
        }

    }
}
