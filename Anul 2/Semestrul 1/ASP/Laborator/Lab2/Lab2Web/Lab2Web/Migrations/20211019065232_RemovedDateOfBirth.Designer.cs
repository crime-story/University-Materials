﻿// <auto-generated />
using Lab2Web.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;

namespace Lab2Web.Migrations
{
    [DbContext(typeof(Lab2Context))]
    [Migration("20211019065232_RemovedDateOfBirth")]
    partial class RemovedDateOfBirth
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("ProductVersion", "5.0.11")
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("Lab2Web.Entities.Author", b =>
                {
                    b.Property<string>("Id")
                        .HasColumnType("nvarchar(450)");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.ToTable("Authors");
                });

            modelBuilder.Entity("Lab2Web.Entities.AuthorAddress", b =>
                {
                    b.Property<string>("Id")
                        .HasColumnType("nvarchar(450)");

                    b.Property<string>("AuthorId")
                        .HasColumnType("nvarchar(450)");

                    b.Property<string>("City")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Country")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Street")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.HasIndex("AuthorId")
                        .IsUnique()
                        .HasFilter("[AuthorId] IS NOT NULL");

                    b.ToTable("AuthorAddresses");
                });

            modelBuilder.Entity("Lab2Web.Entities.AuthorConcurs", b =>
                {
                    b.Property<string>("AuthorId")
                        .HasColumnType("nvarchar(450)");

                    b.Property<string>("ConcursId")
                        .HasColumnType("nvarchar(450)");

                    b.HasKey("AuthorId", "ConcursId");

                    b.HasIndex("ConcursId");

                    b.ToTable("AuthorConcurs");
                });

            modelBuilder.Entity("Lab2Web.Entities.Book", b =>
                {
                    b.Property<string>("Id")
                        .HasColumnType("nvarchar(450)");

                    b.Property<string>("AuthorId")
                        .HasColumnType("nvarchar(450)");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.HasIndex("AuthorId");

                    b.ToTable("Books");
                });

            modelBuilder.Entity("Lab2Web.Entities.Concurs", b =>
                {
                    b.Property<string>("Id")
                        .HasColumnType("nvarchar(450)");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.ToTable("Concurs");
                });

            modelBuilder.Entity("Lab2Web.Entities.AuthorAddress", b =>
                {
                    b.HasOne("Lab2Web.Entities.Author", "Author")
                        .WithOne("AuthorAddress")
                        .HasForeignKey("Lab2Web.Entities.AuthorAddress", "AuthorId");

                    b.Navigation("Author");
                });

            modelBuilder.Entity("Lab2Web.Entities.AuthorConcurs", b =>
                {
                    b.HasOne("Lab2Web.Entities.Author", "Author")
                        .WithMany("AuthorConcurs")
                        .HasForeignKey("AuthorId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("Lab2Web.Entities.Concurs", "Concurs")
                        .WithMany("AuthorConcurs")
                        .HasForeignKey("ConcursId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Author");

                    b.Navigation("Concurs");
                });

            modelBuilder.Entity("Lab2Web.Entities.Book", b =>
                {
                    b.HasOne("Lab2Web.Entities.Author", "Author")
                        .WithMany("Books")
                        .HasForeignKey("AuthorId");

                    b.Navigation("Author");
                });

            modelBuilder.Entity("Lab2Web.Entities.Author", b =>
                {
                    b.Navigation("AuthorAddress");

                    b.Navigation("AuthorConcurs");

                    b.Navigation("Books");
                });

            modelBuilder.Entity("Lab2Web.Entities.Concurs", b =>
                {
                    b.Navigation("AuthorConcurs");
                });
#pragma warning restore 612, 618
        }
    }
}
