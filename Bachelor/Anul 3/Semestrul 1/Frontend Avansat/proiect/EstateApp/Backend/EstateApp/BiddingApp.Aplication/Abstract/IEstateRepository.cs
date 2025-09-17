using EstateApp.Domain.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Aplication.Abstract
{
    public interface IEstateRepository : IGenericRepository<Estate>
    {
        Task<List<Estate>> GetEstatesByUsername(string username);
        Task<Estate> GetEstateWithImagesByID(int id);
        Task<List<Estate>> GetEstatesByPageNumber(int pageNumber, int elements);
    }
}
