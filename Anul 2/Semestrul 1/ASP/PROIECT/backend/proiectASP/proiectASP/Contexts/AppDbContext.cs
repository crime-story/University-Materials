using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using proiectASP.Entities;

namespace proiectASP.Contexts
{
    public class AppDbContext : DbContext
    {
        // Relation 1 - 1 for Location <-> Restaurant
        public DbSet<Location> Locations { get; set; }
        public DbSet<Restaurant> Restaurants { get; set; }
        public DbSet<Employee> Employees { get; set; }
        public DbSet<Product> Products { get; set; }
        public DbSet<Menu> Menus { get; set; }
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        // configuration before Dependency Injection
        /*
        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder
                .UseLoggerFactory(LoggerFactory.Create(builder => builder.AddConsole()))
                .UseSqlServer(
                    "Data Source = localhost\\SQLEXPRESS; Initial Catalog = bazaDate; Integrated Security = SSPI; MultipleActiveResultSets = True");
        }
        */
        protected override void OnModelCreating(ModelBuilder builder)
        {
            builder.Entity<Location>()
                .HasOne(a => a.Restaurant)
                .WithOne(aa => aa.Location);

            builder.Entity<Restaurant>()
                .HasMany(a => a.Employees)
                .WithOne(b => b.Restaurant);

            builder.Entity<Menu>().HasKey(ac => new {ac.RestaurantId, ac.ProductId});

            builder.Entity<Menu>()
                .HasOne(ac => ac.Restaurant)
                .WithMany(a => a.Menus)
                .HasForeignKey(ac => ac.RestaurantId);

            builder.Entity<Menu>()
                .HasOne(ac => ac.Product)
                .WithMany(a => a.Menus)
                .HasForeignKey(ac => ac.ProductId);
        }
    }
}
