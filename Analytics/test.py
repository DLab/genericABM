import numpy as np
import umap
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler

# esto es solo para setear el tamano de la figura
plt.rcParams['figure.figsize'] = 15, 8

# Random data REEMPLAZAR POR DATOS REQUERIDOS
data = np.random.rand(100, 8)+2
# Datos con los que se pintaran los puntos,
# debe tener un largo igual a la cantidad de vectores
data_color = np.random.rand(100)

# Apply Standard Scaler. Se aplica si los valores son mayores a 1.
scaler = StandardScaler()
data_scaled = scaler.fit_transform(data)

# Initialize the UMAP object. Los parámetros se pueden modificar
reducer = umap.UMAP(n_neighbors=30, min_dist=0.3, verbose=False, random_state=3)

# Fit the model to your data
embedding = reducer.fit_transform(data_scaled)

# Plotting the result
plt.scatter(embedding[:, 0], embedding[:, 1], c=data_color, s=5.5) # s = size
plt.title('UMAP projection')
plt.xlabel('UMAP1')
plt.ylabel('UMAP2')
plt.show()