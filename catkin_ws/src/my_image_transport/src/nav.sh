#!/bin/sh

rosrun map_server map_saver -f /home/chen/catkin_ws/src/my_image_transport/map/map
rosnode kill  ./rviz
rosnode kill  ./slam_gmapping

rosrun my_image_transport image_map 
roslaunch my_image_transport nav.launch
