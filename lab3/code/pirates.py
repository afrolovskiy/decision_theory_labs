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

    def __init__(self, point, end_point, direction, edge=8):
        self.point = point
        self.end_point = end_point
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
    PIRATE_TURN_PROBABILITY = 0.04
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
        self.refresh_tourists()

    def refresh_polices(self):
        for police in self.polices:
            self.delete_targets(police)
            self.refresh_police(police)

    def delete_targets(self, police):
        removed_targets = []
        for target in self.pirates:
            if self.calc_distance(target.point, police.point) < 1:
                removed_targets.append(target)
        for target in removed_targets:
            self.pirates.remove(target)        

    def refresh_police(self, police):
        pirates = self.get_nearest_pirates(police)
        if pirates:
            # есть пираты. надо прикрыть их лавочку
            distance, pirate = self.get_most_nearest_pirate(police)
            police.direction = (
                    (pirate.point[0] - police.point[0]) / distance,
                    (pirate.point[1] - police.point[1]) / distance,  
            )
        else:
            # просто патрулируем
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
        self.delete_pirates()
    
        if len(self.pirates) < self.MAX_PIRATES_COUNT and \
                random.random() < self.PIRATE_PROBABILITY:
            self.add_new_pirate()

        for pirate in self.pirates:
            self.refresh_pirate(pirate)

    def delete_pirates(self):
        removed_pirates = []
        for pirate in self.pirates:
            if not self.check_limits(pirate.point):
                removed_pirates.append(pirate)
        for pirate in removed_pirates:
            self.pirates.remove(pirate)

    def add_new_pirate(self):
        point = (0, random.randint(0, self.SCREEN_SIZE[1]))
        pirate = Pirate(point, self.generate_direction())
        self.pirates.append(pirate)

    def refresh_pirate(self, pirate):
        polices = self.get_nearest_polices(pirate)
        if polices:
            # надо убегать, т.к. по длизости есть полицейские
            distance, police = self.get_most_nearest_police(polices)
            pirate.direction = (
                (pirate.point[0] - police.point[0]) / distance,
                (pirate.point[1] - police.point[1]) / distance,  
            )
        else:
            # попробуем напасть
            tourists = self.get_nearest_tourists(pirate)
            if tourists:
                distance, tourist = self.get_most_nearest_tourist(tourists)
                pirate.direction = (
                    (tourist.point[0] - pirate.point[0]) / distance,
                    (tourist.point[1] - pirate.point[1]) / distance,  
                )
            else:
                # плаваем и никого не трогаем
                turn = random.random() < self.PIRATE_TURN_PROBABILITY
                if turn:
                    pirate.direction = self.generate_direction()
                while True:
                    new_point = self.calc_new_position(pirate.point, pirate.direction, pirate.SPEED)
                    if self.check_limits(new_point):
                        pirate.point = new_point
                        break
                    pirate.direction = self.generate_direction()

    def refresh_tourists(self):
        self.delete_tourists()

        if len(self.tourists) < self.MAX_TOURISTS_COUNT and \
                random.random() < self.TOURIST_PROBABILITY:
            self.add_new_tourist()

        for tourist in self.tourists:
            self.refresh_tourist(tourist)

    def delete_tourists(self):
        removed_tourists = []
        for tourist in self.tourists:
            if self.calc_distance(tourist.point, tourist.end_point) < 1:
                removed_toursts.append(tourist)
        for o in removed_tourists:
            self.tourists.remove(tourist)

    def add_new_tourist(self):
        point = (0, random.randint(0, self.SCREEN_SIZE[1]))
        end_point = (self.SCREEN_SIZE[0], random.randint(0, self.SCREEN_SIZE[1]))
        pirate = Tourist(point, end_point, self.generate_direction())
        self.tourists.append(pirate)

    def refresh_tourist(self, tourist):
        pirates = self.get_nearest_pirates(tourist)
        if pirates:
            distance, pirate = self.get_most_nearest_police(pirates)
            tourist.direction = (
                (tourist.point[0] - pirate.point[0]) / distance,
                (tourist.point[1] - pirate.point[1]) / distance,  
            )
        else:
            # посылаем в пункт назначения
            distance = self.calc_distance(tourist.end_point, tourist.point)
            tourist.direction = (
                (tourist.end_point[0] - tourist.point[0]) / distance,
                (tourist.end_point[1] - tourist.point[1]) / distance,  
            )
            



    def get_nearest_polices(self, pirate):
        return self.get_nearest_objects(pirate, self.polices)

    def get_most_nearest_police(self, distancies):
        return self.get_nearest_object(distancies)

    def get_nearest_tourists(self, pirate):
        return self.get_nearest_objects(pirate, self.tourists)

    def get_most_nearest_tourist(self, distancies):
        return self.get_nearest_object(distancies)

    def get_nearest_pirates(self, police):
        return self.get_nearest_objects(police, self.pirates)

    def get_most_nearest_pirate(self, distancies):
        return self.get_nearest_object(distancies)
                


    def get_nearest_objects(self, obj, objs):
        objs = []
        for o in objs:
            distance = self.calc_distance(obj, o)
            if distance < self.PIRATE_OBSERVATION_AREA:
        		objs.append((distance, o))
        return objs

    def get_most_nearest_object(self, distancies):
        min_distance = 10000
        obj = None
        for distance in distancies:
            if distance[0] < min_distance:
                min_distance = distance[0]
                obj = distance[1]
        return min_distance, obj



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

        for tourist in self.tourists:
            tourist.draw(self.screen)


Simulator().run()
