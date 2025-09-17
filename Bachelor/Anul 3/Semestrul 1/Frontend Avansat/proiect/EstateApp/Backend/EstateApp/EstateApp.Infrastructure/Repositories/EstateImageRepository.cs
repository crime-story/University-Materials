using EstateApp.Aplication.Abstract;
using EstateApp.Domain.Models;
using EstateApp.Infrastructure.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Infrastructure.Repositories
{
    public class EstateImageRepository : GenericRepository<EstateImage>, IEstateImageRepository
    {
        public EstateImageRepository(EstateAppContext _context) : base(_context) { }
    }
}
