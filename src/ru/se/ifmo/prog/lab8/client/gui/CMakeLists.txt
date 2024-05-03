cmake_minimum_required(VERSION 3.1.0)

project(ProgLab8ClientGUI VERSION 1.0.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY "../")
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_AUTOUIC ON)

if(CMAKE_VERSION VERSION_LESS "3.7.0")
    set(CMAKE_INCLUDE_CURRENT_DIR ON)
endif()

find_package(Qt5 REQUIRED
        Core
        Gui
        Widgets)
find_package(JNI)
set (SOURCE main.cpp)

include_directories(jni)

add_library(connectionWindow STATIC windows/connectionWindow.h)

add_library(mainPage STATIC windows/mainPage.h)

add_library(dragonField STATIC windows/dragonField.h)

add_library(commandWindow STATIC windows/commandWindow.h)

add_executable(main main.cpp windows/connectionWindow.cpp windows/mainPage.cpp windows/dragonField.cpp windows/commandWindow.cpp)

#add_executable(main main.cpp windows/connectionWindow.cpp)

add_subdirectory(jni)

target_link_libraries(main Qt5::Widgets)
target_link_libraries(main Qt5::Gui)
target_link_libraries(main JNI::JNI)
target_link_libraries(main JNI::JVM)
target_link_libraries(main jnicode)
