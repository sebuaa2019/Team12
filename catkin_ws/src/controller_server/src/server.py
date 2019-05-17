#!/usr/bin/env python

import socket
import rospy
import thread
import re
from SpeedInfoServer import SpeedInfoServer
from NavServer import NavServer
from controller_server.msg import control_signal
from LocServer import LocServer

HOST = '127.0.0.1'
CONTRO_PORT = 1989
INFO_PORT = 2000
LOC_REF_PORT = 2001
SIGNAL = ['FORWARD', 'BACKWARD', 'TURNLEFT', 'TURNRIGHT']
STOPMOVE = 'STOPMOVE'
STARTNAV = 'STARTNAV'

if __name__ == '__main__':
    rospy.init_node('server')
    pub = rospy.Publisher('control_signal', control_signal, queue_size=1)
    soc = socket.socket()
    soc.bind((HOST, CONTRO_PORT))
    thread.start_new_thread(SpeedInfoServer(HOST, INFO_PORT).start, ('INFO-Server-Thread', ))
    thread.start_new_thread(LocServer(HOST, LOC_REF_PORT).start, ('LOC-Server-Thread', ))
    navServer = NavServer()
    print('Server Online!')
    soc.listen(5)
    while (True):
        conn, addr = soc.accept()
        data = conn.recv(1024)
        print(data)
        if (data in SIGNAL):
            pub.publish(control_signal(data, False))
        elif (data == STOPMOVE):
            pub.publish(control_signal(data, True))
        elif (',' in data):
            pos = filter(lambda x : x, re.split('[^\w.-]', data))
            pos_X = float(pos[0])
            pos_Y = float(pos[1])
            ori_W = float(pos[2])
            print(pos_X)
            print(pos_Y)
            print(ori_W)
            navServer.append_waypoint(pos_X, pos_Y, ori_W)
        elif (data == STARTNAV):
            navServer.start()
