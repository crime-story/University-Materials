using EstateApp.Aplication.Abstract;
using EstateApp.Aplication.Commands;
using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Aplication.CommandHandler
{
    public class DeleteEstateCommandHandler : IRequestHandler<DeleteEstateCommand, Estate>
    {
        private readonly IUnitOfWork _unitOfWork;
        public DeleteEstateCommandHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<Estate> Handle(DeleteEstateCommand request, CancellationToken cancellationToken)
        {
            var estate = await _unitOfWork.EstateRepository.GetByIdAsync(request.EstateID);
            if(estate == null)
            {
                return null;
            }
            _unitOfWork.EstateRepository.Delete(estate);
            await _unitOfWork.Save();
            return estate;
        }
    }
}
