using EstateApp.Domain.Models;
using EstateApp.Infrastructure.Configuartions;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Infrastructure.Data
{
    public class EstateAppContext : IdentityDbContext<User>
    {
        public EstateAppContext(DbContextOptions<EstateAppContext> options) : base(options) { }
        public DbSet<UserDetails> UserDetails { get; set; }
        public DbSet<Location> Locations { get; set; }
        public DbSet<Estate> Estates { get; set; }
        public DbSet<EstateImage> EstateImages { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionBuilder)
        {
            optionBuilder.UseSqlServer(@"Data Source=(localdb)\local;Initial Catalog=EstateAppDatabase;Integrated Security=True");

            //optionBuilder.UseSqlServer(@"Data Source=(localdb)\MSSQLLocalDB;Initial Catalog=EstateAppDb;Integrated Security=True;Connect Timeout=30;Encrypt=False;TrustServerCertificate=False;ApplicationIntent=ReadWrite;MultiSubnetFailover=False");
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.ApplyConfiguration(new EstateConfiguration());
            modelBuilder.ApplyConfiguration(new EstateImageConfiguration());
        }
    }
}
