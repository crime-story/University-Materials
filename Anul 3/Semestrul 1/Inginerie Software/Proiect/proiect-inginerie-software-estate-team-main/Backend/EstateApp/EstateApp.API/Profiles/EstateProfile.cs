using AutoMapper;
using EstateApp.Aplication.Commands;
using EstateApp.Domain.Dtos;
using EstateApp.Domain.Models;

namespace EstateApp.API.Profiles
{
    public class EstateProfile : Profile
    {
        public EstateProfile()
        {
            CreateMap<Estate, CreateEstateDto>().ReverseMap();
            CreateMap<CreateEstateCommand, CreateEstateDto>().ReverseMap();
        }
    }
}
