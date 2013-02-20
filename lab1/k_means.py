import random


class Cluster:
	def __init__(self, center=None, elements=[]):
		self.center = tuple(center)
		self.elements = list(elements)

	def __eq__(self, other):
		if self.center != other.center:
			return False
		
		if len(self.elements) != len(other.elements):
			return False

		for idx in range(len(self.elements)):
			if self.elements[idx] != other.elements[idx]:
				return False

		return True


def k_means(datas, cluster_count):
	clusters = initialize_clusters(datas, cluster_count)
	print "initialized clusters:"
	num = 0
	for cluster in clusters: 
		print "center:", cluster.center
		print "elements:", cluster.elements
		num += 1

	new_clusters = rebuild_clusters(clusters, datas)
	print "new_clusters:"
	num = 0
	for cluster in new_clusters: 
		print "center:", cluster.center
		print "elements:", cluster.elements
		num += 1

	while clusters != new_clusters:
		clusters = new_clusters
		new_clusters = rebuild_clusters(clusters, datas)
		print "new_clusters:"
		num = 0
		for cluster in new_clusters: 
			print "center:", cluster.center
			print "elements:", cluster.elements
			num += 1

	return clusters


def initialize_clusters(datas, cluster_count):
	if len(datas) < cluster_count:
		raise Exception('Incorrect cluster count')	
	
	def get_random_cluster_centers(datas, cluster_count):
		center_idxs = []
		while len(center_idxs) < cluster_count:
			idx = random.randint(0, cluster_count)
			if idx not in center_idxs:
				center_idxs.append(idx)
		return [datas[idx] for idx in center_idxs]

	centers = get_random_cluster_centers(datas, cluster_count)
	clusters = [Cluster(center=center, elements=[center]) for center in centers]
	for data in datas:
		print "data:", data
		if data not in centers:
			idx = find_nearest_cluster(data, clusters)
			print "idx:", idx
			if data not in clusters[idx].elements:
				clusters[idx].elements.append(data)
	return clusters
			

def find_nearest_cluster(data, clusters):
	def get_dist(vector1, vector2):
		diff = _subtraction(vector1, vector2)
		return _length(diff)

	def _length(vector):
		result = 0
		for coord in vector:
			result += coord * coord
		return result

	def _subtraction(vector1, vector2):
		result = []
		for idx in range(len(vector1)):
			result.append(vector1[idx] - vector2[idx])
		return tuple(result)

	min_dist = get_dist(clusters[0].center, data)
	min_idx = 0
	for idx in range(len(clusters)):
		dist = get_dist(clusters[idx].center, data)
		print "idx:", idx, " dist:", dist
		if dist < min_dist:
			min_dist = dist
			min_idx = idx
	return min_idx


def rebuild_clusters(clusters, datas):
	def calc_center_clusters(clusters):
		new_clusters = []
		for cluster in clusters:
			new_clusters.append(Cluster(center=calc_center_cluster(cluster)))
		return new_clusters

	def calc_center_cluster(cluster):
		center = [0, ] * len(cluster.center)
		print "center:", center
		for element in cluster.elements:
			center = _sum(center, element)
			print "element:", element
			print "center:", center

		for idx in range(len(center)):
			center[idx] = center[idx] / float(len(cluster.center))
			print "center:", center
		print "center:", center
		return tuple(center)

	def _sum(vector1, vector2):
		print "vector1:", vector1
		print "vector2:", vector2
		result = [0, ] * len(vector1)
		for idx in range(len(vector1)):
			result[idx] = vector1[idx] + vector2[idx]
		return result
		
	new_clusters = calc_center_clusters(clusters)
	print "new_clusters after calc centers:"
	num = 0
	for cluster in new_clusters: 
		print "center:", cluster.center
		print "elements:", cluster.elements
		num += 1
	
	for data in datas:
		idx = find_nearest_cluster(data, new_clusters)
		print "idx:", idx, " data:", data
		if data not in new_clusters[idx].elements:
			new_clusters[idx].elements.append(data)
	return new_clusters

			
datas = [(0, 1, 2), (0, 2, 3), (10, 2, 3), (15, 2, 4), (20,3, 2), (7, 3, 4), (2, 2, 0), (0, 0, 0), (15, 0, 0)]
clusters = k_means(datas=datas, cluster_count=3)
num = 0
for cluster in clusters:
	print "cluster # %s" % num
	print "center:", cluster.center
	print "elements:", cluster.elements
	num += 1

