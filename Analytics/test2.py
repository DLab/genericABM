import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA

def perform_kmeans_clustering(data, k):
    """
    Perform K-means clustering on 2D or 3D data. If the data has more than three dimensions,
    it must be reduced to 2 or 3 dimensions using a dimensionality reduction algorithm (e.g., PCA)
    before applying this function.

    Parameters:
        data (np.array): The input data for clustering. It should be a numpy array with 2 or 3 dimensions.
        k (int): The number of clusters to form.

    Returns:
        labels (np.array): Array of labels indicating the cluster each data point belongs to.
        centroids (np.array): Array of centroids for the clusters.
    """
    if data.shape[1] > 3:
        raise ValueError("Data has more than 3 dimensions. Please reduce it before clustering.")
    
    # Create KMeans model
    kmeans = KMeans(n_clusters=k, random_state=0)

    # Fit model on the data
    kmeans.fit(data)

    # Get the cluster labels and centroids
    return kmeans.labels_, kmeans.cluster_centers_


np.random.seed(0)
sample_data = np.random.rand(100, 4)  # 100 points in 4 dimensions

# Reduce data to 2D using PCA for visualization and fitting
pca = PCA(n_components=2)
reduced_data = pca.fit_transform(sample_data)

# Perform clustering with kmeans
labels, centroids = perform_kmeans_clustering(reduced_data, k=3)
print(labels, centroids)