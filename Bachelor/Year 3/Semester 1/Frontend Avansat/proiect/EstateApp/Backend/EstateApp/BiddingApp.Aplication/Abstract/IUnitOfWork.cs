using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Aplication.Abstract
{
    public interface IUnitOfWork : IDisposable
    {
        public IUserDetailsRepository UserDetailsRepository { get; }
        public IEstateRepository EstateRepository { get; }
        public IEstateImageRepository EstateImageRepository { get; }

        Task Save();
    }
}
