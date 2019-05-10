#!/usr/bin/env python

import rospy
import actionlib
from move_base_msgs.msg import MoveBaseAction, MoveBaseGoal

if __name__ == '__main__':
    rospy.init_node('goal')
    move_base = actionlib.SimpleActionClient('move_base', MoveBaseAction)
    move_base.wait_for_server(rospy.Duration(60))
    print ('Connected to move_base server')
    goal = MoveBaseGoal()
    goal.target_pose.header.frame_id = 'base_footprint'
    goal.target_pose.header.stamp = rospy.Time.now()

    goal.target_pose.pose.position.x = 6.0
    goal.target_pose.pose.position.y = -1.0
    goal.target_pose.pose.orientation.w = 0.5
    print ('Sending Goal')
    move_base.send_goal(goal)
    
    move_base.wait_for_result()
