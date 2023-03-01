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
    public class GetUserDetailsByUsernameQueryHandler : IRequestHandler<GetUserDetailsByUsernameQuery, UserDetails>
    {
        private readonly IUnitOfWork _unitOfWork;
        public GetUserDetailsByUsernameQueryHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<UserDetails> Handle(GetUserDetailsByUsernameQuery request, CancellationToken cancellationToken)
        {
            var client = await _unitOfWork.UserDetailsRepository.GetUserDetailsByUsername(request.Username);
            return client;
        }
    }
}