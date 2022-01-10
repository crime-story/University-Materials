cmake_minimum_required(VERSION 3.21)
project(untitled C)

set(CMAKE_C_STANDARD 11)

add_executable(untitled main.c)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -pthread")