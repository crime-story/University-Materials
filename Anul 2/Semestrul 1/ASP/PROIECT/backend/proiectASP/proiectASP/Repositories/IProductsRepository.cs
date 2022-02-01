using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Entities;

namespace proiectASP.Repositories
{
    public interface IProductsRepository
    {
        IQueryable<Product> GetProductsIQueryable();
        void Create(Product product);
        void Update(Product product);
        void Delete(Product product);
    }
}
