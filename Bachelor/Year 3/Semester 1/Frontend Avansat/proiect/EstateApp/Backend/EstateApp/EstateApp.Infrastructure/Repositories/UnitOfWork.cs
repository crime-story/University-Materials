using EstateApp.Aplication.Abstract;
using EstateApp.Infrastructure.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Infrastructure.Repositories
{
    public class UnitOfWork : IUnitOfWork
    {
        private readonly EstateAppContext _context;

        public UnitOfWork(EstateAppContext context, IUserDetailsRepository userDetailsRepository, 
            IEstateRepository estateRepository, IEstateImageRepository estateImageRepository)
        {
            _context = context;
            UserDetailsRepository = userDetailsRepository;
            EstateRepository = estateRepository;
            EstateImageRepository = estateImageRepository;
        }

        public IUserDetailsRepository UserDetailsRepository { get; private set; }
        public IEstateRepository EstateRepository { get; private set; }
        public IEstateImageRepository EstateImageRepository { get; private set; }
        public void Dispose()
        {
            _context.Dispose();
        }

        public async Task Save()
        {
            await _context.SaveChangesAsync();
        }
    }
}
