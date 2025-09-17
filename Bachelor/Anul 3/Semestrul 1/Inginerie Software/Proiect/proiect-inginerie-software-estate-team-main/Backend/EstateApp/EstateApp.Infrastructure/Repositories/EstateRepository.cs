using EstateApp.Aplication.Abstract;
using EstateApp.Domain.Models;
using EstateApp.Infrastructure.Data;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Infrastructure.Repositories
{
    public class EstateRepository : GenericRepository<Estate>, IEstateRepository
    {
        public EstateRepository(EstateAppContext _context) : base(_context) { }

        public Task<List<Estate>> GetEstatesByUsername(string username)
        {
            return _context.Estates.Where(x => x.Username == username).Include(x => x.Images).ToListAsync();
        }

        public Task<Estate> GetEstateWithImagesByID(int id)
        {
            return _context.Estates.Where(x => x.EstateID == id).Include(x => x.Images).FirstOrDefaultAsync();
        }
        public async Task<List<Estate>> GetEstatesByPageNumber(int pageNumber, int elements)
        {
            var products = await _context.Estates.Where(x => x.Status == "Enable").Include(x => x.Images).ToListAsync();
            return products.Skip((pageNumber - 1) * elements).Take(elements).ToList();
        }
    }
}
