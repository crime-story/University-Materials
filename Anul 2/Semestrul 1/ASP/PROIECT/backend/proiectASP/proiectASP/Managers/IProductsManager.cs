using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Entities;
using proiectASP.Models;

namespace proiectASP.Managers
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
