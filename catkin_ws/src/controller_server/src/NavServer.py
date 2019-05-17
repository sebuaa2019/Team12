#!/usr/bin/env python

import rospy
from move_base_msgs.msg import MoveBaseAction, MoveBaseGoal
import actionlib

class NavServer:
    def __init__(self):
        self.waypoint_list = []
        self.client = actionlib.SimpleActionClient('move_base', MoveBaseAction)

    def __wrap_waypoint(self, pos_X, pos_Y, ori_W):
        waypoint = MoveBaseGoal()
        waypoint.target_pose.header.frame_id = 'map'
        waypoint.target_pose.pose.position.x = pos_X
        waypoint.target_pose.pose.position.y = pos_Y
        waypoint.target_pose.pose.orientation.w = ori_W
        return waypoint

    def append_waypoint(self, pos_X, pos_Y, ori_W):
        waypoint = self.__wrap_waypoint(pos_X, pos_Y, ori_W)
        self.waypoint_list.append(waypoint)

    def start(self):
        print('Start Navigation')
        while (self.client.wait_for_server(rospy.Duration(5.0)) == False):
            print('Waiting for move_base server to come up')
            return
        for waypoint in self.waypoint_list:
            pos_X = waypoint.target_pose.pose.position.x
            pos_Y = waypoint.target_pose.pose.position.y
            print('Go to Waypoint ( %f, %f )'%(pos_X, pos_Y))
            self.client.send_goal(waypoint)
            self.client.wait_for_result()
            if (self.client.get_state() == actionlib.GoalStatus.SUCCEEDED):
                print('Arrived at Waypoint ( %f, %f )'%(pos_X, pos_Y))
            else:
                print('Failed to get to Waypoint ( %f, %f )'%(pos_X, pos_Y))