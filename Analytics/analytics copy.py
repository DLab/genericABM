import numpy as np
import numbers
import umap
import random
from scipy.stats import shapiro
from scipy.stats import pearsonr
from sklearn.preprocessing import StandardScaler

def getList(data):
    list = []
    for i in range(0, len(data)):
        sublist = []
        list.append(sublist)
        for j in range(0, 3):
            val = data[i][j]
            if (isinstance(val, np.ndarray)):
                val = float(val[0])
            if isinstance(val, float) and np.isnan(i):
                val = 'nan'
            elif isinstance(val, numbers.Integral):
                val = int(val)
            sublist.append(val)
    return list

def getAnalysisData(allData):
    print(allData['additionalData'])
    dataType = allData['additionalData'][3]
    normalityMethod = allData['normalityAnalysisMethod']['name']
    data = []
    isNormal = None
    ntest = None
    model = None
    pozo = []
    min = None
    max = None
    subtext = 'n_neighbors:' + str(allData['additionalData'][0]) + ', min_dist:' + str(allData['additionalData'][1]) + ', random_state:' + str(allData['additionalData'][2]) + + ', Data type:' + allData['additionalData'][3]
    title = 'Umap'
    if dataType == 'MEAN':
        for item in allData['data']:
            val = np.mean(item['pozo'])
            if (min == None or min > val):
                min = val
            if (max == None or max < val):
                max = val
            data.append([item['initConf'][0], item['initReci'][0], item['initRepu'][0], item['model']['umbralCoop']['params'][0], val])
            pozo.append([val])
    elif dataType == 'MEDIAN':
        for item in allData['data']:
            val = np.median(item['pozo'])
            if (min == None or min > val):
                min = val
            if (max == None or max < val):
                max = val
            data.append([item['initConf'][0], item['initReci'][0], item['initRepu'][0], item['model']['umbralCoop']['params'][0], val])
            pozo.append([val])
    else: #ALL
        for item in allData['data']:
            for val in item['pozo']:
                if (min == None or min > val):
                    min = val
                if (max == None or max < val):
                    max = val
                data.append([item['initConf'][0], item['initReci'][0], item['initRepu'][0], item['model']['umbralCoop']['params'][0], val])
                pozo.append([val])

    #print(allData['additionalData'])
    umapData = umapFunction(data, allData['additionalData'][0])
    embedding = np.hstack([umapData, pozo])
    #print(embedding)
    return {'min': min, 'max':max, 'xAxisName': 'UMap2', 'yAxisName': 'UMap1', 'title': title, 'subtitle': subtext
            , 'data': data, 'result': getList(embedding)}



def normalityTest(data):
    stat, p_value = shapiro(data)
    print(f'Statistic: {stat}, P-value: {p_value}')
    return p_value

def pearsonTest(data1, data2):
    corr, p_value = pearsonr(data1, data2)

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
    