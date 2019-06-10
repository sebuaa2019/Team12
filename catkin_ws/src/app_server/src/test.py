#!/usr/bin/env python

import socket

if __name__ == "__main__":
    soc = socket.socket()
    HOST = '127.0.0.1'
    PORT = 1989
    soc.connect((HOST, PORT))
    #soc.send('(0,0,1)')
    soc.send('STARTNAV')