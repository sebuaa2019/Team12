#!/bin/sh

rosnode kill  ./map_server
rosnode kill  ./move_base
rosnode kill  ./amcl
rosnode kill  ./rviz

roslaunch my_image_transport gmapping.launch
