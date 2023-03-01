using EstateApp.Aplication.Abstract;
using EstateApp.Aplication.Queries;
using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace EstateApp.Aplication.QueryHandlers
{
    public class GetUsersDetailsQueryHandler : IRequestHandler<GetUsersDetailsQuery, List<UserDetails>>
    {
        private readonly IUnitOfWork _unitOfWork;

        public GetUsersDetailsQueryHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<List<UserDetails>> Handle(GetUsersDetailsQuery request, CancellationToken cancellationToken)
        {
            return await _unitOfWork.UserDetailsRepository.GetAll();
        }

    }
}