cmake_minimum_required(VERSION 3.1.0)

project(jnicode VERSION 1.0.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_AUTOUIC ON)

if(CMAKE_VERSION VERSION_LESS "3.7.0")
    set(CMAKE_INCLUDE_CURRENT_DIR ON)
endif()

find_package(JNI REQUIRED)

# Define the library target
add_library(jnicode STATIC jvmConnector.cpp)

# Link the JNI libraries to the target
target_link_libraries(jnicode PUBLIC ${JNI_LIBRARIES})

# Include JNI headers
target_include_directories(jnicode PUBLIC ${JNI_INCLUDE_DIRS})
