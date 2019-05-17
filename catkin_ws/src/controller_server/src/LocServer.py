#!/usr/bin/env python

import rospy
import socket
from geometry_msgs.msg import PoseWithCovarianceStamped

class LocServer:
    def __init__(self, host, port):
        self.soc = socket()
        self.soc.bind((host, port))
        self.con = None

    def sendtoclient(self, msg):
        pos_x = ('%.3f' % msg.pose.pose.position.x)
        pos_y = ('%.3f' % msg.pose.pose.position.y)
        try:
            self.con.send(str(pos_x) + ',' + str(pos_y))
        except:
            self.con.close()

    def start(self, threadName):
        print("LocServer Online!")
        self.con.listen(5)
        self.con, addr = self.soc.accept()
        rospy.Subscriber('amcl_pose', PoseWithCovarianceStamped, sendtoclient)
        rospy.spin()