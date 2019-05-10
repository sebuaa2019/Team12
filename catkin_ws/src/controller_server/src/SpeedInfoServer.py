#!/usr/bin/env python

import rospy
import socket
from geometry_msgs.msg import Twist

class SpeedInfoServer:
    def __init__(self, host, port):
        self.soc = socket.socket()
        self.soc.bind((host, port))

    def sendtoclient(self, msg):
        x = msg.linear.x
        z = msg.angular.z
        if (x > 0 or z > 0):
            try:
                self.soc.send('( ' + str(x) + ' , ' + str(z) + ' )')
            except:
                print('Connect Failed')

    def start(self, threadname):
        print('Info-Server Online!')
        rospy.Subscriber('/cmd_vel', Twist, self.sendtoclient)
        rospy.spin()