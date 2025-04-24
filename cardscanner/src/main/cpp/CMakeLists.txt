cmake_minimum_required(VERSION 3.4.1)

add_library(yuv-decoder SHARED yuv-decoder.c)

find_library(log-lib log)

target_link_libraries(yuv-decoder ${log-lib} GLESv2 jnigraphics)