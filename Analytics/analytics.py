import numpy as np
import numbers
import umap
import json
import random
import sklearn
from scipy.stats import shapiro
from scipy.stats import pearsonr
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA

def getValue(val):
    if (isinstance(val, np.ndarray)):
        return float(val[0])
    elif isinstance(val, np.float32):
        return float(val)
    if isinstance(val, float) and np.isnan(val):
        return 'nan'
    elif isinstance(val, numbers.Integral):
        return int(val)

    return val

def getList(data):
    list = []
    n = 0
    #print('getList', data, isinstance(data[0], np.ndarray), flush=True)
    if (len(data) > 0):
        #print('issss', isinstance(data, np.ndarray), flush=True)
        if (isinstance(data[0], np.ndarray)):
            for i in range(0, len(data)):
                sublist = []
                list.append(sublist)
                for j in range(0, len(data[i])):
                    sublist.append(getValue(data[i][j]))
        else:
            for i in range(0, len(data)):
                list.append(getValue(data[i]))
    return list

def getAnalysisData(allData):
    index = 0
    #print(allData['additionalData'], flush=True)

    dimensionalityReductionMethod = allData.get('dimensionalityReductionMethod')
    result = dimensionalityReduction(allData, index, dimensionalityReductionMethod['name'])

    
    clusteringMethod = allData.get('clusteringMethod')
    if (clusteringMethod):
        print('clusteringMethod', clusteringMethod, flush=True)
        resultClustering = None
        normalityAnalysisMethod = allData.get('normalityAnalysisMethod')
        if (normalityAnalysisMethod):
            matriz_np = np.array(result['result'])
            #print(matriz_np[:, 2])
            if normalityTest(matriz_np[:, 2]) > 0.05:
                resultClustering = perform_kmeans_clustering(result['title'], result['subtitle'], result['result'], allData['additionalData'][2])
            else:
                resultClustering = perform_kmedians_clustering(result['title'], result['subtitle'], result['result'], allData['additionalData'][2])
        else:
            if clusteringMethod['name'] == 'k_means':
                resultClustering = perform_kmeans_clustering(result['title'], result['subtitle'], result['result'], allData['additionalData'][1])
            else:
                resultClustering = perform_kmedians_clustering(result['title'], result['subtitle'], result['result'], allData['additionalData'][1])
        resultClustering['data'] = result['data']
        return resultClustering
    #print(result['result'])
    #result['data2D'] = None
    return result

def getItemData(item, graph2DProperties, index, val):
    result = []
    for field in graph2DProperties['fields']:
        if field['typeId'] == 5:
            result.append(item['model'][field['property']]['params'][index])
        else:
            result.append(item[field['property']][index])
    result.append(val)
    return result
    
def dimensionalityReduction(allData, index, name):
    dataType = allData['additionalData'][index][len(allData['additionalData'][index]) - 1]
    graph2DProperties = allData['graph2DProperties']
    data = []
    pozo = []
    min = None
    max = None
    subtext = None
    if name == 'U_map':
        subtext = 'n_neighbors:' + str(allData['additionalData'][index][0]) + ', min_dist:' + str(allData['additionalData'][index][1]) + ', random_state:' + str(allData['additionalData'][index][2]) + ', data type:' + allData['additionalData'][index][3]
    else:
        subtext = 'n_components:' + str(allData['additionalData'][index][0])
    title = name

    if dataType == 'Mean':
        for item in allData['data']:
            val = np.mean(item[graph2DProperties['zAxisField']['property']])
            if (min == None or min > val):
                min = val
            if (max == None or max < val):
                max = val
            data.append(getItemData(item, graph2DProperties, index, val))
            #data.append([item['initConf'][index], item['initReci'][index], item['initRepu'][index], item['model']['umbralCoop']['params'][index], val])
            pozo.append([val])
    elif dataType == 'Median':
        for item in allData['data']:
            val = np.median(item[graph2DProperties['zAxisField']['property']])
            if (min == None or min > val):
                min = val
            if (max == None or max < val):
                max = val
            data.append(getItemData(item, graph2DProperties, index, val))
            #data.append([item['initConf'][index], item['initReci'][index], item['initRepu'][index], item['model']['umbralCoop']['params'][index], val])
            pozo.append([val])
    else: #ALL
        for item in allData['data']:
            for val in item[graph2DProperties['zAxisField']['property']]:
                if (min == None or min > val):
                    min = val
                if (max == None or max < val):
                    max = val
                data.append(getItemData(item, graph2DProperties, index, val))
                #data.append([item['initConf'][index], item['initReci'][index], item['initRepu'][index], item['model']['umbralCoop']['params'][index], val])
                pozo.append([val])

    sc = sklearn.preprocessing.MinMaxScaler(feature_range=(0,1))
    sc.fit(data)
    data = getList(sc.transform(data))
    #print('data', data, flush=True)
    resultData = None
    if name == 'U_map':
        resultData = umapFunction(data, allData['additionalData'][index])
    else:
        resultData = pcaFunction(data, allData['additionalData'][index])
    
    embedding = np.hstack([resultData, pozo])
    #print(embedding)
    return {'min': min, 'max':max, 'xAxisName': 'UMap2', 'yAxisName': 'UMap1', 'title': title, 'subtitle': subtext
            , 'data': data, 'result': getList(embedding)}


def normalityTest(data):
    stat, p_value = shapiro(data)
    print(f'Statistic: {stat}, P-value: {p_value}')
    return p_value

def pearsonTest(data1, data2):
    corr, p_value = pearsonr(data1, data2)
    return p_value

def pcaFunction(data, parameters):
    n_components = parameters[0]
    pca = PCA(n_components=n_components)
    reduced_data = pca.fit_transform(data)
    return reduced_data

def umapFunction(data, parameters):
    # n_neighbors=30, min_dist=0.3, random_state=3
    n_neighbors = parameters[0]
    min_dist = parameters[1]
    random_state = parameters[2]
    # Apply Standard Scaler. Se aplica si los valores son mayores a 1.
    scaler = StandardScaler()
    data_scaled = scaler.fit_transform(data)
    
    # Initialize the UMAP object. Los parámetros se pueden modificar
    reducer = umap.UMAP(n_neighbors=n_neighbors, min_dist=min_dist, verbose=False, random_state=random_state)
    # Fit the model to your data
    embedding = reducer.fit_transform(data_scaled)
    return embedding
    
def perform_kmeans_clustering(title, subtext, data, parameters):
    k = parameters[0]
    print('perform_kmeans_clustering', k)
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
    result = []
    for i in kmeans.labels_:
        result.append([i])
    return {'min': 0, 'max':k, 'xAxisName': 'KMeans2', 'yAxisName': 'KMeans1', 'title': title + '/kmeans', 'subtitle': subtext + '/k:' + str(k)
            , 'type': 'k_means', 'clusterCount': k, 'centers': getList(kmeans.cluster_centers_), 'labels': getList(kmeans.labels_), 'result': getList(np.hstack([data, result]))}

def perform_kmedians_clustering(title, subtext, data, parameters):
    k = parameters[0]
    print('perform_kmedians_clustering', k)
    """
    Perform K-medians clustering on 2D or 3D data. If the data has more than three dimensions,
    it must be reduced to 2 or 3 dimensions using a dimensionality reduction algorithm (e.g., PCA)
    before applying this function.

    Parameters:
        data (np.array): The input data for clustering. It should be a numpy array with 2 or 3 dimensions.
        k (int): The number of clusters to form.

    Returns:
        labels (np.array): Array of labels indicating the cluster each data point belongs to.
        medians (np.array): Array of medians for the clusters.
    """
    if data.shape[1] > 3:
        raise ValueError("Data has more than 3 dimensions. Please reduce it before clustering.")

    # Initial medians
    initial_medians = data[np.random.choice(data.shape[0], k, replace=False)]

    # Create K-medians instance
    kmedians_instance = kmedians(data, initial_medians)

    # Run cluster analysis and obtain results
    kmedians_instance.process()
    clusters = kmedians_instance.get_clusters()
    medians = kmedians_instance.get_medians()

    # Prepare labels array to match input data order
    labels = np.empty(shape=(data.shape[0],), dtype=int)
    for cluster_index, cluster in enumerate(clusters):
        for index in cluster:
            labels[index] = cluster_index

    result = []
    for i in labels:
        result.append([i])

    return {'min': 0, 'max':k, 'xAxisName': 'KMedians2', 'yAxisName': 'KMedians1', 'title': title + '/kmedians', 'subtitle': subtext + '/k:' + str(k)
            , 'type': 'k_median', 'clusterCount': k, 'centers': getList(medians), 'result': getList(labels), 'result': getList(np.hstack([data, result]))}


