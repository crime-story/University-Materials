using API.Entities;

namespace API.Managers
{
    public interface ITokenManager
    {
        Task<string> CreateToken(User user);
    }
}
