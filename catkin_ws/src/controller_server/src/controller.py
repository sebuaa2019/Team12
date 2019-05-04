#!/usr/bin/env python

from geometry_msgs.msg import Twist
from controller_server.msg import control_signal
import rospy
import thread

SIGNAL_TO_X = {'FORWARD':1, 'BACKWARD':-1, 'TURNLEFT':0, 'TURNRIGHT':0}
SIGNAL_TO_Z = {'FORWARD':0, 'BACKWARD':0, 'TURNLEFT':1, 'TURNRIGHT':-1}
SPEED_X = 0.2
SPEED_Z = 0.1

global twist
twist = Twist()

def callback(msg):
    global twist
    if (msg.signal in SIGNAL_TO_X.keys()):
        twist.linear.x = SIGNAL_TO_X[msg.signal] * SPEED_X
        twist.angular.z = SIGNAL_TO_Z[msg.signal] * SPEED_Z
    else:
        twist.linear.x = 0.0
        twist.angular.z = 0.0

def pub_vel(thread_name):
    global twist
    rate = rospy.Rate(5)
    pub = rospy.Publisher('/cmd_vel', Twist, queue_size=1)
    while (!rospy.is_shutdown):
        pub.publish(twist)
        rate.sleep()

if __name__ == '__main__':
    rospy.init_node('controller')
    rospy.Subscriber('control_signal', control_signal, callback)
    thread.start_new_thread(pub_vel, ("pub_vel_thread",))
    rospy.spin()