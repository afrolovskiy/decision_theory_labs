# -*- coding: utf-8 -*-
from k_means import k_means, find_nearest_cluster


# Система поддержки принятия решений будет помогать определять группу риска 
# водителей по следующим данным:
# (0)стоимость автомобиля, (1)стаж, (2)возраст, (3)количество аварий, (4)пол,  (5)семейное положение
# Группы риска (для страховщиков):
# (2)высокий, (1)средний, (0)низкий

# Обучающая выборка
TRAINING_DATA = [
	(3.2, 10, 41, 10, 1, 1),
	(3.0, 3, 41, 20, 0, 1),
	(3.0, 3, 41, 20, 0, 1),
	(2.5, 10, 33, 15, 1, 0),
	(2.3, 4, 25, 27, 0, 0),
	

	(0.4, 1, 20, 1, 1, 1),
	(0.7, 5, 0, 1, 0, 0),
	(1.5, 5, 32, 2, 1, 0),
	(1.1, 26, 1, 1, 1, 1),
	(4.2, 4, 13, 1, 0, 0),
	(0.8, 2, 22, 1, 0, 0),

	(0.9, 5, 24, 6, 0, 1),
	(0.5, 9, 27, 7, 1, 1),
	(0.5, 9, 27, 7, 1, 1),
	(0.6, 6, 27, 8, 0, 1),
	(0.7, 6, 24, 7, 1, 0),
]

# Тестовая выборка
TESTING_DATA = [
	(1.3, 4, 27, 1, 1, 1),
	(1.1, 6, 30, 10, 0, 0),
	(1.0, 4, 22, 11, 1, 1),
	(0.8, 10, 32, 1, 1, 0),
	(0.8, 10, 32, 6, 1, 1),
	(3.3, 5, 32, 15, 1, 0),
]

# Выделение кластеров и присвоение им группы риска
print "tuple has the form:"
print "(auto cost, expierence, age, number of accidents, sex, married)"

clusters = k_means(TRAINING_DATA, 3)
lvls = {}
for idx in range(len(clusters)):
	cluster = clusters[idx]
	print "%s)" % idx
	print cluster	

	lvl = raw_input("Please, input danger level:")
	lvls[idx] = lvl


# Тестирование и оценка 
for testing_data in TESTING_DATA:
	print "testing data:", testing_data
	idx = find_nearest_cluster(testing_data, clusters)
	print "Man has %s danger level" % lvls[idx]

# precision = 4 / 6 = 0.6666666
# recall = 4 / 5 = 0.8
# 2f = 2pr / (p + r) = 0.74

# Интерфейс
while True:
	print "====================================="
	data = []
	data.append(float(raw_input("Please, input auto cost:")))
	data.append(float(raw_input("Please, input expierence:")))
	data.append(float(raw_input("Please, input age:")))
	data.append(float(raw_input("Please, input number of accidents:")))
	data.append(float(raw_input("Please, input sex(1-male, 0-female):")))
	data.append(float(raw_input("Please, input married(1-true, 0-false):")))
	idx = find_nearest_cluster(data, clusters)
	print "Man has %s danger level" % lvls[idx]

