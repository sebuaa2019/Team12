#!/usr/bin/env python

import socket
import rospy
import thread
import re
from controller_server.msg import control_signal
from NavServer import NavServer

SIGNAL = ['FORWARD', 'BACKWARD', 'TURNLEFT', 'TURNRIGHT']
STOPMOVE = 'STOPMOVE'
STARTNAV = 'STARTNAV'

if __name__ == '__main__':
    #HOST = rospy.get_param('HOST')
    #Server_Port = rospy.get_param('Server_Port')
    rospy.init_node('main_server')
    pub = rospy.Publisher('control_signal', control_signal, queue_size=1)
    soc = socket.socket()
    #soc.bind((HOST, Server_Port))
    soc.bind(('127.0.0.1', 1989))
    navServer = NavServer()
    print('Main-Server Online!')
    soc.listen(5)
    while (True):
        conn, addr = soc.accept()
        print("Main-Server >> Connection From : " + addr[0])
        data = conn.recv(1024)
        if (data in SIGNAL):
            pub.publish(control_signal(data, False))
        elif (data == STOPMOVE):
            pub.publish(control_signal(data, True))
        elif (',' in data):
            pos = filter(lambda x : x, re.split('[^\w.-]', data))
            pos_X = float(pos[0])
            pos_Y = float(pos[1])
            ori_W = float(pos[2])
            navServer.append_waypoint(pos_X, pos_Y, ori_W)
        elif (data == STARTNAV):
            navServer.start(conn)
