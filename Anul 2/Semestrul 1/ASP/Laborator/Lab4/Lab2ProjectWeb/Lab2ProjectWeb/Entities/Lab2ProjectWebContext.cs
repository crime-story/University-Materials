using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2ProjectWeb.Entities
{
    //Ctrl + . For error fixes suggestions

    //Installed Nuget Packages
    /*   
      EntityFrameworkCore, care este ORM-ul in sine 
      EntityFrameworkCore.Relational 
      EntityFrameworkCore.SqlServer, care este provider-ul spre SqlServer(baza de date pe care o vom folosi)
      EntityFrameworkCore.Tools
    */

    //1.Add-Migration NameMigration
    //2.Verificati migration-ul 
    //3. Daca totul e ok, => Update-Database
    public class Lab2ProjectWebContext : DbContext
    {
        public Lab2ProjectWebContext(DbContextOptions<Lab2ProjectWebContext> options) : base(options) { }
        public DbSet<Author> Authors { get; set; }
        public DbSet<Address> Addresses { get; set; }
        public DbSet<Book> Books { get; set; }
        public DbSet<Concurs> Concursuri { get; set; }
        public DbSet<AuthorConcurs> AuthorConcursuri { get; set; }


        /*protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder
                //Comment/Uncomment when not using/using lazy loading
                //.UseLazyLoadingProxies() 
                .UseLoggerFactory(LoggerFactory.Create(builder => builder.AddConsole()))
                .UseSqlServer(@"Server=(localdb)\MSSQLLocalDB;Initial Catalog=Lab2ProjectWeb;Integrated Security=True;Connect Timeout=30;Encrypt=False;TrustServerCertificate=False;ApplicationIntent=ReadWrite;MultiSubnetFailover=False;MultipleActiveResultSets=true");
        }*/

        protected override void OnModelCreating(ModelBuilder builder)
        {
            //One to One
            builder.Entity<Author>()
                .HasOne(author => author.Address)
                .WithOne(address => address.Author);

            //One to Many
            builder.Entity<Author>()
                .HasMany(a => a.Books)
                .WithOne(b => b.Author);

            //Many To Many
            builder.Entity<AuthorConcurs>().HasKey(ac => new { ac.AuthorId, ac.ConcursId });

            builder.Entity<AuthorConcurs>()
                .HasOne(ac => ac.Author)
                .WithMany(a => a.AuthorConcursuri)
                .HasForeignKey(ac => ac.AuthorId);

            builder.Entity<AuthorConcurs>()
                .HasOne(ac => ac.Concurs)
                .WithMany(c => c.AuthorConcursuri)
                .HasForeignKey(ac => ac.ConcursId);
        }
    }
}
