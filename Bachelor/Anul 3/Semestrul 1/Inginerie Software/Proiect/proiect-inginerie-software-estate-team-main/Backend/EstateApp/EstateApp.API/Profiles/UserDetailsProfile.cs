using AutoMapper;
using EstateApp.Aplication.Commands;
using EstateApp.Domain.Dtos;
using EstateApp.Domain.Models;

namespace EstateApp.API.Profiles
{
    public class UserDetailsProfile : Profile
    {
        public UserDetailsProfile()
        {
            CreateMap<UserDetails, CreateUserDetailsDto>().ReverseMap();
            CreateMap<CreateUserDetailsCommand, CreateUserDetailsDto>().ReverseMap();
        }
    }
}
