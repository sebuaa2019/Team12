# Install script for directory: /home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/src/wpb_home/wpb_home_tutorials

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/install")
endif()
string(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
if(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  if(BUILD_TYPE)
    string(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  else()
    set(CMAKE_INSTALL_CONFIG_NAME "")
  endif()
  message(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
endif()

# Set the component getting installed.
if(NOT CMAKE_INSTALL_COMPONENT)
  if(COMPONENT)
    message(STATUS "Install component: \"${COMPONENT}\"")
    set(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  else()
    set(CMAKE_INSTALL_COMPONENT)
  endif()
endif()

# Install shared libraries without execute permission?
if(NOT DEFINED CMAKE_INSTALL_SO_NO_EXE)
  set(CMAKE_INSTALL_SO_NO_EXE "1")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/wpb_home_tutorials/srv" TYPE FILE FILES "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/src/wpb_home/wpb_home_tutorials/srv/Follow.srv")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/wpb_home_tutorials/cmake" TYPE FILE FILES "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/build/wpb_home/wpb_home_tutorials/catkin_generated/installspace/wpb_home_tutorials-msg-paths.cmake")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include" TYPE DIRECTORY FILES "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/devel/include/wpb_home_tutorials")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/roseus/ros" TYPE DIRECTORY FILES "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/devel/share/roseus/ros/wpb_home_tutorials")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/common-lisp/ros" TYPE DIRECTORY FILES "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/devel/share/common-lisp/ros/wpb_home_tutorials")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/gennodejs/ros" TYPE DIRECTORY FILES "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/devel/share/gennodejs/ros/wpb_home_tutorials")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  execute_process(COMMAND "/usr/bin/python" -m compileall "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/devel/lib/python2.7/dist-packages/wpb_home_tutorials")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib/python2.7/dist-packages" TYPE DIRECTORY FILES "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/devel/lib/python2.7/dist-packages/wpb_home_tutorials")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib/pkgconfig" TYPE FILE FILES "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/build/wpb_home/wpb_home_tutorials/catkin_generated/installspace/wpb_home_tutorials.pc")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/wpb_home_tutorials/cmake" TYPE FILE FILES "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/build/wpb_home/wpb_home_tutorials/catkin_generated/installspace/wpb_home_tutorials-msg-extras.cmake")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/wpb_home_tutorials/cmake" TYPE FILE FILES
    "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/build/wpb_home/wpb_home_tutorials/catkin_generated/installspace/wpb_home_tutorialsConfig.cmake"
    "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/build/wpb_home/wpb_home_tutorials/catkin_generated/installspace/wpb_home_tutorialsConfig-version.cmake"
    )
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/wpb_home_tutorials" TYPE FILE FILES "/home/nipo/AndroidStudioProjects/RobotApp/catkin_ws/src/wpb_home/wpb_home_tutorials/package.xml")
endif()

