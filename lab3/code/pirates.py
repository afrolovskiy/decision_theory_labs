# -*- coding: utf-8 -*-

import random
import math
import pygame


class Ship(object):

    def draw(self):
        raise UnimplementedMethod()


class Pirate(Ship):
    COLOR = (0, 0, 0)
    SPEED = 32.0

    def __init__(self, point, direction, width=10, height=10):
        self.point = point
        self.direction = direction
        self.width = width
        self.height = height

    def draw(self, screen):
        pygame.draw.rect(
            screen, self.COLOR,  pygame.Rect(int(self.point[0]), int(self.point[1]), self.width, self.height))


class Police(Ship):
    COLOR = (0, 0, 255)
    SPEED = 35.0

    def __init__(self, point, direction, radius=8):
        self.point = point
        self.direction = direction
        self.radius = radius

    def draw(self, screen):
        pygame.draw.circle(screen, self.COLOR, (int(self.point[0]), int(self.point[1])), self.radius)


class Tourist(Ship):
    COLOR = (0, 255, 0)
    SPEED = 28.0

    def __init__(self, point, direction, edge=6):
        self.point = point
        self.direction = direction
        self.edge = edge

    def draw(self, screen):
        height = math.sqrt(self.edge**2 - self.edge**2/4.0)
        pygame.draw.polygon(
            screen, self.COLOR, 
            ((int(self.point[0] - self.edge/2.0), int(self.point[1] - height/3.0)), 
             (int(self.point[0] + self.edge/2.0), int(self.point[1] - height/3.0)), 
             (int(self.point[0]), int(self.point[1] + 2.0*height/3.0))))


class Simulator(object):
    SCREEN_SIZE = (600, 700)
    SCREEN_COLOR = (0, 191, 255)
    PIRATE_PROBABILITY = 0.01
    TOURIST_PROBABILITY = 0.2
    POLICE_TURN_PROBABILITY = 0.04
    TIMEOUT = 40  # 1000 / 25
    POLICES_COUNT = 4
    MAX_PIRATES_COUNT  = 5
    MAX_TOURISTS_COUNT = 6
    PIRATE_OBSERVATION_AREA = 100
    POLICE_OBSERVATION_AREA = 100
    TOURIST_OBSERVATION_AREA = 100
    
    
    def __init__(self):
        self.polices = []
        self.pirates = []
        self.tourists = []
        self.screen = pygame.display.set_mode(self.SCREEN_SIZE)


    def run(self):
        self.initialize()
        self.draw_ships()

        while True:
            self.screen.fill(self.SCREEN_COLOR)
            self.refresh()
            self.draw_ships()
            pygame.time.delay(self.TIMEOUT)
            pygame.display.flip() 

    def initialize(self):
        pygame.init()
        for idx in range(self.POLICES_COUNT):
            self.polices.append(Police(self.generate_coord(), self.generate_direction()))

    def generate_coord(self):
        return (random.randint(0, self.SCREEN_SIZE[0]), 
                   random.randint(0, self.SCREEN_SIZE[1]))

    def generate_direction(self):
        return (round(random.uniform(-1.0, 1.0)), round(random.uniform(-1.0, 1.0)))

    def refresh(self):
        self.refresh_polices()
        self.refresh_pirates()

    def refresh_polices(self):
        for police in self.polices:
            self.refresh_police(police)

    def refresh_police(self, police):
        # TODO
        if police.is_patrolled:
            turn = random.random() < self.POLICE_TURN_PROBABILITY
            if turn:
                police.direction = self.generate_direction()
            while True:
                new_point = self.calc_new_position(police.point, police.direction, police.SPEED)
                if self.check_limits(new_point):
                    police.point = new_point
                    break
                police.direction = self.generate_direction()


    def refresh_pirates(self):
        if len(self.pirates) < self.MAX_PIRATES_COUNT and \
                random.random() < self.PIRATE_PROBABILITY:
            self.add_new_pirate()

        for pirate in self.pirates:
            self.regresh_pirate(pirate)


    def add_new_pirate(self):
        point = (0, random.randint(0, self.SCREEN_SIZE[1]))
        pirate = Pirate(point, self.generate_direction())
        self.pirates.append(pirate)

    def refresh_pirate(self, pirate):
        polices = self.get_nearest_polices(pirate)
        if polices:
            # надо убегать, т.к. по длизости есть полицейские
            pass


    def get_nearest_polices(self, pirate):
        polices = []
        for police in self.polices:
            distance = self.calc_distance(pirate, police):
            if distance < self.PIRATE_OBSERVATION_AREA:
                polices.append(police)
        return polices
            



    def calc_new_position(self, point, direction, speed):
        return (point[0] + direction[0]*speed/25, point[1] + direction[1]*speed/25)

    def check_limits(self, point):
        return point[0] > 0 and point[0] < self.SCREEN_SIZE[0] and \
            point[1] > 0 and point[1] < self.SCREEN_SIZE[1]

    def calc_distance(self, point1, point2):
        return math.sqrt((point1[0] - point2[0])**2 + (point1[1] - point2[1])**2)

    def draw_ships(self):
        for police in self.polices:
            police.draw(self.screen)

        for pirate in self.pirates:
            pirate.draw(self.screen)

        for tourust in self.tourists:
            tourist.draw(self.screen)


Simulator().run()
