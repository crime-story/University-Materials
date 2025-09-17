using AutoMapper;
using EstateApp.Aplication.Commands;
using EstateApp.Domain.Dtos;
using EstateApp.Domain.Models;

namespace EstateApp.API.Profiles
{
    public class EstateImageProfile : Profile
    {
        public EstateImageProfile()
        {
            CreateMap<EstateImage, CreateEstateImageDto>().ReverseMap();
            CreateMap<CreateEstateImageCommand, CreateEstateImageDto>().ReverseMap();
        }
    }
}
